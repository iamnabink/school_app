package com.swipecrafts.school.ui.dashboard.livebus;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.swipecrafts.school.R;
import com.swipecrafts.library.views.CircleView;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveBus;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveLocationData;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class LiveBusListAdapter extends RecyclerView.Adapter<LiveBusListAdapter.ViewHolder> {

    private String schoolId;
    private List<LiveBus> mValues;
    private final AdapterListener<LiveBus> mListener;


    public LiveBusListAdapter(String schoolId, List<LiveBus> items, AdapterListener<LiveBus> listener) {
        this.schoolId = schoolId;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.livebus_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        LiveBus mItem = mValues.get(position);
        holder.mItem = mItem;
        holder.bind();

        String driverId = mItem.getDriverId();
        driverId = driverId == null ? "0" : driverId;
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(holder.mView.getContext().getResources().getString(R.string.live_bus_map_path, schoolId, driverId));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                LiveLocationData data = dataSnapshot.getValue(LiveLocationData.class);
                if (data != null) {
                    Calendar activeDate = Calendar.getInstance();
                    activeDate.setTimeInMillis(data.getAddedAt());
                    Calendar currentDate = Calendar.getInstance();

                    long elapsedMinute = TimeUnit.MILLISECONDS.toMinutes(currentDate.getTimeInMillis() - activeDate.getTimeInMillis());
                    boolean online = elapsedMinute < 5;
                    holder.setStatus(online);
                }else{
                    holder.setStatus(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                holder.setStatus(false);
            }
        });


        holder.mView.setOnClickListener(v -> {
            if (null != mListener) {
                mListener.onItemClicked(holder.mItem);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setBusItems(List<LiveBus> data) {
        this.mValues = data;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View mView;
        public TextView busNameTV;
        public TextView busNumberTV;
        public TextView busStatusTV;
        public CircleView busStatusView;
        public LiveBus mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            busNameTV = (TextView) view.findViewById(R.id.busNameTV);
            busNumberTV = (TextView) view.findViewById(R.id.busNumberTV);
            busStatusTV = (TextView) view.findViewById(R.id.busStatusTV);
            busStatusView = (CircleView) view.findViewById(R.id.busStatusView);
        }

        public void bind() {
            if (mItem == null) return;

            busNameTV.setText(mItem.getBusName());
            busNumberTV.setText(mItem.getBusNumber());
            setStatus(mItem.getStatus());
        }

        public void setStatus(boolean status) {
            this.mItem.setStatus(status);
            if (status) {
                busStatusView.setColor(mView.getContext().getResources().getColor(R.color.colorNonHoliday));
                busStatusTV.setText("Online");
            } else {
                busStatusView.setColor(mView.getContext().getResources().getColor(R.color.colorHoliday));
                busStatusTV.setText("Offline");
            }
        }
    }
}
