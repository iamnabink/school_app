package com.swipecrafts.school.ui.dashboard.profile;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.Parent;
import com.swipecrafts.school.data.model.db.Student;
import com.swipecrafts.library.views.CircleImageView;
import com.swipecrafts.library.views.ErrorView;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.StudentViewModel;

import java.util.List;

import javax.inject.Inject;

public class StudentProfileFragment extends BaseFragment {

    StudentViewModel studentViewModel;

    @Inject
    ViewModelProvider.Factory viewModelFactory;


    private AppBarLayout appBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private Toolbar toolbar;
    private ErrorView errorLayout;
    private CardView studentDetailsLyt;

    private CircleImageView studentImageView;

    private TextView studentLocationTV;
    private TextView studentRegNoTV;
    private TextView studentClassTV;
    private TextView studentSectionTV;
    private TextView studentDOBTV;
    private TextView studentAddressTV;
    private TextView studentPhoneTV;
    private TextView studentGenderTV;
    private TextView studentReligionTV;
    private TextView studentCasteTV;
    private TextView studentBloodGroupTV;

    private TextView studentFatherNameTV;
    private TextView studentFatherOccupationTV;
    private TextView studentFatherPhoneTV;
    private TextView studentFatherOfficeTV;

    private TextView studentMotherNameTV;
    private TextView studentMotherOccupationTV;
    private TextView studentMotherPhoneTV;
    private TextView studentMotherOfficeTV;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        studentViewModel = ViewModelProviders.of(this, viewModelFactory).get(StudentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_student_profile, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        // Lookup the swipe container view
        appBar = (AppBarLayout) view.findViewById(R.id.app_bar_profile);
        toolbar = view.findViewById(R.id.profileToolBar);
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefreshLayout);
        collapsingToolbarLayout = view.findViewById(R.id.profileCollapseToolbar);

        errorLayout = (ErrorView) view.findViewById(R.id.errorLayout);

        studentDetailsLyt = (CardView) view.findViewById(R.id.studentDetailsLyt);

        studentImageView = (CircleImageView) view.findViewById(R.id.circle_profile_image);

        studentLocationTV = (TextView) view.findViewById(R.id.studentLocationTV);
        studentRegNoTV = (TextView) view.findViewById(R.id.studentRegNoTV);
        studentClassTV = (TextView) view.findViewById(R.id.studentClassNameTV);
        studentSectionTV = (TextView) view.findViewById(R.id.studentSectionTV);
        studentDOBTV = (TextView) view.findViewById(R.id.studentDOBTV);
        studentAddressTV = (TextView) view.findViewById(R.id.studentAddressTV);
        studentPhoneTV = (TextView) view.findViewById(R.id.studentPhoneTV);
        studentGenderTV = (TextView) view.findViewById(R.id.studentGenderTV);
        studentReligionTV = (TextView) view.findViewById(R.id.studentReligionTV);
        studentCasteTV = (TextView) view.findViewById(R.id.studentCasteTV);
        studentBloodGroupTV = (TextView) view.findViewById(R.id.studentBloodGroupTV);

        studentFatherNameTV = (TextView) view.findViewById(R.id.fatherNameTV);
        studentFatherOccupationTV = (TextView) view.findViewById(R.id.fatherOccupationTV);
        studentFatherPhoneTV = (TextView) view.findViewById(R.id.fatherPhoneTV);
        studentFatherOfficeTV = (TextView) view.findViewById(R.id.fatherOfficeTV);

        studentMotherNameTV = (TextView) view.findViewById(R.id.motherNameTV);
        studentMotherOccupationTV = (TextView) view.findViewById(R.id.motherOccupationTV);
        studentMotherPhoneTV = (TextView) view.findViewById(R.id.motherPhoneTV);
        studentMotherOfficeTV = (TextView) view.findViewById(R.id.motherOfficeTV);

    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        errorLayout.setOnRetryListener(btn -> {
            errorLayout.changeType(ErrorView.PROGRESS);
            getStudentDetails(true);
        });

        swipeRefreshLayout.setOnRefreshListener(() -> getStudentDetails(true));

        getStudentDetails(false);
    }

    private void getStudentDetails(boolean refresh) {
        studentViewModel.loadStudents(refresh).observe(this, resource -> {
            if (resource == null) return;

            if (resource.isLoading()){
                Log.e("Student", "Data is loading!!");
            }else if (resource.isSuccessful()) {
                setUpStudentData(resource.data);
                swipeRefreshLayout.setRefreshing(false);
            } else {
                swipeRefreshLayout.setRefreshing(false);
                if (resource.data == null) {
                    Log.e("Student", "Null "+ resource.message);
                    studentDetailsLyt.setVisibility(View.GONE);
                    errorLayout.setVisibility(View.VISIBLE);
                    String message = !Utils.isOnline(getContext()) ? Constants.NO_INTERNET_MESSAGE : resource.message + "";
                    errorLayout.setErrorTitle(message);
                    errorLayout.changeType(ErrorView.ERROR_WITH_BUTTON);
                    errorLayout.apply();
                    errorLayout.setVisibility(View.VISIBLE);
                } else {
                    Log.e("Student", "Has Data "+ resource.message);
                    Toast.makeText(getContext(), resource.message, Toast.LENGTH_SHORT).show();
                    setUpStudentData(resource.data);
                }
            }
        });
    }

    public void setUpStudentData(@NonNull Student model) {
        LiveData<List<Parent>> parentLiveData = studentViewModel.loadParents(model.getStudentId());
        parentLiveData.observe(this, parents -> {
            if (parents == null) return;
            parentLiveData.removeObservers(StudentProfileFragment.this);
            setUpParentsDetails(parents);
        });

        studentDetailsLyt.setVisibility(View.VISIBLE);
        errorLayout.setVisibility(View.GONE);

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.progressbar)
                .error(R.drawable.placeholder);

        Glide.with(this)
                .load(model.getProfileImg())
                .apply(options)
                .into(studentImageView);

        collapsingToolbarLayout.setTitle(model.getName());
        studentLocationTV.setText(model.getAddress());
        studentRegNoTV.setText(model.getRegistrationNo());
        studentClassTV.setText(model.getClassName());
        studentSectionTV.setText(model.getSection());
        studentDOBTV.setText(model.getDateOfBirth());
        studentAddressTV.setText(model.getAddress());
        studentPhoneTV.setText(model.getPhone());
        studentGenderTV.setText(model.getGender());
        studentReligionTV.setText(model.getReligion());
        studentCasteTV.setText(model.getCaste());
        studentBloodGroupTV.setText(model.getBloodGroup());
    }

    public void setUpParentsDetails(List<Parent> parents) {
        if (parents == null) return;

        Parent father = null;
        Parent mother = null;

        for (Parent parent : parents) {
            if (parent.getRelation().equalsIgnoreCase("father")) {
                father = parent;
            } else if (parent.getRelation().equalsIgnoreCase("mother")){
                mother = parent;
            }
        }

        if (father != null) {
            studentFatherNameTV.setText(String.format("%s %s %s", father.getFirstName(), father.getMiddleName(), father.getLastName()));
            studentFatherOccupationTV.setText(father.getOccupation());
            studentFatherPhoneTV.setText(father.getPhone());
            studentFatherOfficeTV.setText(father.getOffice());
        }
        if (mother != null) {
            studentMotherNameTV.setText(String.format("%s %s %s", mother.getFirstName(), mother.getMiddleName(), mother.getLastName()));
            studentMotherOccupationTV.setText(mother.getOccupation());
            studentMotherPhoneTV.setText(mother.getPhone());
            studentMotherOfficeTV.setText(mother.getOffice());
        }
    }
}
