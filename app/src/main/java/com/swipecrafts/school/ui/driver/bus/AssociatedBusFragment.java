package com.swipecrafts.school.ui.driver.bus;

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

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Resource;
import com.swipecrafts.school.data.model.db.BusDriver;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.viewmodel.UserViewModel;

import java.util.List;

import javax.inject.Inject;


public class AssociatedBusFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;
    private Observer<Resource<List<BusDriver>>> busListObserver;

    private ErrorView errorView;
    private RecyclerView busRecyclerView;
    private Toolbar toolbar;
    private BusAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_associated_bus, container, false);
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        toolbar = view.findViewById(R.id.toolbar);
        errorView = view.findViewById(R.id.errorView);
        busRecyclerView = view.findViewById(R.id.busListRecyclerView);

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        busRecyclerView.setHasFixedSize(true);
        busRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        busRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new BusAdapter();
        busRecyclerView.setAdapter(mAdapter);


        busListObserver = response -> {
            if (response == null) return;

            if (response.isLoading()) {
                errorView.setViewType(ErrorView.PROGRESS);
                errorView.setVisibility(View.VISIBLE);
            } else if (response.isSuccessful()) {
                if (response.data == null || response.data.isEmpty()) {
                    errorView.setErrorTitle("No associated bus found!");
                    errorView.setErrorDescription("Please contact to the school.");
                    errorView.changeType(ErrorView.ERROR);
                    errorView.apply();
                    errorView.setVisibility(View.VISIBLE);
                } else {
                    mAdapter.setBuses(response.data);
                    errorView.setVisibility(View.GONE);
                }
            } else {
                errorView.setErrorTitle(response.message);
                errorView.changeType(ErrorView.ERROR_WITH_BUTTON);
                errorView.apply();
                errorView.setVisibility(View.VISIBLE);
            }
        };
        userViewModel.getBusDetails().observe(this, busListObserver);

        errorView.setOnRetryListener(v -> userViewModel.getBusDetails().observe(this, busListObserver));

        return view;
    }
}
