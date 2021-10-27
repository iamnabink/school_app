package com.swipecrafts.school.ui.dashboard.booklist;

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
import com.swipecrafts.school.data.model.db.Book;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.viewmodel.BookViewModel;

import java.util.List;

import javax.inject.Inject;


public class BookFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private RecyclerView mRecyclerView;
    private BookListAdapter mAdapter;
    private BookViewModel viewModel;

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private LinearLayout errorLayout;

    private boolean isFirstRefresh = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BookViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_book, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.bookSwipeToRefreshLayout);
        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);


        toolbar = view.findViewById(R.id.bookToolbar);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bookRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(this::refreshBooks);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        viewModel.countBooks().observe(this, booksCount -> {
            if (booksCount == null) return;
            if (booksCount <= 0) {
                isFirstRefresh = true;
                swipeContainer.post(() -> swipeContainer.setRefreshing(true));
                refreshBooks();

            }
        });

        viewModel.getBooks().observe(this, this::setUpRecyclerView);
    }

    public void setUpRecyclerView(List<Book> books) {
        if (mAdapter == null) {
            mAdapter = new BookListAdapter(books, (imageView, book) -> {

                RequestOptions options = new RequestOptions()
                        .placeholder(R.drawable.progressbar)
                        .error(R.drawable.placeholder);

                Glide.with(this)
                        .load(book.getBookImgUrl())
                        .apply(options)
                        .into(imageView);
            });
            mRecyclerView.setAdapter(mAdapter);
        } else {
            mAdapter.updateBookList(books);
        }
    }

    private void refreshBooks() {
        viewModel.refreshBooks().observe(this, (result) -> {
            if (result == null) return;


            if (result.second) {
                mRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
            } else {
                swipeContainer.setRefreshing(false);
                if (!isFirstRefresh) {
                    Toast.makeText(getContext(), result.first, Toast.LENGTH_SHORT).show();
                    return;
                }
                mRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
                TextView msg = errorLayout.findViewById(R.id.error_message);
                msg.setText(result.first);
            }
        });
    }
}
