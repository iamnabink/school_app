package com.swipecrafts.school.ui.dashboard.schoolschedule;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.SchoolSchedule;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.viewmodel.SchoolScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ScheduleFragment extends BaseFragment implements AdapterListener<SchoolSchedule> {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SchoolScheduleViewModel scheduleViewModel;

    private RecyclerView recyclerView;
    private ScheduleListAdapter mAdapter;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;
    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

        //Set up and subscribe (observe) to the ViewModel
        scheduleViewModel = ViewModelProviders.of(this, viewModelFactory).get(SchoolScheduleViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.scheduleToolbar);

        recyclerView = view.findViewById(R.id.scheduleRecycler);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.schoolScheduleSwipeToRefreshLayout);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshSchoolSchedule);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        mAdapter = new ScheduleListAdapter(new ArrayList<>(), this);
        recyclerView.setAdapter(mAdapter);

        scheduleViewModel.countSchoolSchedule().observe(this, count ->{
            if (count == null) return;

            if (count <= 0){
                isFirstRefresh = true;
                swipeContainer.post(()-> swipeContainer.setRefreshing(true));
                refreshSchoolSchedule();
            }
        });

        scheduleViewModel.getSchoolSchedule().observe(this, this::setRecyclerViewData);
    }

    private void refreshSchoolSchedule() {
        scheduleViewModel.refersSchoolSchedule().observe(this, result -> {
            if (result == null) return;

            LogUtils.errorLog("SchoolSchedule", "SchoolSchedule refresh result "+ result.second +" message "+ result.first);
            if (result.second){
                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                isFirstRefresh = false;
            }else {
                swipeContainer.setRefreshing(false);
                if (!isFirstRefresh){
                    Toast.makeText(getContext(), result.first, Toast.LENGTH_SHORT).show();
                    return;
                }
                recyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(result.first);
            }
        });
    }

    public void setRecyclerViewData(List<SchoolSchedule> list) {
        if (list == null) return;

        Log.e("scheldule", list.size() + "");
        mAdapter.updateScheduleItems(list);
    }

    @Override
    public void onItemClicked(SchoolSchedule item) {

    }
}
