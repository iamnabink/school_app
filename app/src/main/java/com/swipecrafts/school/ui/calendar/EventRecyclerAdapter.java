package com.swipecrafts.school.ui.calendar;

import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.library.calendar.core.BSCalendar;
import com.swipecrafts.library.calendar.core.BaseCalendar;
import com.swipecrafts.library.calendar.core.CalendarType;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.listener.AdapterListener;

import java.util.List;

import static com.swipecrafts.library.calendar.core.BaseCalendar.AD;
import static com.swipecrafts.library.calendar.core.BaseCalendar.BS;


/**
 * Created by Madhusudan Sapkota on 2/27/2018.
 */

public class EventRecyclerAdapter extends RecyclerView.Adapter<EventRecyclerAdapter.ViewHolder> {
    private final AdapterListener<Event> listener;
    private List<Event> eventList;
    private int calendarType;

    public EventRecyclerAdapter(List<Event> eventList, AdapterListener<Event> listener, @CalendarType int calendarType) {
        this.eventList = eventList;
        this.listener = listener;
        this.calendarType = calendarType;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.events_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.bindView(eventList.get(position));
        holder.mainView.setOnClickListener(v -> listener.onItemClicked(eventList.get(holder.getAdapterPosition())));

    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public void setEvents(List<Event> events, @CalendarType int type) {
        this.eventList.clear();
        this.calendarType = type;
        this.eventList = events;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        View mainView;

        private TextView dayView;
        private TextView weekDay;
        private TextView details;
        private TextView daysLeft;


        ViewHolder(View itemView) {
            super(itemView);
            this.mainView = itemView;

            dayView = (TextView) itemView.findViewById(R.id.dayTV);
            weekDay = (TextView) itemView.findViewById(R.id.dayOfWeekNameTV);
            details = (TextView) itemView.findViewById(R.id.eventDetailsTV);
            daysLeft = (TextView) itemView.findViewById(R.id.dayLeftTV);
        }

        void bindView(Event event) {

            String eventDescString = event.getDescription();
            BaseCalendar calendar = BaseCalendar.from(event.getTime(), calendarType);
            String dayString = calendarType == BS ? BSCalendar.toNepali(calendar.getDay()) : calendar.getDay() + "";
            String weekDayString = calendar.daysOfWeeksShort()[calendar.getDayOfWeek()];

            BaseCalendar to = BaseCalendar.getInstance(calendarType);
            int daysDifference = BaseCalendar.getDaysDifference(to, calendar);

            dayView.setText(dayString);
            weekDay.setText(weekDayString);
            details.setText(Html.fromHtml(eventDescString).toString().trim());
            daysLeft.setText(getTimeToShow(daysDifference));
        }

        private String getTimeToShow(int left) {
            if (left == 0)
                return (calendarType == AD ? Constants.ENGLISH_TODAY : Constants.NEPALI_TODAY);
            else if (left < 0)
                return (calendarType == AD ? Math.abs(left) + Constants.ENGLISH_DAYS_AGO : BSCalendar.toNepali(Math.abs(left)) + Constants.NEPALI_DAYS_AGO);
            else
                return (calendarType == AD ? left + Constants.ENGLISH_DAYS_REMAINING : BSCalendar.toNepali(left) + Constants.NEPALI_DAYS_REMAINING);
        }

    }
}
