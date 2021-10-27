package com.swipecrafts.school.ui.calendar;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.library.calendar.MaterialCalendarView;
import com.swipecrafts.library.calendar.core.BaseCalendar;
import com.swipecrafts.library.calendar.core.CalendarDay;
import com.swipecrafts.library.calendar.decorator.EventDecorator;
import com.swipecrafts.library.calendar.decorator.TodayDecorator;
import com.swipecrafts.library.calendar.format.DateFormatTitleFormatter;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.viewmodel.EventViewModel;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


public class CalendarFragment extends BaseFragment implements AdapterListener<Event> {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    DateFormatTitleFormatter titleFormatter;

    private EventViewModel eventViewModel;

    private Toolbar toolbar;
    private ActionBar actionBar;
    private MaterialCalendarView calendarView;
    private RecyclerView eventRecycler;
    private EventRecyclerAdapter eventsAdapter;

    private LinearLayout errorLayout;
    private AppBarLayout appbar;
    private int eventColor;
    private EventDecorator eventDecorator;
    private int todayColor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

        //Set up and subscribe (observe) to the ViewModel
        eventViewModel = ViewModelProviders.of(this, viewModelFactory).get(EventViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_calendar, container, false);

        findView(mainView);
        init();

        initActivityTransitions();

        return mainView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        calendarView.setCurrentDate(CalendarDay.today());
        eventViewModel.setTime(calendarView.getCurrentDate(), EventViewModel.MONTH);
        eventViewModel.getEvents().observe(this, this::setUpEventAdapter);

        eventViewModel.getEventsDate().observe(this, dates -> {
            if (dates == null || dates.isEmpty()) return;
            calendarView.removeDecorators();
            calendarView.addDecorators(new TodayDecorator(todayColor), new EventDecorator(eventColor, eventDate(dates)));
        });
    }

    private void findView(View mainView) {
        appbar = mainView.findViewById(R.id.app_bar_calendar);

        toolbar = mainView.findViewById(R.id.calendarToolbar);

        calendarView = mainView.findViewById(R.id.customCalendar);
        eventRecycler = mainView.findViewById(R.id.events_recycler);
        errorLayout = (LinearLayout) mainView.findViewById(R.id.errorLayout);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        titleFormatter = new DateFormatTitleFormatter();
        toolbar.setTitle(titleFormatter.format(calendarView.getCurrentDate()));

        eventColor = getResources().getColor(R.color.componentPrimaryColor);
        todayColor = getResources().getColor(R.color.colorPrimary);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        eventRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(eventRecycler.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        eventRecycler.addItemDecoration(itemDecoration);
        eventRecycler.setItemAnimator(new DefaultItemAnimator());

        eventsAdapter = new EventRecyclerAdapter(new ArrayList<>(), this, calendarView.getCalendarType());
        eventRecycler.setAdapter(eventsAdapter);

        calendarView.setOnDateChangedListener((widget, date, selected) -> eventViewModel.setTime((CalendarDay) date.clone(), EventViewModel.DAY));

        calendarView.setOnMonthChangedListener((widget, date) -> {
            toolbar.setTitle(titleFormatter.format(date));
            eventViewModel.setTime((CalendarDay) date.clone(), EventViewModel.MONTH);
        });
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getActivity().getWindow().setEnterTransition(transition);
            getActivity().getWindow().setReturnTransition(transition);
        }
    }

    public void setUpEventAdapter(List<Event> events) {
        if (events == null || events.isEmpty()) {
            eventRecycler.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            return;
        }

        eventRecycler.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        eventsAdapter.setEvents(events, BaseCalendar.BS);
    }



    public List<Long> eventDate(List<Date> events) {
        List<Long> dates = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        for (Date date : events) {
            calendar.clear();
            calendar.clear(Calendar.YEAR);
            calendar.clear(Calendar.MONTH);
            calendar.clear(Calendar.DATE);

            calendar.setTime(date);
            long hash = ((calendar.get(Calendar.YEAR) * 1000000) + ((calendar.get(Calendar.MONTH) + 1) * 1000) + (calendar.get(Calendar.DAY_OF_MONTH) * 10));
            dates.add(hash);
        }
        return dates;
    }

    @Override
    public void onItemClicked(Event item) {

    }
}
