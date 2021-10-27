package com.swipecrafts.school.ui.dashboard.attendance.teacher;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.attendance.model.StudentAttendance;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.viewmodel.AttendanceViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;


public class TeacherAttendanceFragment extends BaseFragment {

    public static final String ARG_CLASS_ID = "class_id_key";
    public static final String ARG_CLASS_NAME = "class_name_key";
    public static final String ARG_SECTION_ID = "section_id_key";
    public static final String ARG_SECTION_NAME = "section_name_key";
    private final String TAG = TeacherAttendanceFragment.class.getSimpleName();

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private AttendanceViewModel attendanceViewModel;

    private Toolbar toolbar;

    private RecyclerView studentAttendanceRecycler;

    private AttendanceStudentListAdapter mStudentListAdapter;
    private AttendanceDataAdapter mAttendanceDataAdapter;

    private CustomDialog pDialog;

    private Button submitAttendanceBtn;
    private ToggleButton allStudentPresentToggleBtn;
    private TextView attendanceClassTV;
    private TextView attendanceSectionTV;
    private long classId;
    private long sectionId;
    private String className;
    private String sectionName;

    public static TeacherAttendanceFragment newInstance(Long classId,String className, Long sectionId, String sectionName) {
        Bundle args = new Bundle();
        args.putLong(ARG_CLASS_ID, classId);
        args.putString(ARG_CLASS_NAME, className);
        args.putLong(ARG_SECTION_ID, sectionId);
        args.putString(ARG_SECTION_NAME, sectionName);
        TeacherAttendanceFragment fragment = new TeacherAttendanceFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        attendanceViewModel = ViewModelProviders.of(this, viewModelFactory).get(AttendanceViewModel.class);

        if (getArguments() != null) {
            classId = getArguments().getLong(ARG_CLASS_ID);
            className = getArguments().getString(ARG_CLASS_NAME);
            sectionId = getArguments().getLong(ARG_SECTION_ID);
            sectionName = getArguments().getString(ARG_SECTION_NAME);
        }
    }

    private void showAttendanceData(List<StudentAttendance> attendanceRecord) {
        if (attendanceRecord == null || attendanceRecord.isEmpty()){
            allStudentPresentToggleBtn.setEnabled(false);
            submitAttendanceBtn.setEnabled(false);
        }else {
            allStudentPresentToggleBtn.setVisibility(View.GONE);
            submitAttendanceBtn.setVisibility(View.GONE);
//            submitAttendanceBtn.setEnabled(true);
//            submitAttendanceBtn.setText(getResources().getString(R.string.re_submit));
//            submitAttendanceBtn.setOnClickListener(v -> showStudentsLists(attendanceRecord));
            mAttendanceDataAdapter.updateAttendanceData(attendanceRecord);
            studentAttendanceRecycler.setAdapter(mAttendanceDataAdapter);
        }
    }

    private void showStudentsLists(List<StudentAttendance> attendanceStudents) {
        if (attendanceStudents == null || attendanceStudents.isEmpty()){
            allStudentPresentToggleBtn.setEnabled(false);
            submitAttendanceBtn.setEnabled(false);
        }else {
            submitAttendanceBtn.setText(getResources().getString(R.string.submit));
            submitAttendanceBtn.setOnClickListener(submitAttendanceListener);
            allStudentPresentToggleBtn.setEnabled(true);
            allStudentPresentToggleBtn.setVisibility(View.VISIBLE);
            allStudentPresentToggleBtn.setOnCheckedChangeListener(toggleBtnListener);

            mStudentListAdapter.updateStudentList(attendanceStudents);
            studentAttendanceRecycler.setAdapter(mStudentListAdapter);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_attendance, container, false);


        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = view.findViewById(R.id.teacherAttendanceToolbar);

        studentAttendanceRecycler = (RecyclerView) view.findViewById(R.id.teacherAttendanceRecycler);
        allStudentPresentToggleBtn = (ToggleButton) view.findViewById(R.id.presentAllStudentBtn);
        submitAttendanceBtn = (Button) view.findViewById(R.id.submitAttendanceBtn);

        attendanceClassTV = (TextView) view.findViewById(R.id.labelAttendanceClass);
        attendanceSectionTV = (TextView) view.findViewById(R.id.labelAttendanceSection);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Attendance");
        }

        String htmlClass = String.format("Class: <b> %s </b>", className);
        String htmlSection = String.format("Section: <b> %s </b>", (sectionId != -1 ? " <b>" + sectionName +" </b>": ""));
        attendanceClassTV.setText(Html.fromHtml(htmlClass));
        attendanceSectionTV.setText(Html.fromHtml(htmlSection));

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        studentAttendanceRecycler.setLayoutManager(layoutManager);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(studentAttendanceRecycler.getContext(), layoutManager.getOrientation());
        itemDecoration.setDrawable(ContextCompat.getDrawable(getContext(), R.color.colorGrey));
        studentAttendanceRecycler.addItemDecoration(itemDecoration);
        studentAttendanceRecycler.setItemAnimator(new DefaultItemAnimator());

        // Student Attendance take Adapter
        mStudentListAdapter = new AttendanceStudentListAdapter(getContext(), new ArrayList<>(), isAttendanceComplete ->{
            if (isAttendanceComplete){
                submitAttendanceBtn.setEnabled(true);
            }else {
                submitAttendanceBtn.setEnabled(false);
            }
        });

        // Student Attendance data listing Adapter
        mAttendanceDataAdapter = new AttendanceDataAdapter(getContext(), new ArrayList<>());

        studentAttendanceRecycler.setAdapter(mStudentListAdapter);

        pDialog = showProgressDialog(null, getString(R.string.LOADING));
        attendanceViewModel.getAttendanceResponse(classId, sectionId).observe(this, response ->{
            if (response == null || response.isLoading()) return;

            if (response.isSuccessful()){
                pDialog.dismissWithAnimation();

                if (response.data.getStatus() == 0){ // show students list
                    showStudentsLists(response.data.getAttendanceStudents());
                }else { // show attendance data
                    showAttendanceData(response.data.getAttendanceRecord());
                }
            }else {
                allStudentPresentToggleBtn.setEnabled(false);
                submitAttendanceBtn.setEnabled(false);
                Log.e("Students", "Students failed to Arrive!!");
                showErrorDialog(
                        pDialog,
                        getString(R.string.error_dialog_title),
                        response.message,
                        getString(R.string.close), null);
            }
        });
    }

    CompoundButton.OnCheckedChangeListener toggleBtnListener = (buttonView, isChecked) -> {
        mStudentListAdapter.setAllStudentPresent(isChecked);
        submitAttendanceBtn.setEnabled(isChecked);
    };

    View.OnClickListener submitAttendanceListener = btn ->{
        submitAttendanceBtn.setEnabled(false);
        CustomDialog dialog = showProgressDialog(null, "Submitting Attendance...");
        attendanceViewModel.submitAttendance(mStudentListAdapter.getAttendanceData()).observe(this, response ->{
            if (response == null || response.isLoading()) return;

            if (response.isSuccessful()){
                showSuccessDialog(dialog, "Attendance!", "Students attendance successfully submitted!", getString(R.string.dialog_ok));
                showAttendanceData(mStudentListAdapter.getAttendanceData());
            }else {
                submitAttendanceBtn.setEnabled(true);
                String message = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
                showErrorDialog(dialog, getString(R.string.error_dialog_title), message, getString(R.string.dialog_cancel), null);
            }
        });
    };


}
