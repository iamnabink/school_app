package com.swipecrafts.school.ui.dc.subject;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.DCSubject;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dc.Video.VideoFragment;
import com.swipecrafts.school.ui.dc.chapter.ChapterFragment;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeedFragment;
import com.swipecrafts.school.viewmodel.SubjectViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class SubjectFragment extends BaseFragment {

    private static final String ARG_GRADE_ID = "ARG_GRADE_ID";
    private static final String ARG_GRADE_NAME = "ARG_GRADE_NAME";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private long gradeID;
    private String gradeName = "";
    private RecyclerView mRecyclerView;
    private SubjectViewAdapter mAdapter;
    private List<DCSubject> DCSubjectList;
    private boolean isCompleted = false;
    private SubjectViewModel viewModel;
    private FragmentTransaction fragmentTransaction;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;

    private LinearLayout errorLayout;

    public SubjectFragment() {
    }

    public static SubjectFragment newInstance(long gradeID, String gradeName) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_GRADE_ID, gradeID);
        args.putString(ARG_GRADE_NAME, gradeName);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gradeID = getArguments().getLong(ARG_GRADE_ID);
            gradeName = getArguments().getString(ARG_GRADE_NAME);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(SubjectViewModel.class);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_subject, container, false);
        findView(view);
        init();
        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.dcSubjectToolbar);
        toolbar.setTitle(gradeName);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.subjectSwipeToRefreshLayout);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.subjectRecyclerView);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshSubject);
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

        if (DCSubjectList == null) DCSubjectList = new ArrayList<>();
        mAdapter = new SubjectViewAdapter(DCSubjectList,
                model -> {
//                    VideoFragment videoFragment = new VideoFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.main_container,videoFragment);
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();

                    VideoFragment fragment = VideoFragment.newInstance(gradeID, model.getDcSubjectId(), model.getDcSubjectName());
                    replaceFragment(R.id.main_container, fragment, VideoFragment.class.getSimpleName(), "Subject");

//                    ChapterFragment fragment = ChapterFragment.newInstance(gradeID, model.getDcSubjectId(), model.getDcSubjectName());
//                    replaceFragment(R.id.main_container, fragment, ChapterFragment.class.getSimpleName(), "DCSubject");
                },
                (imageView, model) -> {
                    RequestOptions options = new RequestOptions()
                            .placeholder(R.drawable.progressbar)
                            .error(R.drawable.placeholder);

                    Glide.with(this)
                            .load(model.getRemoteSubjectIcon())
                            .apply(options)
                            .into(imageView);
                });
        mRecyclerView.setAdapter(mAdapter);

        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                int subjects = viewModel.getSubjectNumber(gradeID);

                Log.e("subjectSize", "localChecking" + subjects);

                if (subjects > 0) {
                    isCompleted = true;
                } else if (subjects <= 0) {
                    swipeContainer.post(() -> swipeContainer.setRefreshing(true));
                    refreshSubject();
                }

                return isCompleted;
            }
        }.execute();

        viewModel.getSubjectsByGrade(gradeID).observe(this, this::setUpRecyclerView);

    }

    private void setUpRecyclerView(List<DCSubject> DCSubjects) {
        if (DCSubjects == null) return;
        DCSubjectList = DCSubjects;

        if (mAdapter != null) mAdapter.updateSubjects(DCSubjects);
    }

    private void refreshSubject() {
        Log.e("refreshing", "subjects!!");
        viewModel.refreshSubjects(String.valueOf(gradeID)).observe(SubjectFragment.this, (result) -> {
            if (result == null) return;

            if (result.second) {
                mRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                isCompleted = true;
            } else {
                mRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(result.first);
                swipeContainer.setRefreshing(false);
                isCompleted = false;
            }
        });
    }
}