package com.swipecrafts.library.visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Madhusudan Sapkota on 7/5/2018.
 */
public class ColumnarDynamicBarRenderer implements IRenderer {

    private Paint mPaint;
    private Rect mLastDrawArea = new Rect();
    private RectF[] mRenderColumns;
    // per column' width equals to twice of gap
    private float mGapRatio = 0.7F;
    private float mRadius = 10F;
    private float mHalfHeight = 0F;

    public ColumnarDynamicBarRenderer(Paint paint) {
        mPaint = paint;
    }

    public ColumnarDynamicBarRenderer() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public void onStart(int captureSize) {
//        mRenderColumns = Array(Math.min(40, captureSize), { _ -> new RectF(0F, -5F, 0F, 5F) });
        int size = Math.min(36, captureSize);
        mRenderColumns = new RectF[size];
        for (int i = 0; i < mRenderColumns.length; i++) {
            mRenderColumns[i] = new RectF(0F, -5F, 0F, 0F);
        }
        mLastDrawArea.set(0, 0, 0, 0);
    }

    @Override
    public void onStop() {
    }

    @Override
    public void calculate(Rect drawArea, byte[] data) {
        if (drawArea != mLastDrawArea) {
            calculateRenderData(drawArea);
            mLastDrawArea.set(drawArea);
        }
        updateWave(data);
    }


    private void transformWaveValue(byte value, RectF rectF) {
        rectF.bottom = (((float) (((int) value) & 0xFF)) - 128F) / 128F * mHalfHeight;
        rectF.bottom = (rectF.bottom == 0F) ? 5F : rectF.bottom;
        rectF.top = -rectF.bottom;
    }

    private void updateWave(byte[] data) {
        if (mRenderColumns.length >= data.length) {
            for (int i = 0; i < data.length; i++) {
                byte b = data[i];
                transformWaveValue(b, mRenderColumns[i]);
            }

        } else {
            int step = data.length / mRenderColumns.length;
            for (int q = 0; q < mRenderColumns.length; q++) {
                RectF rectF = mRenderColumns[q];
                transformWaveValue(data[q * step], rectF);
            }
        }
    }

    private void calculateRenderData(Rect drawArea) {
        mHalfHeight = drawArea.height() / 3F;
        float perGap = ((float) drawArea.width()) / (mRenderColumns.length * (mGapRatio + 1) + 1);
        for (int i = 0; i < mRenderColumns.length; i++) {
            RectF rect = mRenderColumns[i];
            rect.left = ((i + 1) * (1 + mGapRatio) - mGapRatio) * perGap;
            rect.right = rect.left + mGapRatio * perGap;
        }
    }

    @Override
    public void render(Canvas canvas) {
        canvas.save();
        canvas.translate(((float) mLastDrawArea.left), (mLastDrawArea.top + mLastDrawArea.bottom) / 2F);
        for (RectF it : mRenderColumns) {
            canvas.drawRoundRect(it, mRadius, mRadius, mPaint);
        }
        canvas.restore();
    }

    @Override
    public DataType getInputDataType() {
        return DataType.WAVE;
    }
}
