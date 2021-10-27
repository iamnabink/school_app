package com.swipecrafts.library.visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

/**
 * Created by Madhusudan Sapkota on 7/4/2018.
 */
public class ColumnarBaseBarRenderer implements IRenderer {
    private Paint mPaint;
    private Rect mLastDrawArea = new Rect();
    private RectF[] mRenderColumns;
    // per column' width equals to twice of gap
    private float mGapRatio = 0.7F;
    private float mRadius = 10F;

    public ColumnarBaseBarRenderer(Paint paint) {
        mPaint = paint;
    }

    public ColumnarBaseBarRenderer() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);
    }

    @Override
    public void onStart(int captureSize) {
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

    private void updateWave(byte[] data) {
        for (int i = 0; i <= Math.min(data.length / 2, mRenderColumns.length-1); i++) {
            // Calculate dbValue
            byte rfk = data[i];
            byte ifk = data[i + 1];
            float magnitude = (rfk * rfk + ifk * ifk);
            float dbValue = (float) (75 * Math.log10((double) magnitude));
            RectF rectF = mRenderColumns[i];
            rectF.top = -dbValue;
            rectF.top = (rectF.top > -5F) ? -5F : rectF.top;
        }
    }

    private void calculateRenderData(Rect drawArea) {
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
        canvas.translate(((float)mLastDrawArea.left), (mLastDrawArea.top + mLastDrawArea.bottom) / 2F);
        for (RectF it: mRenderColumns) {
            canvas.drawRoundRect(it, mRadius, mRadius, mPaint);
        }
        canvas.restore();
    }

    @Override
    public DataType getInputDataType() {
        return DataType.FFT;
    }
}
