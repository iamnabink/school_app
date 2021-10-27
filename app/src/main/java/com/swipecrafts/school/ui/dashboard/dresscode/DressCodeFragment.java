package com.swipecrafts.school.ui.dashboard.dresscode;

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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.DressCode;
import com.swipecrafts.school.data.model.db.Gender;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.viewmodel.DressCodeViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

public class DressCodeFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private DressCodeViewModel dressCodeViewModel;

    private RecyclerView mDressCodeRecycler;
    private DressCodeListAdapter mDressCodeAdapter;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;
    private boolean isFirstRefresh = false;

    private List<DressCode> dressCodeLists;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

        //Set up and subscribe (observe) to the ViewModel
        dressCodeViewModel = ViewModelProviders.of(this, viewModelFactory).get(DressCodeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dresscode, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.dressCodeToolBar);

        mDressCodeRecycler = view.findViewById(R.id.dressCodeRecyclerView);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.dressCodeSwipeToRefreshLayout);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshDressCode);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mDressCodeRecycler.setLayoutManager(layoutManager);

        if (dressCodeLists == null) dressCodeLists = new ArrayList<>();
        mDressCodeAdapter = new DressCodeListAdapter(dressCodeLists, (image, model) -> {

            String imgUrl = model == null ? "" : model.getCategoryImage();
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.progressbar)
                    .error(R.drawable.placeholder);

            Glide.with(this)
                    .load(imgUrl)
                    .apply(options)
                    .into(image);
        });

        mDressCodeRecycler.setAdapter(mDressCodeAdapter);

        dressCodeViewModel.countDressCode().observe(this, (count) -> {
            if (count == null) return;

            LogUtils.errorLog("DressCode", "local dress code size "+ count);
            if (count <= 0){
                isFirstRefresh = true;
                swipeContainer.post( ()-> swipeContainer.setRefreshing(true));
                refreshDressCode();
            }else {
                LogUtils.errorLog("DressCode", "local dress code here "+ count);
            }
        });

        dressCodeViewModel.getAllDressCode().observe(this, dressCodes ->{
            if (dressCodes == null) return;

            LogUtils.errorLog("Dresscode", dressCodes.toString());
            LinkedHashMap<String, HashMap<Long, Gender>> allData = new LinkedHashMap<>();
            LinkedList<DressCode> finalGenderList = new LinkedList<>();

            for (Gender gender : dressCodes) {
                if (allData.containsKey(gender.getDayName())) {
                    HashMap<Long, Gender> genderMap = allData.get(gender.getDayName());
                    genderMap.put(gender.getCategoryId(), gender);
                } else {
                    HashMap<Long, Gender> genderMap = new HashMap<>();
                    genderMap.put(gender.getCategoryId(), gender);
                    allData.put(gender.getDayName(), genderMap);
                }
            }

            for (String key : allData.keySet()) {
                HashMap<Long, Gender> data = allData.get(key);
                ArrayList<Gender> genderList = new ArrayList<>();
                for (long k : data.keySet()) {
                    genderList.add(data.get(k));
                }
                finalGenderList.add(new DressCode(key, genderList));
            }

            setRecycleViewData(finalGenderList);
        });
//        dressCodeViewModel.getDressCodeList().observe(this, this::setRecycleViewData);
    }

    private void refreshDressCode() {
        dressCodeViewModel.refreshDressCode().observe(this, (result) -> {
            if (result == null) return;

            LogUtils.errorLog("DressCode", "dress code refresh result "+ result.second +" message "+ result.first);
            if (result.second){
                swipeContainer.setRefreshing(false);
                mDressCodeRecycler.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                isFirstRefresh = false;
            }else {
                swipeContainer.setRefreshing(false);
                if (!isFirstRefresh){
                    Toast.makeText(getContext(), result.first, Toast.LENGTH_SHORT).show();
                    return;
                }
                mDressCodeRecycler.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(result.first);
            }
        });
    }

    private void setRecycleViewData(List<DressCode> dressCodes){
        if (dressCodes == null) return;
        this.dressCodeLists = dressCodes;
        LogUtils.errorLog("DressCode", "dress code data size "+ dressCodes.size());
        mDressCodeAdapter.updateDressCode(dressCodes);
    }
}
