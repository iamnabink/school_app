package com.swipecrafts.school.ui.dashboard.busroute;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Bus;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/15/2018.
 */

public class BusListAdapter extends RecyclerView.Adapter<BusListAdapter.BusListViewHolder> {

    private final Context mContext;
    private List<Bus> busList;
    private AdapterListener<Bus> mListener;
    private boolean isItemSelected = false;
    private int selectedPosition;
    private View previousView;

    public BusListAdapter(Context mContext, List<Bus> busList, AdapterListener<Bus> mListener) {
        this.mContext = mContext;
        this.busList = busList;
        this.mListener = mListener;
    }

    @Override
    public BusListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bus_item, parent, false);

        return new BusListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BusListViewHolder holder, int position) {
        holder.model = busList.get(position);
        holder.bindView();

        holder.view.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.model);
            }

            if (holder.view != previousView) {
                holder.view.setBackgroundColor(mContext.getResources().getColor(R.color.colorPrimary));
                holder.busDetailsTV.setTextColor(Color.WHITE);
            }

            // remove previous item selection
            if (previousView != null) {
                previousView.setBackgroundColor(Color.WHITE);
                TextView busDetailsTV = (TextView) previousView.findViewById(R.id.busDetailsTV);
                busDetailsTV.setTextColor(Color.parseColor("#FF1F2D3D"));

            }

            if (holder.view == previousView) previousView = null;
            else previousView = holder.view;

        });
    }

    @Override
    public int getItemCount() {
        return busList.size();
    }

    public void updateBusList(List<Bus> busList) {
        this.busList = busList;
        notifyDataSetChanged();
    }

    class BusListViewHolder extends RecyclerView.ViewHolder {
        Bus model;
        View view;
        private TextView busDetailsTV;

        public BusListViewHolder(View itemView) {
            super(itemView);
            view = itemView;

            busDetailsTV = (TextView) view.findViewById(R.id.busDetailsTV);
        }

        public void bindView() {
            busDetailsTV.setText(String.format("%s ( %s )", model.getBusNo(), model.getBusName()));
        }
    }

}
