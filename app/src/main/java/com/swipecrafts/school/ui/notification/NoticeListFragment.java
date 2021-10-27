package com.swipecrafts.school.ui.notification;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.school.data.model.db.Notification;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.gallery.FullScreenImageGalleryActivity;
import com.swipecrafts.library.dialog.CustomNotificationDialog;
import com.swipecrafts.school.ui.notification.adapter.NoticesAdapter;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.utils.listener.AdapterListener;
import com.swipecrafts.school.viewmodel.NotificationViewModel;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NoticeListFragment extends BaseFragment implements AdapterListener<Notification> {

    public final static String ARG_TYPE = "notificationType";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private NotificationViewModel notificationViewModel;
    private RecyclerView mRecyclerView;
    private NoticesAdapter mAdapter;
    private List<Notification> notificationList;


    private ErrorView errorLayout;

    private UserType noticeViewType = UserType.PARENT;
    private CustomNotificationDialog notificationDialog;

    public static NoticeListFragment newInstance(UserType type) {
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type.getValue());

        NoticeListFragment fragment = new NoticeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication())
                .applicationComponent()
                .inject(this);

        //Set up and subscribe (observe) to the ViewModel
        notificationViewModel = ViewModelProviders.of(this, viewModelFactory).get(NotificationViewModel.class);


        if (getArguments() != null) {
            String value = getArguments().getString(ARG_TYPE);
            noticeViewType = value == null ? UserType.PARENT : UserType.from(value);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.fragment_notice_list, container, false);
        findView(mainView);
        init();
        return mainView;
    }

    private void findView(View mainView) {
        mRecyclerView = (RecyclerView) mainView.findViewById(R.id.notification_list_recycler);

        errorLayout = (ErrorView) mainView.findViewById(R.id.errorLayout);

        notificationDialog = new CustomNotificationDialog(getContext(), R.style.noticeDialog);

    }

    private void init() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (notificationList == null) notificationList = new ArrayList<>();
        mAdapter = new NoticesAdapter(notificationList,this, (imageView, model) -> {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(R.drawable.progressbar)
                    .error(R.drawable.placeholder);

            Glide.with(this)
                    .load(model.getRemoteImgUrl())
                    .apply(options)
                    .into(imageView);
        });
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

        errorLayout.setViewType(ErrorView.ERROR_WITH_BUTTON)
                .setOnRetryListener(v -> loadData(true))
                .apply();

        loadData(false);
    }

    private void loadData(boolean refresh) {
        notificationViewModel.getNotificationsByType(noticeViewType, refresh).observe(this, resource -> {
            if (resource == null) return;

            if (resource.isLoading()) return;

            if (resource.isSuccessful()) {
                mRecyclerView.setVisibility(View.VISIBLE);
                errorLayout.setVisibility(View.GONE);
                mAdapter.setNotifications(resource.data);
                mAdapter.notifyDataSetChanged();
            } else {
                String message = Utils.isOnline(getContext()) ? (resource.code() == 1001 ? Constants.UNKNOWN_ERROR_MESSAGE : resource.message) : Constants.NO_INTERNET_MESSAGE ;
                if (mAdapter.getItemCount() != 0) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    return;
                }
                errorLayout.setViewType(ErrorView.ERROR_WITH_BUTTON)
                        .setErrorTitle(message)
                        .apply();
                mRecyclerView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public void onItemClicked(Notification item) {
        if (notificationDialog.isShowing()) notificationDialog.dismiss();

        notificationDialog.setTitle(item.getTitle());
        notificationDialog.setMessage(Html.fromHtml(item.getDescription()));

        if (item.getRemoteImgUrl() != null && !TextUtils.isEmpty(item.getRemoteImgUrl()) && !item.getRemoteImgUrl().endsWith("/")) {
            notificationDialog.setImageUri(item.getRemoteImgUrl());
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

            ArrayList<String> imgList = new ArrayList<String>();
            imgList.add(item.getRemoteImgUrl());

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
        }

        if (item.getNoticeLink() != null) {
            notificationDialog.setFileLink(item.getNoticeLink());
        }

        if (item.getParentAvailability() == 1) {
            notificationDialog.setCondition("Required");
            notificationDialog.setConditionTextColor(getResources().getColor(R.color.colorImportantText));
        } else {
            notificationDialog.setConditionVisibility(false);
        }

//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//        String noticeTime = sdf.format(item.getTime());
        String noticeTime = DateFormat.getDateInstance(DateFormat.LONG).format(item.getTime());
        notificationDialog.setTime(noticeTime);
        notificationDialog.apply();
        notificationDialog.show();
    }
}
