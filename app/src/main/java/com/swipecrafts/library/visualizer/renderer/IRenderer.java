package com.swipecrafts.library.visualizer.renderer;

import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Madhusudan Sapkota on 7/4/2018.
 */
public interface IRenderer {
    /**
     * Visualizer data type.
     */
    enum DataType {
        FFT, WAVE
    }

    /**
     * The renderer can do some initialization here.
     * @param captureSize current capture size.
     */
    void onStart(int captureSize);

    /**
     * The renderer can do some release here.
     */
    void onStop();

    /**
     * Calculate render data, do some pure calculating work here.
     * @param drawArea current canvas draw area.
     * @param data current capture data which it's type is one of [DataType].
     */
    void calculate(Rect drawArea, byte[] data);

    /**
     * Render calculated data.
     * @param canvas the canvas on which renderer will drawn.
     */
    void render(Canvas canvas);

    /**
     * Return The type of data used by the renderer.
     *
     * @return the type of data.
     */
    DataType getInputDataType();
}
