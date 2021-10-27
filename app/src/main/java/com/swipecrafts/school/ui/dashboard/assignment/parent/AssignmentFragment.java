package com.swipecrafts.school.ui.dashboard.assignment.parent;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.assignment.model.Assignment;
import com.swipecrafts.school.ui.dashboard.gallery.FullScreenImageGalleryActivity;
import com.swipecrafts.library.views.OnlineDocumentViewer;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.AssignmentViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class AssignmentFragment extends BaseFragment {


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private RecyclerView mRecyclerView;
    private AssignmentRecyclerAdapter mAdapter;
    private AssignmentViewModel viewModel;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;
    private List<Assignment> mAssignmentLists;


    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AssignmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_assignment, container, false);


        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.studentAttendanceToolbar);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.studentAttendanceSwipeToRefreshLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.studentAttendanceRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshAssignmentList);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        // Set the adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(layoutManager);

        if (mAssignmentLists == null) mAssignmentLists = new ArrayList<>();
        mAdapter = new AssignmentRecyclerAdapter(mAssignmentLists, this::openSpecificApp);
        mRecyclerView.setAdapter(mAdapter);

        swipeContainer.post(() -> swipeContainer.setRefreshing(true));
        isFirstRefresh = true;
        refreshAssignmentList();
    }

    private void openSpecificApp(Assignment assignment) {

        if (assignment.getFileType().equalsIgnoreCase("pdf") ||
                assignment.getFileType().equalsIgnoreCase("doc") ||
                assignment.getFileType().equalsIgnoreCase("docx")) {


            Intent i = new Intent(getActivity(), OnlineDocumentViewer.class);
            Bundle bundle = new Bundle();
            bundle.putString(OnlineDocumentViewer.ARG_FILE_URL, assignment.getFileUrl());
            bundle.putString(OnlineDocumentViewer.ARG_FILE_NAME, assignment.getTitle());
            i.putExtras(bundle);
            startActivity(i);

        } else if (assignment.getFileType().equalsIgnoreCase("jpg") ||
                assignment.getFileType().equalsIgnoreCase("png") ||
                assignment.getFileType().equalsIgnoreCase("jpeg") ||
                assignment.getFileType().equalsIgnoreCase("jpg")) {

            ArrayList<String> imgList = new ArrayList<String>();
            imgList.add(assignment.getFileUrl());

            Intent i = new Intent(getActivity(), FullScreenImageGalleryActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(FullScreenImageGalleryActivity.KEY_IMAGES, imgList);
            bundle.putInt(FullScreenImageGalleryActivity.KEY_POSITION, 0);
            i.putExtras(bundle);
            startActivity(i);

        } else {
            Toast.makeText(getContext(), "Sorry this file type is not supported!!", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshAssignmentList() {
        viewModel.loadAssignments().observe(this, response ->{
            if (response == null) return;

            if (response.isLoading()){

            }else if (response.isSuccessful()){
                mRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);

                mAdapter.updateAssignmentList(response.data);
            }else{
                swipeContainer.setRefreshing(false);
                String errorMessage = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);

                if (!isFirstRefresh) {
                    Toast.makeText(getContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    return;
                }
                mRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(errorMessage);
            }
        });
    }
}
