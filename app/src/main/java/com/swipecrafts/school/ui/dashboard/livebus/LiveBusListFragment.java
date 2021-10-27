package com.swipecrafts.school.ui.dashboard.livebus;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.api.ApiResponse;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.livebus.livemap.LiveBusFragment;
import com.swipecrafts.school.ui.dashboard.livebus.model.LiveBus;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class LiveBusListFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private LiveBusListAdapter mAdapter;
    private ErrorView errorView;
    private Observer<ApiResponse<List<LiveBus>>> observer = response -> {
        if (response == null || response.isLoading()) return;

        if (response.data == null || response.data.isEmpty()) {
            errorView.setErrorTitle(getString(R.string.no_data_message));
            errorView.setViewType(ErrorView.ERROR_WITH_BUTTON);
            errorView.apply();
        } else if (response.isSuccessful()) {
            errorView.setVisibility(View.GONE);
            mAdapter.setBusItems(response.data);
        } else {
            errorView.setViewType(ErrorView.ERROR_WITH_BUTTON);
            if (Utils.isOnline(getContext())) {
                errorView.setErrorTitle(response.message);
            } else {
                errorView.setErrorTitle(getString(R.string.no_internet_message));
            }
            errorView.apply();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        userViewModel.getLiveMapBusList().observe(this, observer);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livebus, container, false);

        findView(view);
        init();

        return view;
    }

    private void init() {
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        String schoolId = userViewModel.getSchoolId();
        mAdapter = new LiveBusListAdapter(schoolId, new ArrayList<>(), item -> {
            if (item.getStatus()) {
                LiveBusFragment fragment = LiveBusFragment.newInstance(item.getBusId(), item.getDriverId(), item.getBusNumber());
                replaceFragment(R.id.main_container, fragment, fragment.getClass().getSimpleName(), fragment.getClass().getSimpleName());
            } else {
                Toast.makeText(getContext(), "Bus is not gone to live yet!", Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(mAdapter);
    }

    private void findView(View view) {

        toolbar = view.findViewById(R.id.liveBusToolbar);
        recyclerView = view.findViewById(R.id.liveBusListView);

        errorView = (ErrorView) view.findViewById(R.id.errorView);
        errorView.setOnRetryListener(v -> {
            errorView.setViewType(ErrorView.PROGRESS);
            userViewModel.getLiveMapBusList().observe(LiveBusListFragment.this, observer);
        });


        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

    }
}
