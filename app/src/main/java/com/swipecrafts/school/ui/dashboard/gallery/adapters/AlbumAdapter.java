package com.swipecrafts.school.ui.dashboard.gallery.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Album;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/18/2018.
 */

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ImageViewHolder> {
    private List<Album> albumList;
    private com.swipecrafts.school.utils.listener.ImageLoader<Album> imageLoader;
    private AdapterListener<Album> mListener;

    private int gridItemWidth;
    private int gridItemHeight;


    public AlbumAdapter(Context context, List<Album> albums, com.swipecrafts.school.utils.listener.ImageLoader<Album> imageLoader, AdapterListener<Album> listener) {
        this.albumList = albums;
        this.mListener = listener;
        this.imageLoader = imageLoader;

        int screenWidth = DisplayUtility.getScreenWidth(context);
        int numOfColumns;
        if (DisplayUtility.isInLandscapeMode(context)) {
            numOfColumns = 3;
        } else {
            numOfColumns = 2;
        }
        gridItemWidth = screenWidth / numOfColumns;
    }

    @Override
    public AlbumAdapter.ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_list, viewGroup, false);
        v.setLayoutParams(getGridItemLayoutParams(v));

        return new ImageViewHolder(v);
    }
    // endregion

    @Override
    public void onBindViewHolder(AlbumAdapter.ImageViewHolder holder, int position) {

        holder.model = albumList.get(position);
        imageLoader.loadImage(holder.imageView, holder.model);
        holder.albumTitle.setText(holder.model.getAlbumName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String albumDate = sdf.format(holder.model.getAlbumCreatedAt());
        holder.albumDate.setText(albumDate);

        holder.view.setOnClickListener((view) -> {
            mListener.onItemClicked(holder.model);
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

    public void updateAlbums(List<Album> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView albumTitle;
        private final TextView albumDate;

        public Album model;

        private View view;

        public ImageViewHolder(final View view) {
            super(view);
            this.view = view;

            imageView = (ImageView) view.findViewById(R.id.albumThumbnailIV);
            albumTitle = (TextView) view.findViewById(R.id.albumTitleTV);
            albumDate = (TextView) view.findViewById(R.id.albumDateTV);
        }
    }
}

