package com.swipecrafts.school.ui.home;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.db.Event;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.library.dialog.CustomNotificationDialog;
import com.swipecrafts.library.imageSlider.SliderView;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.gallery.FullScreenImageGalleryActivity;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.viewmodel.HomeViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HomeFragment extends BaseFragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private HomeViewModel viewModel;
    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private List<HomeModel> homeModelList;
    private AppBarLayout appbar;
    private Toolbar toolbar;
    private SwipeRefreshLayout swipeContainer;
    private ErrorView errorLayout;
    private SliderView imgSlider;
    private CustomNotificationDialog notificationDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        appbar = view.findViewById(R.id.app_bar_home);

        toolbar = view.findViewById(R.id.homeToolbar);
        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.homeSwipeToRefreshLayout);

        errorLayout = (ErrorView) view.findViewById(R.id.errorLayout);

        imgSlider = (SliderView) view.findViewById(R.id.imageSliderView);

        notificationDialog = new CustomNotificationDialog(getContext(), R.style.noticeDialog);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.homeRecyclerView);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        appbar.setEnabled(false);
        appbar.setExpanded(false);
        imgSlider.setVisibility(View.GONE);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> refreshHomeItems(true));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //set home items
        if (homeModelList == null) homeModelList = new ArrayList<>();
        mAdapter = new HomeAdapter(homeModelList, this::openHomeDialog);
        mRecyclerView.setAdapter(mAdapter);

        errorLayout.setViewType(ErrorView.PROGRESS).apply();
        errorLayout.setOnRetryListener(v -> refreshHomeItems(true));
        refreshHomeItems(false);

        imgSlider.setLoader((imageView, url) -> {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.progressbar)
                    .error(R.drawable.placeholder);

            Glide.with(this)
                    .load(url)
                    .apply(options)
                    .into(imageView);
        });
    }

    private void refreshHomeItems(boolean loadFromRemote) {
        viewModel.getHomeItems(loadFromRemote).observe(this, result -> {
            if (result == null) return;

            LogUtils.errorLog("HomeType", result.status +"");

            if (result.status == Status.LOADING) {
                return;
            }

            if (swipeContainer.isRefreshing()) swipeContainer.post(() -> swipeContainer.setRefreshing(false));
            if (result.status == Status.SUCCESS) {
                errorLayout.setVisibility(View.GONE);
                swipeContainer.setVisibility(View.VISIBLE);
                mAdapter.updateItems(result.data);
                this.homeModelList = result.data;
            } else if (result.status == Status.ERROR) {
                if (mAdapter.getItemCount() != 0) {
                    swipeContainer.setVisibility(View.VISIBLE);
                    Toast.makeText(getContext(), result.message, Toast.LENGTH_SHORT).show();
                    return;
                }

                errorLayout
                        .setErrorTitle(getResources().getString(R.string.home_no_data_message))
                        .setViewType(ErrorView.ERROR_WITH_BUTTON)
                        .apply();
                swipeContainer.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });

        viewModel.getImageUrls(loadFromRemote).observe(this, result -> {
            if (result == null || result.data == null || result.data.isEmpty()) return;
            switch (result.status) {
                case LOADING:
                    // update UI if needed
                    break;
                case SUCCESS:
                    appbar.setEnabled(true);
                    appbar.setExpanded(true);

                    imgSlider.setVisibility(View.VISIBLE);
                    imgSlider.setImages(result.data);
                    break;
                default:
                    appbar.setEnabled(false);
                    appbar.setExpanded(false);
                    imgSlider.setVisibility(View.GONE);
                    break;
            }
        });
    }


    public void openHomeDialog(HomeModel item) {

        if (notificationDialog.isShowing()) notificationDialog.dismiss();

        String title = "";
        String desc = "";
        String imgUrl = "";
        int parentAV = 0;

        int noticeTypeIcon = R.drawable.ic_notifications;

        if (item.TYPE.equals(HomeModel.NOTIFICATION_TYPE)) {
            Notification model = (Notification) item;
            title = model.getTitle();
            desc = model.getDescription();
            imgUrl = model.getRemoteImgUrl();
            parentAV = model.getParentAvailability();
            noticeTypeIcon = R.drawable.ic_notifications;
        } else if (item.TYPE.equals(HomeModel.EVENT_TYPE)) {
            Event model = (Event) item;
            title = model.getTitle();
            desc = model.getDescription();
            noticeTypeIcon = R.drawable.ic_calendar;
        }

        notificationDialog.setTitle(title);
        notificationDialog.setMessage(Html.fromHtml(desc));
        notificationDialog.setDialogIconResource(noticeTypeIcon);

        if (!TextUtils.isEmpty(imgUrl) && !imgUrl.endsWith("/")) {
            notificationDialog.setImageUri(imgUrl);
            notificationDialog.setImageLoader((imageView, url) -> {
                RequestOptions options = new RequestOptions()
                        .centerCrop()
                        .placeholder(R.drawable.progressbar)
                        .error(R.drawable.placeholder);

                Glide.with(this)
                        .load(url)
                        .apply(options)
                        .into(imageView);
            });

            ArrayList<String> imgList = new ArrayList<>();
            imgList.add(imgUrl);

            notificationDialog.setImageListener(() -> {
                Intent i = new Intent(getActivity(), FullScreenImageGalleryActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList(FullScreenImageGalleryActivity.KEY_IMAGES, imgList);
                bundle.putInt(FullScreenImageGalleryActivity.KEY_POSITION, 0);
                i.putExtras(bundle);
                startActivity(i);
            });
        } else {
            notificationDialog.setImageUri(null);
            notificationDialog.setImageUri(null);
        }

        if (parentAV == 1) {
            notificationDialog.setConditionHeader("Parent availability :");
            notificationDialog.setCondition("Required");
            notificationDialog.setConditionTextColor(getResources().getColor(R.color.colorImportantText));
        } else {
            notificationDialog.setConditionVisibility(false);
        }

        String noticeTime = DateFormat.getDateInstance(DateFormat.LONG).format(item.getTime());
        notificationDialog.setTime(noticeTime);
        notificationDialog.apply();
        notificationDialog.show();
    }

    @Override
    public void onDestroy() {
        imgSlider.clean();
        super.onDestroy();
    }
}
