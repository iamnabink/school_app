package com.swipecrafts.school.ui.dashboard.busroute;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.BusRoute;

import java.util.List;


public class BusRouteAdapter extends RecyclerView.Adapter<BusRouteAdapter.ViewHolder> {

    private List<BusRoute> busRouteList;

    public BusRouteAdapter(List<BusRoute> items) {
        busRouteList = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.busroute_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        holder.model = busRouteList.get(position);
        holder.bindView();
    }

    @Override
    public int getItemCount() {
        return busRouteList.size();
    }

    public void updateRoutes(List<BusRoute> busRoutes) {
        this.busRouteList = busRoutes;
        notifyDataSetChanged();
    }

    public List<BusRoute> getBusRouteList() {
        return busRouteList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final TextView mStationName;

        public BusRoute model;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mStationName = (TextView) mView.findViewById(R.id.stationNameTV);
        }

        public void bindView(){
            mStationName.setText(model.getStationName());
        }
    }
}
