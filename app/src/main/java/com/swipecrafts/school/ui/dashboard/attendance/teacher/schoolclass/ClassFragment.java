package com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolclass;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
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

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.TeacherAttendanceFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolsection.SectionFragment;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.viewmodel.AttendanceViewModel;

import java.util.ArrayList;

import javax.inject.Inject;


public class ClassFragment extends BaseFragment {


    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AttendanceViewModel attendanceViewModel;

    private Toolbar toolbar;
    private RecyclerView classRecycler;
    private ClassRecyclerViewAdapter mClassListAdapter;
    private SchoolClass classModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        attendanceViewModel = ViewModelProviders.of(this, viewModelFactory).get(AttendanceViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);

        findView(view);
        init();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        CustomDialog pDialog = showProgressDialog(null, getString(R.string.LOADING));
        attendanceViewModel.loadClasses().observe(this, resource -> {
            if (resource == null) return;

            if (resource.status == Status.SUCCESS) {
                pDialog.dismissWithAnimation();
                mClassListAdapter.updateClasses(resource.data);
            } else if (resource.status == Status.ERROR) {
                showErrorDialog(
                        pDialog,
                        getString(R.string.error_dialog_title),
                        resource.message,
                        getString(R.string.close), null);
            }
        });
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.classListToolbar);

        classRecycler = (RecyclerView) view.findViewById(R.id.schoolClassRecycler);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Choose Class");
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        classRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(classRecycler.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorIconTint));
        classRecycler.addItemDecoration(itemDecoration);
        classRecycler.setItemAnimator(new DefaultItemAnimator());
        mClassListAdapter = new ClassRecyclerViewAdapter(new ArrayList<>(), model -> {
            this.classModel = model;
            attendanceViewModel.getSections(model.getClassId()).observe(this, sections -> {
                if (sections == null || sections.isEmpty()) {
                    TeacherAttendanceFragment fragment = TeacherAttendanceFragment.newInstance(classModel.getClassId(), classModel.getClassName(), -1L , "");
                    replaceFragment(R.id.main_container, fragment, ClassFragment.class.getSimpleName(), TeacherAttendanceFragment.class.getSimpleName());
                } else {
                    SectionFragment fragment = SectionFragment.newInstance(classModel.getClassId(), classModel.getClassName());
                    replaceFragment(R.id.main_container, fragment, ClassFragment.class.getSimpleName(), SectionFragment.class.getSimpleName());
                }
            });
        });
        classRecycler.setAdapter(mClassListAdapter);
    }
}
