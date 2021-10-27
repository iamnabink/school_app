package com.swipecrafts.school.ui.dashboard.gallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.ImageAlbumContent;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/16/2018.
 */

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ImageViewHolder> {

    private List<ImageAlbumContent> albumList;
    private int gridItemWidth;
    private int gridItemHeight;
    private ImageLoader<ImageAlbumContent> imageLoader;
    private AdapterListener<Integer> mListener;

    public GalleryAdapter(Context context, List<ImageAlbumContent> albums, ImageLoader<ImageAlbumContent> imageLoader, AdapterListener<Integer> listener) {
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
    // endregion

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {

        holder.model = albumList.get(position);
        imageLoader.loadImage(holder.imageView, holder.model);

        holder.view.setOnClickListener( (view) ->{
            mListener.onItemClicked(position);
        });
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

    public void updateAlbums(List<ImageAlbumContent> albumList) {
        Log.e("albumContentList", albumList.size() + "");
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        public ImageAlbumContent model;
        public ImageView btn;

        private View view;


        public ImageViewHolder(final View view) {
            super(view);
            this.view = view;

            imageView = (ImageView) view.findViewById(R.id.galleryImageView);
            btn = (ImageView) view.findViewById(R.id.play_button);
            btn.setVisibility(View.GONE);
        }
    }

}