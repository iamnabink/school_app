package com.swipecrafts.school.ui.dashboard.application;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.viewmodel.LeaveApplicationViewModel;

import javax.inject.Inject;

public class ApplicationFragment extends BaseFragment {


    private Toolbar toolbar;

    private EditText edtTitle;
    private EditText edtSubject;
    private EditText edtBody;
    private Button btnSubmitApplication;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private LeaveApplicationViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(LeaveApplicationViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_application, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {

        toolbar = (Toolbar) view.findViewById(R.id.leaveApplicationToolbar);


        edtTitle = (EditText) view.findViewById(R.id.edtApplicationTitle);
        edtSubject = (EditText) view.findViewById(R.id.edtApplicationSubject);
        edtBody = (EditText) view.findViewById(R.id.edtApplicationBody);

        btnSubmitApplication = (Button) view.findViewById(R.id.btnApplication);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnSubmitApplication.setOnClickListener(this::postApplication);
    }

    private void postApplication(View button) {
        String title = edtTitle.getText().toString().trim();
        String subject = edtSubject.getText().toString().trim();
        String body = edtBody.getText().toString();


        if (TextUtils.isEmpty(title)){

            edtTitle.setError("title can not be empty!");
            return;
        }

        if (TextUtils.isEmpty(subject)){

            edtSubject.setError("subject can not be empty!");
            return;
        }

        if (TextUtils.isEmpty(body)){

            edtBody.setError("body can not be empty!");
            return;
        }


        CustomDialog dialog = showProgressDialog(null, "Posting Leave Application...");
        viewModel.postLeaveApplication(title, subject, body).observe(this, data ->{
            if (data == null) return;

            if (data.status()){
                showSuccessDialog(dialog, "Leave Application!", "Leave application ", getString(R.string.dialog_ok));
                edtTitle.setText("");
                edtSubject.setText("");
                edtBody.setText("");
            }else {
                showErrorDialog(dialog, getString(R.string.error_dialog_title), data.getMessage(), getString(R.string.dialog_ok), null);
            }
        });

    }

}
