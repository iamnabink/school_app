package com.swipecrafts.school.ui.notification.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/18/2018.
 */

public class NoticeListAdapter extends RecyclerView.Adapter<NoticeListAdapter.NoticeHolder> {

    private final static int FADE_DURATION = 1000; //FADE_DURATION in milliseconds
    private final ImageLoader<Notification> imageLoader;
    private int lastPosition = -1;

    private AdapterListener<Notification> listener;
    private List<Notification> notifications;

    public NoticeListAdapter(AdapterListener<Notification> listener, List<Notification> notifications, ImageLoader<Notification> imageLoader) {
        this.listener = listener;
        this.imageLoader = imageLoader;
        this.notifications = notifications;
    }

    @Override
    public NoticeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        return new NoticeHolder(view);
    }

    @Override
    public void onBindViewHolder(NoticeHolder holder, int position) {
       holder.notification = notifications.get(position);

        holder.noticeHeader.setText(holder.notification.getTitle());
        holder.noticeDesc.setText(Html.fromHtml(holder.notification.getDescription()));

        if (holder.notification.getRemoteImgUrl() != null && !TextUtils.isEmpty(holder.notification.getRemoteImgUrl()) && !holder.notification.getRemoteImgUrl().endsWith("/")){
            holder.noticeImg.setVisibility(View.VISIBLE);
            if (imageLoader != null){
                imageLoader.loadImage(holder.noticeImg, holder.notification);
            }
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClicked(notifications.get(holder.getAdapterPosition())));
        holder.setFadeAnimation(holder.itemView, position);

    }

    @Override
    public void onViewDetachedFromWindow(NoticeHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
    }

    public List<Notification> getNotifications() {
        return notifications;
    }

    class NoticeHolder extends RecyclerView.ViewHolder{

        private TextView noticeHeader;
        private TextView noticeDesc;
        private ImageView noticeImg;

        View itemView;
        private int lastPosition = -1;

        public Notification notification;


        NoticeHolder(View itemView) {
            super(itemView);
            findView(itemView);
            this.itemView = itemView;
        }

        private void findView(View view){
            noticeHeader = (TextView) view.findViewById(R.id.notice_header);
            noticeDesc = (TextView) view.findViewById(R.id.notice_desc);
            noticeImg = (ImageView) view.findViewById(R.id.notice_img);
        }

        private void setFadeAnimation(View view, int currentPosition) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (currentPosition > lastPosition) {
                AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
                anim.setDuration(FADE_DURATION);
                view.startAnimation(anim);
                lastPosition = currentPosition;
            }
        }

        private void setScaleAnimation(View view, int currentPosition) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (currentPosition > lastPosition) {
                ScaleAnimation anim = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                anim.setDuration(FADE_DURATION);
                view.startAnimation(anim);
                lastPosition = currentPosition;
            }
        }
    }

}
