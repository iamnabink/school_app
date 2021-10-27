package com.swipecrafts.school.ui.dashboard.chat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.library.views.CircleImageView;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/18/2018.
 */

class ChatUserListAdapter extends RecyclerView.Adapter<ChatUserListAdapter.ImageViewHolder> {
    private List<Teacher> teacherList;
    private ImageLoader<Teacher> imageLoader;
    private AdapterListener<Teacher> mListener;


    public ChatUserListAdapter(List<Teacher> teachers, ImageLoader<Teacher> imageLoader, AdapterListener<Teacher> listener) {
        this.teacherList = teachers;
        this.mListener = listener;
        this.imageLoader = imageLoader;
    }

    @Override
    public ChatUserListAdapter.ImageViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_list, viewGroup, false);
        return new ImageViewHolder(view);
    }
    // endregion

    @Override
    public void onBindViewHolder(ChatUserListAdapter.ImageViewHolder holder, int position) {

        holder.model = teacherList.get(position);
        imageLoader.loadImage(holder.imageView, holder.model);
        holder.albumTitle.setText(holder.model.getTeacherName());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String albumDate = sdf.format(holder.model.getLastOnline());
        holder.albumDate.setText(albumDate);

        holder.view.setOnClickListener((view) -> {
            if (mListener != null) {
                mListener.onItemClicked(holder.model);
            }
        });
    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public void updateVideoAlbums(List<Teacher> teacherList) {
        this.teacherList = teacherList;
        notifyDataSetChanged();
    }


    public static class ImageViewHolder extends RecyclerView.ViewHolder {
        private final CircleImageView imageView;
        private final TextView albumTitle;
        private final TextView albumDate;

        public Teacher model;

        private View view;

        public ImageViewHolder(final View view) {
            super(view);
            this.view = view;

            imageView = (CircleImageView) view.findViewById(R.id.albumThumbnailIV);
            albumTitle = (TextView) view.findViewById(R.id.albumTitleTV);
            albumDate = (TextView) view.findViewById(R.id.albumDateTV);
        }
    }
}

