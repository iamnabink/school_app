package com.swipecrafts.school.ui.dashboard.busroute;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.viewmodel.BusRouteViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 7/7/2019.
 */
public class BusListFragment extends BaseFragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private BusRouteViewModel busRouteViewModel;

    private BusListAdapter mBusListAdapter;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView mBusListRecyclerView;
    private ErrorView errorLayout;
    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        busRouteViewModel = ViewModelProviders.of(this, viewModelFactory).get(BusRouteViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buslist, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.busListToolbar);

        mBusListRecyclerView = view.findViewById(R.id.busListRecyclerView);
        swipeRefreshLayout = view.findViewById(R.id.busDetailsSwipeToRefreshLayout);
        errorLayout = view.findViewById(R.id.errorView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener(this::refreshBusList);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // for busList
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mBusListRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mBusListRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mBusListRecyclerView.addItemDecoration(itemDecoration);
        mBusListRecyclerView.setItemAnimator(new DefaultItemAnimator());


        busRouteViewModel.countBus().observe(this, count -> {
            if (count == null) return;

            if (count <= 0) {
                isFirstRefresh = true;
                swipeRefreshLayout.post(() -> swipeRefreshLayout.setRefreshing(true));
                refreshBusList();
            }
        });

        mBusListAdapter = new BusListAdapter(getContext(), new ArrayList<>(), (model) -> {
            Log.e("clicked", model.getBusId() + "");

            getFragmentManager().beginTransaction()
                    .replace(R.id.main_container, BusRouteFragment.newInstance(model.getBusName(), model.getBusId()))
                    .addToBackStack(BusRouteFragment.class.getSimpleName())
                    .commit();
        });
        mBusListRecyclerView.setAdapter(mBusListAdapter);

        busRouteViewModel.getBusList().observe(this, (busList) -> {
            if (busList == null) {
                return;
            }
           mBusListAdapter.updateBusList(busList);
        });
        refreshBusList();
    }

    private void refreshBusList() {
        busRouteViewModel.refreshBusList().observe(this, result -> {
            if (result == null) return;

            if (result.second) {
                errorLayout.setVisibility(View.GONE);
                mBusListRecyclerView.setVisibility(View.VISIBLE);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                if (!isFirstRefresh) {
                    Toast.makeText(getContext(), result.first, Toast.LENGTH_SHORT).show();
                    return;
                }else{

                }
                errorLayout.setVisibility(View.VISIBLE);
                errorLayout.changeType(ErrorView.ERROR);
                errorLayout.setErrorTitle(result.first);
                errorLayout.apply();
                mBusListRecyclerView.setVisibility(View.GONE);
            }
        });
    }
}
