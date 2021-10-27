package com.swipecrafts.school.ui.notification.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.library.sectionrecycler.SectionedAdapter;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 7/5/2018.
 */
public class NoticesAdapter extends SectionedAdapter<NoticesAdapter.ContentAdapter> {

    private HashMap<String, List<Notification>> sections;
    private SparseArray<String> headers;

    private List<Notification> notifications;
    private AdapterListener<Notification> listener;
    private ImageLoader<Notification> imageLoader;

    public NoticesAdapter(List<Notification> notifications, AdapterListener<Notification> listener, com.swipecrafts.school.utils.listener.ImageLoader<Notification> imageLoader) {
        this.notifications = notifications;
        this.listener = listener;
        this.imageLoader = imageLoader;
        this.sections = new HashMap<>();
        this.headers = new SparseArray<>();
        initSections();
    }

    private void initSections() {
        Date today = clearDate(new Date());
        sections.clear();
        headers.clear();

//          res = 0  => if this Date is equal to the argument Date. // today
//          res < 0  => if this Date is before the Date argument. // tomorrow
//          res > 0  => if this Date is after the Date argument. // yesterday

        int headerIndex = 0;
        for(Notification notice: notifications){
            String header;
            Date noticeDate = clearDate(notice.getTime());
            int check = today.compareTo(noticeDate);
            if (check == 0) {
                header = "Today";
            } else if (check < 0) {
                header = "Upcoming";
            } else {
                header = DateFormat.format("MMM yyyy", noticeDate).toString();
            }

            List<Notification> data = sections.get(header);
            if (data == null) {
                data = new ArrayList<>();
                headers.put(headerIndex, header);
                sections.put(header, data);
                headerIndex++;
            }
            data.add(notice);
        }
    }

    private Date clearDate(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return date;
    }

    @Override
    protected String getSectionHeaderTitle(int section) {
        return headers.get(section);
    }

    @Override
    protected int getSectionCount() {
        return sections.size();
    }

    @Override
    protected int getItemCountForSection(int section) {
        String header = headers.get(section);
        return sections.get(header).size();
    }

    @Override
    protected ContentAdapter onCreateContentViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notice_item, parent, false);
        return new ContentAdapter(view);
    }

    @Override
    protected void onBindContentViewHolder(ContentAdapter holder, int section, int position) {
        String headerKey = headers.get(section);
        holder.notification = sections.get(headerKey).get(position);

        holder.noticeHeader.setText(holder.notification.getTitle());
        holder.noticeDesc.setText(Html.fromHtml(holder.notification.getDescription()));

        if (holder.notification.getRemoteImgUrl() != null && !TextUtils.isEmpty(holder.notification.getRemoteImgUrl()) && !holder.notification.getRemoteImgUrl().endsWith("/")){
            holder.noticeImg.setVisibility(View.VISIBLE);
            if (imageLoader != null){
                imageLoader.loadImage(holder.noticeImg, holder.notification);
            }
        }else {
            holder.noticeImg.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClicked(holder.notification));
    }

    public void setNotifications(List<Notification> notifications) {
        this.notifications = notifications;
        initSections();
        notifyDataSetChanged();
    }

    public class ContentAdapter extends RecyclerView.ViewHolder {
        private TextView noticeHeader;
        private TextView noticeDesc;
        private ImageView noticeImg;
        private View itemView;

        public Notification notification;

        ContentAdapter(View itemView) {
            super(itemView);
            findView(itemView);
            this.itemView = itemView;
        }

        private void findView(View view){
            noticeHeader = (TextView) view.findViewById(R.id.notice_header);
            noticeDesc = (TextView) view.findViewById(R.id.notice_desc);
            noticeImg = (ImageView) view.findViewById(R.id.notice_img);
        }
    }

}
