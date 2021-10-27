package com.swipecrafts.school.ui.dashboard.videos;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.DisplayUtility;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/18/2018.
 */

class VideoAlbumAdapter extends RecyclerView.Adapter<VideoAlbumAdapter.ImageViewHolder> {
    private List<VideoAlbum> albumList;
    private ImageLoader<VideoAlbum> imageLoader;
    private AdapterListener<VideoAlbum> mListener;

    private int gridItemWidth;
    private int gridItemHeight;


    public VideoAlbumAdapter(Context context, List<VideoAlbum> albums, ImageLoader<VideoAlbum> imageLoader, AdapterListener<VideoAlbum> listener) {
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
    public VideoAlbumAdapter.ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_list, viewGroup, false);
        v.setLayoutParams(getGridItemLayoutParams(v));

        return new ImageViewHolder(v);
    }
    // endregion

    @Override
    public void onBindViewHolder(VideoAlbumAdapter.ImageViewHolder holder, int position) {

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

    public void updateVideoAlbums(List<VideoAlbum> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageView;
        private final TextView albumTitle;
        private final TextView albumDate;

        public VideoAlbum model;

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

