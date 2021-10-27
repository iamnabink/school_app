package com.swipecrafts.library.imageSlider;

/**
 * Created by Madhusudan Sapkota on 6/13/2018.
 */
public enum TransformType {
    NONE(0),
    FLOW(1),
    DEPTH(2),
    ZOOM(3),
    SLIDE_OVER(4),
    FADE(5);

    final int value;

    TransformType(int value) {
        this.value = value;
    }

    public static TransformType valueOf(int value){
        TransformType type = values()[value];
        if (type == null) type = NONE;
        return type;
    }
}
