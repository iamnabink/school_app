package com.swipecrafts.library.imageSlider;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/13/2018.
 */

public class SliderView extends ViewPager implements ImageLoader<String> {

    private static final long DELAY_MS = 5000;

    private static final Interpolator mInterpolator = t -> {
        t -= 1.0f;
        return t * t * t * t * t + 1.0f;
    };

    private boolean shouldAutoLoop = true;

    private Handler handler = new Handler();
    private Runnable imageLooping = new Runnable() {
        @Override
        public void run() {
            if (shouldAutoLoop) {
                nextImage();
                handler.postDelayed(this, DELAY_MS);
            }
        }
    };

    private int indicatorColor = Color.GRAY;
    private int activeIndicatorColor = Color.BLACK;
    private List<String> urls = new ArrayList<>();
    private ImageLoader<String> loader;
    private SliderAdapter mAdapter;
    private SpeedScroll mScroller;
    private TransformType pageTransformer = TransformType.NONE;

    public SliderView(Context context) {
        super(context);
        init(context, null);
    }

    public SliderView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        try {
            Field scroller = ViewPager.class.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            Field interpolator = ViewPager.class.getDeclaredField("sInterpolator");
            interpolator.setAccessible(true);

            mScroller = new SpeedScroll(getContext(), (Interpolator) interpolator.get(null));
            scroller.set(this, mScroller);
        } catch (Exception ignore) {
        }

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SliderView);

        try {
            setIndicatorColor(
                    a.getInteger(R.styleable.SliderView_indicatorColor, indicatorColor)
            );

            setActiveIndicatorColor(
                    a.getInteger(R.styleable.SliderView_activeIndicatorColor, activeIndicatorColor)
            );

            setAutoImageLooping(
                    a.getBoolean(R.styleable.SliderView_autoImageLoop, false)
            );

            int type = a.getInt(R.styleable.SliderView_imageTransition, TransformType.NONE.value);
            setPageTransformer(
                  TransformType.valueOf(type)
            );

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            a.recycle();
        }


        mAdapter = new SliderAdapter(getContext(), urls, this);
        setOverScrollMode(OVER_SCROLL_NEVER);
        addOnPageChangeListener(new ImageSliderPageListener(this));
        setAdapter(mAdapter);
        setPageTransformer(true, (new ImageSliderTransformer(pageTransformer)));
    }

    public void setAutoImageLooping(boolean enable) {
        this.shouldAutoLoop = enable;
        if (shouldAutoLoop) {
            handler.removeCallbacks(imageLooping);
            handler.postDelayed(imageLooping, DELAY_MS);
        }
    }

    public void nextImage() {
        int mCurrentPosition = getCurrentItem() + 1;
        setCurrentItem(mCurrentPosition);
    }

    public void previousImage() {
        int mCurrentPosition = getCurrentItem();
        int total = urls.size();

        int index = mCurrentPosition -1;
        if (mCurrentPosition == 0) {
            index = (total * mCurrentPosition) / (total - 1);
        }
        setCurrentItem(index);
    }


    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public void setActiveIndicatorColor(int activeIndicatorColor) {
        this.activeIndicatorColor = activeIndicatorColor;
    }

    public void addImage(String... imageUrls) {
        this.urls.addAll(Arrays.asList(imageUrls));
        this.mAdapter.setImages(urls);
    }

    public void removeImages() {
        this.urls.clear();
        this.mAdapter.setImages(urls);
    }

    public void removeImage(String url) {
        this.urls.remove(url);
        this.mAdapter.setImages(urls);
    }

    public Collection<String> getImages() {
        return urls;
    }

    public void setImages(List<FeaturesImage> urls) {
        for (FeaturesImage fi : urls) {
            this.urls.add(fi.getUrl());
        }
        this.mAdapter.setImages(this.urls);
    }

    public int getTotalItem() {
        return mAdapter == null ? 0 : mAdapter.getCount();
    }

    public ImageLoader<String> getLoader() {
        return loader;
    }

    public void setLoader(ImageLoader<String> loader) {
        this.loader = loader;
    }

    public void setScrollDurationFactor(double scrollFactor) {
        mScroller.setScrollDurationFactor(scrollFactor);
    }

    public void setPageTransformer(TransformType pageTransformer) {
        this.pageTransformer = pageTransformer;
    }

    public void clean() {
        handler.removeCallbacks(imageLooping);
        imageLooping = null;
    }

    @Override
    public void loadImage(ImageView imageView, String url) {
        if (loader != null) {
            loader.loadImage(imageView, url);
        }
    }
}
