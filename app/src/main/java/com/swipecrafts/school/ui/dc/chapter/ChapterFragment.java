package com.swipecrafts.school.ui.dc.chapter;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.AsyncTask;
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

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.Chapter;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeedFragment;
import com.swipecrafts.school.viewmodel.ChapterViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChapterFragment extends BaseFragment{

    private static final String ARG_GRADE_ID = "ARG_GRADE_ID";
    private static final String ARG_SUBJECT_ID = "ARG_SUBJECT_ID";
    private static final String ARG_SUBJECT_NAME = "ARG_SUBJECT_NAME";

    private long gradeId;
    private long subjectID;
    private String subjectName = "";

    private RecyclerView mRecyclerView;
    private ChapterViewAdapter mAdapter;
    private List<Chapter> chapterList;
    private boolean isCompleted = false;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private ChapterViewModel viewModel;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;

    private LinearLayout errorLayout;

    public ChapterFragment() {
    }


    public static ChapterFragment newInstance(long gradeId, long subjectID, String subjectName) {
        ChapterFragment fragment = new ChapterFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_GRADE_ID, gradeId);
        args.putLong(ARG_SUBJECT_ID, subjectID);
        args.putString(ARG_SUBJECT_NAME, subjectName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gradeId = getArguments().getLong(ARG_GRADE_ID);
            subjectID = getArguments().getLong(ARG_SUBJECT_ID);
            subjectName = getArguments().getString(ARG_SUBJECT_NAME);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ChapterViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chapter, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.dcChapterToolbar);
        toolbar.setTitle(subjectName);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.chapterSwipeToRefreshLayout);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.chapterRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshChapter);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration itemDecoration = new DividerItemDecoration(mRecyclerView.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(layoutManager);
        if (chapterList == null) chapterList = new ArrayList<>();
        mAdapter = new ChapterViewAdapter(chapterList, chapter ->{
            ContentFeedFragment fragment = ContentFeedFragment.newInstance(gradeId, subjectID, chapter.getDcChapterId(), chapter.getDcChapterTitle());
            replaceFragment(R.id.main_container, fragment, ContentFeedFragment.class.getSimpleName(), "Chapter");
        });
        mRecyclerView.setAdapter(mAdapter);

        new AsyncTask<Void, Integer, Boolean>() {
            @Override
            protected Boolean doInBackground(Void... voids) {
                int chapterNumber = viewModel.getChapterNumber(subjectID);

                if (chapterNumber > 0) {
                    isCompleted = true;
                } else if (chapterNumber <= 0) {
                    swipeContainer.post(() -> swipeContainer.setRefreshing(true));
                   refreshChapter();
                }

                return isCompleted;
            }
        }.execute();

        viewModel.getChaptersBySubject(subjectID).observe(this, this::setUpRecyclerView);
    }

    private void setUpRecyclerView(List<Chapter> chapters){
        if (chapters == null) return;
        this.chapterList = chapters;

        if (mAdapter != null) mAdapter.setChapterList(chapters);
    }

    private void refreshChapter() {

        viewModel.refreshChapters(subjectID).observe(ChapterFragment.this, result -> {
            if (result == null) return;

            if (result.second) {
                swipeContainer.setRefreshing(false);
                isCompleted = true;
            }else{
                mRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(result.first);
                isCompleted = false;
                swipeContainer.setRefreshing(false);
            }
        });
    }
}