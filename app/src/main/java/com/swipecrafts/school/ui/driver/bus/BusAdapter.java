package com.swipecrafts.school.ui.driver.bus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.BusDriver;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 1/1/2019.
 */
public class BusAdapter extends RecyclerView.Adapter<BusAdapter.ViewHolder> {
    private List<BusDriver> buses = new ArrayList<>();

    public BusAdapter() {
    }

    public void setBuses(List<BusDriver> buses) {
        this.buses = buses;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BusAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.associated_bus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BusAdapter.ViewHolder holder, int position) {
        holder.bindView(buses.get(position));
    }

    @Override
    public int getItemCount() {
        return buses.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView busName, busNumber;

        public ViewHolder(View itemView) {
            super(itemView);
            busName = itemView.findViewById(R.id.busNameTV);
            busNumber = itemView.findViewById(R.id.busNumberTV);
        }

        public void bindView(BusDriver bus) {
            if (bus == null) return;

            busName.setText(bus.getBusName());
            busNumber.setText(bus.getBusNo());
        }
    }
}
