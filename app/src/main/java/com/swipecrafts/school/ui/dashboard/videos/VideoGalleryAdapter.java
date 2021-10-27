package com.swipecrafts.school.ui.dashboard.videos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

public class VideoGalleryAdapter extends RecyclerView.Adapter<VideoGalleryAdapter.ImageViewHolder> {

    private List<VideoAlbumContent> albumList;
    private int gridItemWidth;
    private int gridItemHeight;
    private ImageLoader<VideoAlbumContent> imageLoader;
    private AdapterListener<VideoAlbumContent> mListener;

    public VideoGalleryAdapter(Context context, List<VideoAlbumContent> albums, ImageLoader<VideoAlbumContent> imageLoader, AdapterListener<VideoAlbumContent> listener) {
        this.albumList = albums;
        this.mListener = listener;
        this.imageLoader = imageLoader;

        int screenWidth = DisplayUtility.getScreenWidth(context);
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(context)) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }
        gridItemWidth = screenWidth / numOfColumns;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_thumbnail, viewGroup, false);
        v.setLayoutParams(getGridItemLayoutParams(v));

        return new ImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        holder.model = albumList.get(position);
        imageLoader.loadImage(holder.imageView, holder.model);

        holder.view.setOnClickListener( (view) -> mListener.onItemClicked(holder.model));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    private ViewGroup.LayoutParams getGridItemLayoutParams(View view) {
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        layoutParams.width = gridItemWidth;
        layoutParams.height = gridItemWidth;

        return layoutParams;
    }

    public void updateAlbums(List<VideoAlbumContent> albumList) {
        Log.e("albumContentList", albumList.size() + "");
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        public VideoAlbumContent model;

        private View view;


        public ImageViewHolder(final View view) {
            super(view);
            this.view = view;

            imageView = (ImageView) view.findViewById(R.id.galleryImageView);
        }
    }

}