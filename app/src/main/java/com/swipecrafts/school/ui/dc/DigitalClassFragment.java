package com.swipecrafts.school.ui.dc;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.Grade;
import com.swipecrafts.school.data.model.db.User;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dc.subject.SubjectFragment;
import com.swipecrafts.school.ui.login.LoginActivity;
import com.swipecrafts.school.ui.login.LoginDialogFragment;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.viewmodel.GradeViewModel;
import com.swipecrafts.library.views.ErrorView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.swipecrafts.school.ui.login.LoginDialogFragment.ARG_LOGIN_TYPE;


public class DigitalClassFragment extends BaseFragment implements AdapterListener<Grade> {


    private static final int LOGIN_RESPONSE_CODE = 1;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private int mColumnCount = 2;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private GradeViewAdapter mAdapater;
    private List<Grade> gradeList;
    private GradeViewModel viewModel;

    private SwipeRefreshLayout swipeContainer;
    private ErrorView errorLayout;

    private LoginDialogFragment loginDialog;
    private User activeUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(GradeViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dc, container, false);


        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        // Lookup the swipe container view
        swipeContainer = view.findViewById(R.id.dcSwipeToRefreshLayout);
        errorLayout = view.findViewById(R.id.errorLayout);

        mRecyclerView = view.findViewById(R.id.gradeListRecycler);
    }

    private void init() {

        errorLayout.setOnRetryListener(v -> {
            // show the login dialog...
//            if (loginDialog == null) {
//                this.loginDialog = LoginDialogFragment.newInstance(UserType.GENERAL);
//            } else {
//                loginDialog.dismiss();
//            }
//            loginDialog.setTargetFragment(this, 1);
//            loginDialog.show(getFragmentManager(), "General Login");

            Intent intent = new Intent(getContext(), LoginActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString(ARG_LOGIN_TYPE, UserType.GENERAL.getValue());
            intent.putExtras(bundle);
            startActivityForResult(intent, LOGIN_RESPONSE_CODE);
        });

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> loadGrades(true));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        mLayoutManager = new GridLayoutManager(getContext(), mColumnCount);
        mRecyclerView.setLayoutManager(mLayoutManager);

        if (gradeList == null) gradeList = new ArrayList<>();
        mAdapater = new GradeViewAdapter(
                gradeList,
                (imageView, model) -> {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .circleCrop()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.placeholder);

                    Glide.with(this)
                            .load(model.getRemoteDcGradeImg())
                            .apply(options)
                            .into(imageView);
                },
                this);
        mRecyclerView.setAdapter(mAdapater);

        viewModel.getActiveUser().observe(this, activeUser -> {
            DigitalClassFragment.this.activeUser = activeUser;
            if (activeUser == null) {
                errorLayout.setViewType(ErrorView.ERROR_WITH_BUTTON).apply();
                swipeContainer.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            } else {
                swipeContainer.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.post(() -> swipeContainer.setRefreshing(true));
                loadGrades(false);
            }
        });
    }

    private void loadGrades(boolean loadFromRemote) {
        viewModel.getGrades(false).observe(this, response -> {
            if (response == null) return;

            if (response.status == Status.SUCCESS) {
                swipeContainer.setRefreshing(false);
                mAdapater.setValues(response.data);
            } else if (response.status == Status.ERROR) {
                swipeContainer.setRefreshing(false);
                // show error message
            }
        });
    }

    @Override
    public void onItemClicked(Grade item) {
        SubjectFragment fragment = SubjectFragment.newInstance(item.getDcGradeId(), item.getDcGradeName());
        replaceFragment(R.id.main_container, fragment, DigitalClassFragment.class.getSimpleName(), "DigitalClass");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOGIN_RESPONSE_CODE) {
            if (resultCode == 0) {
                if (activeUser == null)
                    errorLayout.setViewType(ErrorView.ERROR_WITH_BUTTON).apply();
            }
        }
    }
}
