package com.swipecrafts.school.ui.dashboard.teachercontact;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.TeacherViewModel;

import java.util.ArrayList;

import javax.inject.Inject;


public class TeacherContactFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private RecyclerView teacherRecycler;
    private TeacherRecyclerViewAdapter mAdapter;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;
    private TeacherViewModel teacherViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        teacherViewModel = ViewModelProviders.of(this, viewModelFactory).get(TeacherViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teacher, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.teachersToolbar);

        teacherRecycler = view.findViewById(R.id.teacherRecyclerView);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.teacherContactSwipeLyt);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> refreshTeachers(true));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        teacherRecycler.setLayoutManager(layoutManager);

        mAdapter = new TeacherRecyclerViewAdapter(new ArrayList<>(), teacher ->{ });
        teacherRecycler.setAdapter(mAdapter);

        refreshTeachers(false);
    }

    private void refreshTeachers(boolean fetchFromRemote) {
        teacherViewModel.loadTeachers(fetchFromRemote).observe(this, resource -> {
            if (resource == null) return;

            if (resource.status == Status.SUCCESS) {
                mAdapter.setItems(resource.data);

                swipeContainer.setRefreshing(false);
                teacherRecycler.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
            } else if (resource.status == Status.ERROR) {
                swipeContainer.setRefreshing(false);
                String errorMessage = (resource.code() == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : resource.message);

                if (resource.data != null && !resource.data.isEmpty()) {
                    mAdapter.setItems(resource.data);

                    swipeContainer.setRefreshing(false);
                    teacherRecycler.setVisibility(View.VISIBLE);
                    errorLayout.setVisibility(View.GONE);
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                teacherRecycler.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(errorMessage);
            }
        });
    }
}
