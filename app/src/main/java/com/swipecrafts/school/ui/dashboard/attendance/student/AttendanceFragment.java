package com.swipecrafts.school.ui.dashboard.attendance.student;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.library.calendar.MaterialCalendarView;
import com.swipecrafts.library.calendar.decorator.EventDecorator;
import com.swipecrafts.library.calendar.format.DateFormatTitleFormatter;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.AttendanceViewModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;


public class AttendanceFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Toolbar toolbar;
    private MaterialCalendarView calendarView;
    private DateFormatTitleFormatter titleFormatter;
    private AttendanceViewModel attendanceViewModel;

    private List<StudentAttendance> studentAttendanceData;
    private SwipeRefreshLayout swipeContainer;

    private @ColorInt
    int colorPresent = Color.GREEN;
    private @ColorInt
    int colorAbsent = Color.RED;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        attendanceViewModel = ViewModelProviders.of(this, viewModelFactory).get(AttendanceViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.parentAttendanceToolbar);
        calendarView = view.findViewById(R.id.studentAttendanceCalendar);
        swipeContainer = view.findViewById(R.id.attendanceSwipeRefreshLayout);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        colorPresent = this.getResources().getColor(R.color.colorActive);
        colorAbsent = this.getResources().getColor(R.color.colorHoliday);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshAttendance);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        refreshAttendance();
    }

    private void refreshAttendance() {
        attendanceViewModel.getMyAttendance().observe(this, response -> {
            if (response == null) return;

            if (response.isLoading()) {

            } else if (response.isSuccessful()) {
                swipeContainer.setRefreshing(false);
                populateAttendanceDataToCalendar(response.data);
            } else {
                swipeContainer.setRefreshing(false);
                String message = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateAttendanceDataToCalendar(List<StudentAttendance> studentAttendanceData) {
        this.studentAttendanceData = studentAttendanceData;
        calendarView.removeDecorators();
        List<Long> present = new ArrayList<>();
        List<Long> absent = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        for (StudentAttendance data : studentAttendanceData) {
            if (data.getStatus() == -1) continue;

            calendar.clear();
            calendar.clear(Calendar.YEAR);
            calendar.clear(Calendar.MONTH);
            calendar.clear(Calendar.DATE);

            LogUtils.errorLog("Attendance", data.getAttendanceDate().toString());
            calendar.setTime(data.getAttendanceDate());
            long hash = ((calendar.get(Calendar.YEAR) * 1000000) + ((calendar.get(Calendar.MONTH) + 1) * 1000) + (calendar.get(Calendar.DAY_OF_MONTH) * 10));

            if (data.getStatus() == 1) {
                present.add(hash);
            } else {
                absent.add(hash);
            }
        }
        calendarView.addDecorators(new EventDecorator(colorPresent, present));
        calendarView.addDecorators(new EventDecorator(colorAbsent, absent));
    }

}
