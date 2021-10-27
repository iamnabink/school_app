package com.swipecrafts.library.visualizer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.Process;
import android.util.Log;
import android.view.SurfaceView;

import com.swipecrafts.library.visualizer.renderer.IRenderer;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Madhusudan Sapkota on 7/4/2018.
 */

public class VisualizerRenderWorker {

    public static final int MSG_RENDER = 0;
    public static final int MSG_START = 1;
    public static final int MSG_STOP = 2;
    public static final int MSG_UPDATE_FFT = 3;
    public static final int MSG_UPDATE_WAVE = 4;

    public static final int STATE_INIT = 0;
    public static final int STATE_START = 1;
    public static final int STATE_STOP = 2;
    private Handler mRenderHandler;
    private AtomicInteger mState = new AtomicInteger(STATE_INIT);
    private FpsHelper mFpsHelper = new FpsHelper();
    private Rect mDrawArea = new Rect();
    private RenderCore mRenderCore;
    public VisualizerRenderWorker() {
        HandlerThread ht = new HandlerThread("Render Thread", Process.THREAD_PRIORITY_DISPLAY);
        ht.start();
        mRenderHandler = new Handler(ht.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case MSG_RENDER:
                        processRenderEvent();
                        break;
                    case MSG_START:
                        processStartEvent((RenderCore) msg.obj);
                        break;
                    case MSG_STOP:
                        processStopEvent();
                        break;
                    case MSG_UPDATE_FFT:
                        processUpdateFftEvent((byte[]) msg.obj);
                        break;
                    case MSG_UPDATE_WAVE:
                        processUpdateWaveEvent((byte[]) msg.obj);
                        break;
                }
            }
        };
    }

    private void processStartEvent(RenderCore core) {
        if (mState.get() != STATE_START) {
            return;
        }
        mRenderHandler.removeMessages(MSG_RENDER);
        if (core != null)
        for (IRenderer it : core.renderers) {
            it.onStart(core.captureSize);
        }
        mRenderCore = core;
        mRenderHandler.sendEmptyMessage(MSG_RENDER);
    }

    private void processStopEvent() {
        if (mState.get() != STATE_STOP) {
            return;
        }
        RenderCore core = mRenderCore;
        mRenderHandler.removeMessages(MSG_RENDER);
        if (core != null)
        for (IRenderer it : core.renderers) {
            it.onStop();
        }
        mRenderCore = null;
    }

    private void processUpdateFftEvent(byte[] data) {
        byte[] fft = mRenderCore.fftData;
        System.arraycopy(data, 0, fft, 0, data.length);
    }

    private void processUpdateWaveEvent(byte[] data) {
        RenderCore core = mRenderCore;
        if (core != null) {
            System.arraycopy(data, 0, core.waveData, 0, data.length);
            core.waveDataArrived = true;
        }
    }

    private void processRenderEvent() {
        mFpsHelper.start();
        //Make sure just one
        mRenderHandler.removeMessages(MSG_RENDER);
        if (mState.get() != STATE_START) {
            return;
        }
        RenderCore core = mRenderCore;
        if (core != null){
            renderInternal(core);
        }
        mFpsHelper.end();
        scheduleNextRender(mFpsHelper.nextDelayTime());
    }

    private void scheduleNextRender(Long awaitTime) {
        boolean ret = mRenderHandler.sendEmptyMessageDelayed(MSG_RENDER, awaitTime);
        if (!ret) {
            Log.e("Visualiser", "schedule next render error");
        }
    }

    private void renderInternal(RenderCore renderCore) {
        if (mState.get() != STATE_START) {
            return;
        }
        Canvas canvas = renderCore.surfaceView.getHolder().lockCanvas();
        if (canvas != null)
            try {
                mDrawArea.set(0, 0, canvas.getWidth(), canvas.getHeight());
                for (IRenderer it : renderCore.renderers) {
                    switch (it.getInputDataType()) {
                        case WAVE:
                            if (renderCore.waveDataArrived) {
                                it.calculate(mDrawArea, renderCore.waveData);
                            }
                            break;
                        case FFT:
                            it.calculate(mDrawArea, renderCore.fftData);
                            break;
                    }
                }
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
//            canvas.clear();
                for (IRenderer it : renderCore.renderers) {
                    it.render(canvas);
                }
            } finally {
                renderCore.surfaceView.getHolder().unlockCanvasAndPost(canvas);
            }
    }

    void start(RenderCore renderCore) {
        mState.set(STATE_START);
        mRenderHandler.removeMessages(MSG_START);
        Message.obtain(mRenderHandler, MSG_START, renderCore).sendToTarget();
    }

    void stop() {
        mState.set(STATE_STOP);
        mRenderHandler.removeMessages(MSG_STOP);
        mRenderHandler.sendEmptyMessage(MSG_STOP);
    }

    void updateFftData(byte[] data) {
        if (mState.get() != STATE_START) {
            return;
        }
        Message.obtain(mRenderHandler, MSG_UPDATE_FFT, data).sendToTarget();
    }

    void updateWaveData(byte[] data) {
        if (mState.get() != STATE_START) {
            return;
        }
        Message.obtain(mRenderHandler, MSG_UPDATE_WAVE, data).sendToTarget();
    }

    public static class RenderCore {
        final IRenderer[] renderers;
        final SurfaceView surfaceView;
        final int captureSize;

        byte[] fftData;
        byte[] waveData;
        boolean waveDataArrived;

        public RenderCore(int captureSize, SurfaceView surfaceView, IRenderer[] renderer) {
            this.surfaceView = surfaceView;
            this.captureSize = captureSize;
            this.renderers = renderer;

            fftData = new byte[captureSize];
            waveData = new byte[captureSize];
            waveDataArrived = false;
        }
    }

}
