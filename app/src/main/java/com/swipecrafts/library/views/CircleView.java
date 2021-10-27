package com.swipecrafts.library.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.swipecrafts.school.R;

/**
 * Created by Madhusudan Sapkota on 4/2/2018.
 */

public class CircleView extends View {

    private Paint paint;
    private int color;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // real work here
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleView, 0, 0);

        try {
            color = a.getColor(R.styleable.CircleView_circleColor, 0xff000000);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }
        init();
    }

    public void init() {
        paint = new Paint();
    }

    public void setColor(int color) {
        this.color = color;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canvas != null) {
            paint.setColor(color);
            canvas.drawCircle(getHeight() / 2, getWidth() / 2, getWidth() / 2, paint);
        }
    }

}