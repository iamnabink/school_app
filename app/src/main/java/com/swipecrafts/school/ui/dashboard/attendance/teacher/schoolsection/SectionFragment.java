package com.swipecrafts.school.ui.dashboard.attendance.teacher.schoolsection;

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
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.attendance.teacher.TeacherAttendanceFragment;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.viewmodel.AttendanceViewModel;

import java.util.ArrayList;

import javax.inject.Inject;


public class SectionFragment extends BaseFragment {

    public static final String ARG_CLASS_ID = "class_id_key";
    public static final String ARG_CLASS_NAME = "class_name_key";

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AttendanceViewModel attendanceViewModel;

    private Toolbar toolbar;
    private RecyclerView sectionRecycler;
    private SectionRecyclerViewAdapter mClassListAdapter;
    private TextView classNameTV;

    private long classId;
    private String className = "";

    public static SectionFragment newInstance(long classId, String className) {
        Bundle args = new Bundle();
        args.putLong(ARG_CLASS_ID, classId);
        args.putString(ARG_CLASS_NAME, className);
        SectionFragment fragment = new SectionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        attendanceViewModel = ViewModelProviders.of(this, viewModelFactory).get(AttendanceViewModel.class);

        if (getArguments() != null){
            classId = getArguments().getLong(ARG_CLASS_ID);
            className = getArguments().getString(ARG_CLASS_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_section_list, container, false);

        findView(view);
        init();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        CustomDialog pDialog = showProgressDialog(null, getString(R.string.LOADING));
        attendanceViewModel.getSections(classId).observe(this, data ->{
            if (data == null || data.isEmpty()){
                showErrorDialog(
                        pDialog,
                        getString(R.string.error_dialog_title),
                        "something went wrong!",
                        getString(R.string.close), CustomDialog -> getActivity().onBackPressed());
            }else{
                pDialog.dismissWithAnimation();
                mClassListAdapter.updateClasses(data);
            }
        });
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.sectionListToolbar);

        sectionRecycler = (RecyclerView) view.findViewById(R.id.schoolSectionRecycler);

        classNameTV = (TextView) view.findViewById(R.id.classNameTV);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Choose Section");
        }

        classNameTV.setText(String.format("sections of %s", className));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        sectionRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(sectionRecycler.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        sectionRecycler.addItemDecoration(itemDecoration);
        sectionRecycler.setItemAnimator(new DefaultItemAnimator());
        mClassListAdapter = new SectionRecyclerViewAdapter(new ArrayList<>(), model -> {
            TeacherAttendanceFragment fragment = TeacherAttendanceFragment.newInstance(model.getClassId(), className, model.getSectionId(), model.getSection());
            replaceFragment(R.id.main_container, fragment, SectionFragment.class.getSimpleName(), TeacherAttendanceFragment.class.getSimpleName());

        });
        sectionRecycler.setAdapter(mClassListAdapter);
    }
}
