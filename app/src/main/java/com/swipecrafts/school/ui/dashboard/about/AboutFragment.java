package com.swipecrafts.school.ui.dashboard.about;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.SchoolDetails;
import com.swipecrafts.library.views.CircleImageView;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.SchoolDetailsViewModel;

import javax.inject.Inject;

public class AboutFragment extends Fragment {

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Toolbar toolbar;
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private SchoolDetailsViewModel schoolDetailsViewModel;
    private SchoolDetails schoolDetails;
    private ImageView schoolBgIV;
    private CircleImageView schoolLogoIV;
    private TextView schoolETDDateTV;
    private TextView schoolAddressTV;
    private TextView schoolEmailTV;
    private TextView schoolWebTV;
    private TextView schoolMessageTV;
    private TextView schoolDescriptionTV;
    private TextView schoolRulesTV;
    private SwipeRefreshLayout swipeContainer;
    private NestedScrollView schoolProfileLyt;
    private LinearLayout errorLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        schoolDetailsViewModel = ViewModelProviders.of(this, viewModelFactory).get(SchoolDetailsViewModel.class);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.aboutToolbar);
        appBarLayout = view.findViewById(R.id.app_bar_about);
        collapsingToolbarLayout = view.findViewById(R.id.aboutCollapseToolbar);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.schoolProfileSwipeToRefreshLayout);

        errorLayout = (LinearLayout) view.findViewById(R.id.errorLayout);
        schoolProfileLyt = (NestedScrollView) view.findViewById(R.id.schoolProfileSVLyt);

        schoolBgIV = (ImageView) view.findViewById(R.id.schoolBgImageView);
        schoolLogoIV = (CircleImageView) view.findViewById(R.id.circle_school_profile_image);

        schoolETDDateTV = (TextView) view.findViewById(R.id.schoolESTDDateTV);
        schoolAddressTV = (TextView) view.findViewById(R.id.scgoolAddressTV);

        schoolEmailTV = (TextView) view.findViewById(R.id.scgoolEmailTV);
        schoolWebTV = (TextView) view.findViewById(R.id.scgoolWebTV);
        schoolMessageTV = (TextView) view.findViewById(R.id.schoolMessageTV);
        schoolRulesTV = (TextView) view.findViewById(R.id.schoolRulesTV);
        schoolDescriptionTV = (TextView) view.findViewById(R.id.schoolDescriptionTV);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
//            actionBar.setHomeAsUpIndicator(R.drawable);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(() -> refreshSchoolDetails(true));
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        refreshSchoolDetails(false);
    }

    private void refreshSchoolDetails(boolean refresh) {
        schoolDetailsViewModel.loadSchoolDetails(refresh).observe(this, resource -> {
            if (resource == null || resource.isLoading()) return;

            swipeContainer.setRefreshing(false);
            if (resource.isSuccessful()) {
                populateSchoolDetailsToView(resource.data);
            } else {
                if (resource.data == null) {
                    appBarLayout.setExpanded(false);
                    appBarLayout.setEnabled(false);
                    schoolProfileLyt.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    TextView msg = errorLayout.findViewById(R.id.error_message);
                    String message = Utils.isOnline(getContext()) ? (resource.code() == 1001 ? Constants.UNKNOWN_ERROR_MESSAGE : resource.message) : Constants.NO_INTERNET_MESSAGE ;
                    msg.setText(message);
                } else {
                    Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                    populateSchoolDetailsToView(resource.data);
                }
            }
        });
    }

    private void populateSchoolDetailsToView(SchoolDetails schoolDetails) {
        appBarLayout.setExpanded(true);
        appBarLayout.setEnabled(true);
        schoolProfileLyt.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);

        this.schoolDetails = schoolDetails;

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progressbar)
                .error(R.drawable.placeholder);

        Glide.with(this)
                .load(schoolDetails.getBackgroundImage())
                .apply(options)
                .into(schoolBgIV);

//        Log.e("Pinnacle Bg", schoolDetails.getBackgroundImage());

        Glide.with(this)
                .load(schoolDetails.getLogo())
                .apply(options)
                .into(schoolLogoIV);

        collapsingToolbarLayout.setTitle(schoolDetails.getName());
        schoolAddressTV.setText(schoolDetails.getAddress());
        schoolEmailTV.setText(schoolDetails.getEmail());
        schoolWebTV.setText(schoolDetails.getWebsite());

        schoolETDDateTV.setText(schoolDetails.getEstDate());
        schoolRulesTV.setText(Html.fromHtml(schoolDetails.getRules()));
        schoolMessageTV.setText(Html.fromHtml(schoolDetails.getMsg()));
        schoolDescriptionTV.setText(Html.fromHtml(schoolDetails.getSchoolDescription()));
    }
}
