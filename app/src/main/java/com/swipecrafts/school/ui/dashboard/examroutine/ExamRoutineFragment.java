package com.swipecrafts.school.ui.dashboard.examroutine;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
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
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.examroutine.model.ExamRoutineRes;
import com.swipecrafts.school.viewmodel.RoutineViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class ExamRoutineFragment extends BaseFragment {

    @Inject
    public ViewModelProvider.Factory viewModelFactory;
    private RoutineViewModel routineViewModel;

    private RecyclerView routineRecyclerView;
    private ExamRoutineListAdapter mExamRoutineAdapter;

    private Toolbar toolbar;
    private SwipeRefreshLayout routineSwipeRefreshLyt;
    private LinearLayout errorLayout;
    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        //Set up and subscribe (observe) to the ViewModel
        routineViewModel = ViewModelProviders.of(this, viewModelFactory).get(RoutineViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exam_schedule, container, false);


        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.examRoutineToolbar);

        // Lookup the swipe container view
        routineSwipeRefreshLyt =(SwipeRefreshLayout) view.findViewById(R.id.examSwipeToRefreshLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        routineRecyclerView = (RecyclerView) view.findViewById(R.id.examRoutineRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        routineSwipeRefreshLyt.setOnRefreshListener(this::refreshRoutine);
        // Configure the refreshing colors
        routineSwipeRefreshLyt.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // Set the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        routineRecyclerView.setItemAnimator(new DefaultItemAnimator());
        routineRecyclerView.setLayoutManager(layoutManager);
        mExamRoutineAdapter = new ExamRoutineListAdapter(new ArrayList<>());
        routineRecyclerView.setAdapter(mExamRoutineAdapter);

        isFirstRefresh = true;
        routineSwipeRefreshLyt.post(() -> routineSwipeRefreshLyt.setRefreshing(true));
        refreshRoutine();
    }

    private void refreshRoutine() {
        routineViewModel.refreshExamRoutine().observe(this, data ->{
            if (data == null) return;
            if (data.status()){
                setUpRecyclerView(data.getData());
                routineSwipeRefreshLyt.setRefreshing(false);

                routineRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            } else {
                routineSwipeRefreshLyt.setRefreshing(false);

                if (!isFirstRefresh){
                    Toast.makeText(getContext(), data.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                routineRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(data.getMessage());
            }
        });

    }

    private void setUpRecyclerView(List<ExamRoutineRes> examRoutineResRoutineList) {
        if (examRoutineResRoutineList == null) return;

        Log.e("ExamRoutine", examRoutineResRoutineList.size()+" ");
        mExamRoutineAdapter.updateRoutineDetails(examRoutineResRoutineList);
    }
}
