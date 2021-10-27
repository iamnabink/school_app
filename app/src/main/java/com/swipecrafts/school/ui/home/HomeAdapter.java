package com.swipecrafts.school.ui.home;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.library.calendar.core.Interval;
import com.swipecrafts.school.utils.adapter.AnimationAdapter;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 2/28/2018.
 */

public class HomeAdapter extends AnimationAdapter<HomeAdapter.ViewHolder> {
    private final String TAG = HomeAdapter.class.getSimpleName();
    private List<HomeModel> homeItemList;
    private LinkedHashMap<Integer, Pair<Boolean, HomeModel>> dataToView;
    private AdapterListener<HomeModel> listener;


    public HomeAdapter(List<HomeModel> mItems, AdapterListener<HomeModel> listener) {
        this.homeItemList = mItems;
        this.dataToView = new LinkedHashMap<>();
        this.listener = listener;
        setFirstOnly(false);
        initializeHomeData();
    }

    private void initializeHomeData() {
        dataToView.clear();

        int prevType = -1;
        int count = 0;
        for (HomeModel model : homeItemList) {
            boolean isHeader = false;
            if (prevType < model.getHomeType().value() || prevType == -1) isHeader = true;
            dataToView.put(count, new Pair<>(isHeader, model));

            prevType = model.getHomeType().value();
            count++;
        }
    }

    @NonNull
    @Override
    public HomeAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Pair<Boolean, HomeModel> pair = dataToView.get(position);
        holder.model = pair.second;

        String title = holder.model.getTitle();
        String description = holder.model.getDescription();

        Calendar currentDate = Calendar.getInstance();
        Calendar itemDate = Calendar.getInstance();
        itemDate.setTime(holder.model.getTime());

        String formattedDate = Interval.getFormattedDateDifference(itemDate, currentDate);

        holder.mHomeViewTypeTV.setText(holder.model.getHomeType().displayName());
        holder.mHomeViewHeaderLyt.setVisibility(pair.first ? View.VISIBLE : View.GONE);

        if (holder.model.TYPE.equals(HomeModel.NOTIFICATION_TYPE)) {
            holder.mNotificationTypeIcon.setImageResource(R.drawable.ic_notifications);
            holder.mNotificationTypeTV.setText(R.string.notification);
        } else if (holder.model.TYPE.equals(HomeModel.EVENT_TYPE)) {
            holder.mNotificationTypeIcon.setImageResource(R.drawable.ic_calendar);
            holder.mNotificationTypeTV.setText(R.string.calendar);
        }

        holder.mNotificationTitleTV.setText(title);
        holder.mNotificationDescTV.setText(Html.fromHtml(description));
        holder.mNotificationTimeTV.setText(formattedDate);

        holder.view.setOnClickListener(it -> {
            if (listener != null) {
                listener.onItemClicked(holder.model);
            }
        });

        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return dataToView.size();
    }

    private HomeModel getItem(final int position) {
        return dataToView.get(position).second;
    }

    public void updateItems(List<HomeModel> list) {
        this.homeItemList.clear();
        this.homeItemList = list;
        initializeHomeData();
        notifyDataSetChanged();
    }

    public List<HomeModel> getHomeItems() {
        return homeItemList;
    }

    @Override
    protected Animator[] getAnimators(View view) {
//        Scale from centre Animation
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.0f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.0f, 1.0f);
        return new ObjectAnimator[] { scaleX, scaleY };

//        SlideInLeft Animation
//        return new Animator[] {
//                ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0)
//        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView mNotificationTypeIcon;
        private final TextView mNotificationTypeTV;
        private final TextView mNotificationTitleTV;
        private final TextView mNotificationDescTV;
        private final TextView mNotificationTimeTV;
        private final LinearLayout mHomeViewHeaderLyt;
        private final TextView mHomeViewTypeTV;
        private HomeModel model;
        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            mHomeViewHeaderLyt = (LinearLayout) itemView.findViewById(R.id.homeViewHeaderNoti);
            mHomeViewTypeTV = (TextView) itemView.findViewById(R.id.homeViewTypeNoti);

            mNotificationTypeIcon = (ImageView) itemView.findViewById(R.id.notificationTypeIcon);
            mNotificationTypeTV = (TextView) itemView.findViewById(R.id.notificationTypeTV);

            mNotificationTitleTV = (TextView) itemView.findViewById(R.id.notificationTitleTV);
            mNotificationDescTV = (TextView) itemView.findViewById(R.id.notificationDescTV);
            mNotificationTimeTV = (TextView) itemView.findViewById(R.id.notificationTimeTV);
        }
    }
}