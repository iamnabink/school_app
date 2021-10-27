package com.swipecrafts.library.visualizer;

import android.media.audiofx.Visualizer;
import android.os.Build;
import android.util.Log;
import android.view.SurfaceView;

import com.swipecrafts.library.visualizer.renderer.IRenderer;

import java.lang.ref.WeakReference;

/**
 * Created by Madhusudan Sapkota on 7/4/2018.
 */
public class VisualizerManager {

    WeakReference<SurfaceView> renderViewWR = null;
    IRenderer[] renderers = null;
    private Visualizer mVisualizer;
    private VisualizerRenderWorker mRenderer = new VisualizerRenderWorker();
    private byte[] mWaveBuffer;
    private byte[] mFftBuffer;
    private Object mStateBlock = new Object();
    private Visualizer.OnDataCaptureListener mDataCaptureListener = new Visualizer.OnDataCaptureListener() {
        @Override
        public void onWaveFormDataCapture(Visualizer visualizer, byte[] waveform, int samplingRate) {
            byte[] waveBuffer = mWaveBuffer;
            if (waveform == null || waveform.length != waveBuffer.length) {
                return;
            }
            System.arraycopy(waveform, 0, waveBuffer, 0, waveform.length);
            mRenderer.updateWaveData(waveBuffer);
        }

        @Override
        public void onFftDataCapture(Visualizer visualizer, byte[] fft, int samplingRate) {
            byte[] fftBuffer = mFftBuffer;
            if (fft == null || fft.length != fftBuffer.length) {
                return;
            }
            System.arraycopy(fft, 0, fftBuffer, 0, fft.length);
            mRenderer.updateFftData(fftBuffer);
        }
    };

    /**
     * Initialize visualizer, you should use it in [android.app.Activity.onCreate].
     *
     * @param audioSession system wide unique audio session identifier. see [android.media.audiofx.Visualizer].
     */
    public boolean init(int audioSession) {
        synchronized (mStateBlock) {
            try {
                Visualizer visualizer = new Visualizer(audioSession);
                visualizer.setCaptureSize(512);
                visualizer.setScalingMode(Visualizer.SCALING_MODE_NORMALIZED);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    visualizer.setMeasurementMode(Visualizer.MEASUREMENT_MODE_NONE);
                }
                visualizer.setDataCaptureListener(mDataCaptureListener, Visualizer.getMaxCaptureRate(), true, true);
                visualizer.setEnabled(true);
                mWaveBuffer = new byte[visualizer.getCaptureSize()];
                mFftBuffer = new byte[visualizer.getCaptureSize()];
                mVisualizer = visualizer;
                return true;
            }catch (Exception e){
                Log.e("Visualiser Error", e.getMessage() +" AudioSessionId: "+ audioSession);
                return false;
            }
        }
    }

    /**
     * Release visualizer instance, you should use it in [android.app.Activity.onDestroy].
     */
    public void release() {
        stop();
        if (mVisualizer != null)
        synchronized (mStateBlock) {
            mVisualizer.setEnabled(false);
            mVisualizer.setDataCaptureListener(null, Visualizer.getMaxCaptureRate(), true, true);
            mVisualizer.release();
            mVisualizer = null;
            mWaveBuffer = null;
            mFftBuffer = null;
        }
    }

    /**
     * Start the render work, call it after visualizer has been initialized!
     *
     * @param view         the view which visualizer will render to.
     * @param newRenderers a list of renderer that control the view render work.
     */
    public void start(SurfaceView view, IRenderer[] newRenderers) {
        synchronized (mStateBlock) {
            Visualizer visualizer = mVisualizer;
            if (visualizer == null) {
                throw new IllegalStateException("You must call VisualizerManager.init() first!");
            }
            if (newRenderers.length <= 0) {
                throw new IllegalStateException("Renders is empty!");
            }
            renderViewWR = new WeakReference(view);
            renderers = newRenderers;
            mRenderer.start(new VisualizerRenderWorker.RenderCore(visualizer.getCaptureSize(), view, newRenderers));
        }
    }

    /**
     * Stop the render work.
     */
    public void stop() {
        synchronized (mStateBlock) {
            renderViewWR = null;
            renderers = null;
            mRenderer.stop();
        }
    }

}
