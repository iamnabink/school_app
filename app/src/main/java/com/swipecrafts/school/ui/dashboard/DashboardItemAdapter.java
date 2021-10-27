package com.swipecrafts.school.ui.dashboard;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.DashboardItem;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.utils.listener.ImageLoader;

import java.util.ArrayList;
import java.util.List;


public class DashboardItemAdapter extends RecyclerView.Adapter<DashboardItemAdapter.ViewHolder> {

    private List<DashboardItem> dashboardItems = new ArrayList<>();
    private final AdapterListener<DashboardItem> mListener;
    private final ImageLoader<DashboardItem> mImageLoader;

    public DashboardItemAdapter(List<DashboardItem> items, boolean isLoggedIn, ImageLoader<DashboardItem> imageLoader, AdapterListener<DashboardItem> listener) {
        showOnlyLoggedInData(items, isLoggedIn);
        this.mImageLoader = imageLoader;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        DashboardItem item = dashboardItems.get(position);

        holder.mItem = item;
        mImageLoader.loadImage(holder.mIconView, holder.mItem);
        holder.mContentView.setText(item.menuName);

        holder.mView.setOnClickListener(v -> {
            if (null != mListener) mListener.onItemClicked(holder.mItem);
        });
    }

    @Override
    public int getItemCount() {
        return dashboardItems.size();
    }

    public void showOnlyLoggedInData(List<DashboardItem> items, boolean isLoggedIn){
        this.dashboardItems.clear();
        for (DashboardItem item : items) {
            if (item.getId() == 24 && !isLoggedIn) continue;
            this.dashboardItems.add(item);
        }
    }

    public void addNewItem(DashboardItem item){
        this.dashboardItems.add(item);
        notifyItemChanged(this.dashboardItems.size()-1);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final View mView;
        final TextView mContentView;
        final ImageView mIconView;

        DashboardItem mItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mContentView = (TextView) view.findViewById(R.id.menuName);
            mIconView = (ImageView) view.findViewById(R.id.dashboardIcon);
        }
    }
}
