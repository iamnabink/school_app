package com.swipecrafts.school.ui.dashboard.busroute;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.BusRoute;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.busroute.map.BusRouteMapsActivity;
import com.swipecrafts.school.viewmodel.BusRouteViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class BusRouteFragment extends BaseFragment {


    private static final String ARG_BUS_NAME = "bus_name";
    private static final String ARG_BUS_ID = "bus_id";
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private BusRouteViewModel busRouteViewModel;

    private Toolbar toolbar;
    private SwipeRefreshLayout busRouteSwipeRefresh;

    private RecyclerView mBusRouteRecyclerView;
    private RelativeLayout busRouteDetailsLLyt;
    private Button viewInMapBtn;

    private ErrorView errorLayout;

    private String currentBusName;
    private long currentBusId = -1;
    private boolean isFirstRefresh = false;

    private BusRouteAdapter mBusRouteAdapter;


    public static BusRouteFragment newInstance(String busName, long busId) {

        Bundle args = new Bundle();
        args.putString(ARG_BUS_NAME, busName);
        args.putLong(ARG_BUS_ID, busId);

        BusRouteFragment fragment = new BusRouteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            currentBusId = getArguments().getLong(ARG_BUS_ID);
            currentBusName = getArguments().getString(ARG_BUS_NAME);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        busRouteViewModel = ViewModelProviders.of(this, viewModelFactory).get(BusRouteViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_busroute, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.busRouteToolbar);

        mBusRouteRecyclerView = view.findViewById(R.id.busRouteRecyclerView);

        busRouteDetailsLLyt = view.findViewById(R.id.busRouteDetailsLLyt);
        busRouteSwipeRefresh = view.findViewById(R.id.busRouteSwipeRefresh);

        viewInMapBtn = view.findViewById(R.id.viewInMapBtn);

        // Lookup the swipe container view
        errorLayout = view.findViewById(R.id.errorLayout);

    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(currentBusName);
        }


        // Setup refresh listener which triggers new data loading
        busRouteSwipeRefresh.setOnRefreshListener(this::refreshBusRoutes);
        // Configure the refreshing colors
        busRouteSwipeRefresh.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // for busRoute
        LinearLayoutManager lytManager = new LinearLayoutManager(getContext());
        mBusRouteRecyclerView.setLayoutManager(lytManager);
        DividerItemDecoration itemDec = new DividerItemDecoration(mBusRouteRecyclerView.getContext(), lytManager.getOrientation());
        itemDec.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mBusRouteRecyclerView.addItemDecoration(itemDec);
        mBusRouteRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mBusRouteAdapter = new BusRouteAdapter(new ArrayList<>());
        mBusRouteRecyclerView.setAdapter(mBusRouteAdapter);

        viewInMapBtn.setVisibility(View.GONE);
        viewInMapBtn.setOnClickListener((view) -> {
            if (currentBusId == -1) return;
            Intent intent = new Intent(getActivity(), BusRouteMapsActivity.class);
            Bundle bundle = new Bundle();
            bundle.putLong(BusRouteMapsActivity.BUS_ID_KEY, currentBusId);
            intent.putExtras(bundle);
            startActivity(intent);
        });

        busRouteViewModel.getBusRoutes(currentBusId).observe(this, this::setBusRouteRecyclerView);
        refreshBusRoutes();
    }

    private void refreshBusRoutes() {
        busRouteSwipeRefresh.setRefreshing(false);
        busRouteViewModel.refreshBusRouteList(currentBusId).observe(this, result -> {
            if (result == null) return;

            if (!result.second) {
                busRouteSwipeRefresh.setRefreshing(false);
                if (!mBusRouteAdapter.getBusRouteList().isEmpty()) {
                    Toast.makeText(getContext(), result.first, Toast.LENGTH_SHORT).show();
                    return;
                }
                errorLayout.setErrorDescription(result.first);
                errorLayout.changeType(ErrorView.ERROR);
                errorLayout.apply();
                busRouteDetailsLLyt.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }else{
                errorLayout.changeType(ErrorView.PROGRESS);
                errorLayout.apply();
                busRouteDetailsLLyt.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setBusRouteRecyclerView(List<BusRoute> busRoutes) {
        if (busRoutes == null || busRoutes.isEmpty()){
            viewInMapBtn.setVisibility(View.INVISIBLE);
            busRouteDetailsLLyt.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            errorLayout.setErrorTitle("No bus route available.");
            errorLayout.changeType(ErrorView.ERROR);
            errorLayout.apply();
            return;
        }
        busRouteDetailsLLyt.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);
        viewInMapBtn.setVisibility(View.VISIBLE);
        mBusRouteAdapter.updateRoutes(busRoutes);
    }

}
