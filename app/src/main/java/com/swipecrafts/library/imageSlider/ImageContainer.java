package com.swipecrafts.library.imageSlider;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by Madhusudan Sapkota on 3/13/2018.
 */

public class ImageContainer extends RelativeLayout{
    private final ImageView imageView;

    public ImageContainer(Context context) {
        super(context);

        imageView = new ImageView(getContext());
        imageView.setPadding(15,10,15,10);
        RelativeLayout.LayoutParams lytImgParam = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(lytImgParam);
        addView(imageView);
    }

    public ImageView getImageView() {
        return imageView;
    }
}
