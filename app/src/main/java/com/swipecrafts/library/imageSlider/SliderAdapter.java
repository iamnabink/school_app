package com.swipecrafts.library.imageSlider;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/13/2018.
 */

public class SliderAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImageUrls;
    private ImageLoader<String> imageLoader;
    private SparseArray<ImageContainer> mImageViews = new SparseArray<>();

    public SliderAdapter(Context context, List<String> urls, ImageLoader<String> loader) {
        mContext = context;
        mImageUrls = urls;
        this.imageLoader = loader;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        int index = position % mImageUrls.size();
        ImageContainer view = mImageViews.get(index);
        if (view == null){
            view = new ImageContainer(mContext);
            imageLoader.loadImage(view.getImageView(), mImageUrls.get(index));
            container.addView(view, 0);
            mImageViews.put(index, view);
        }
        return view;
    }

    public void setImages(List<String> images) {
        this.mImageUrls = images;
        this.mImageViews.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageUrls.size() == 0 ? 0 : Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View pagerView = (View) object;
        container.removeView(pagerView);
        mImageViews.remove(position%mImageUrls.size());
    }
}
