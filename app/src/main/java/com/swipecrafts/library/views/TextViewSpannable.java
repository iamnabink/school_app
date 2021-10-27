package com.swipecrafts.library.views;

import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by Madhusudan Sapkota on 2/16/2018.
 */

public class TextViewSpannable extends ClickableSpan{

    private boolean isClickable = false;
    private ClickListener listener;


    public TextViewSpannable(boolean isClickable, ClickListener listener) {
        this.isClickable = isClickable;
        this.listener = listener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setUnderlineText(isClickable);
        ds.setColor(Color.parseColor("#1b76d3"));
    }

    @Override
    public void onClick(View view) {
        listener.onClick(view);
    }

    public interface ClickListener{
        void onClick(View view);
    }
}
