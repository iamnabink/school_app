package com.swipecrafts.school.ui.dashboard.assignment.teacher;


import android.Manifest;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.model.db.SchoolClass;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.school.ui.dashboard.assignment.model.Subject;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.FileUtil;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.library.spinner.CustomSpinnerAdapter;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.AssignmentViewModel;

import java.io.File;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;


public class TeacherAssignmentFragment extends BaseFragment {


    private static final int FILES_READ_CODE = 100;
    private static final int FILE_SELECTED_CODE = 101;
    String permission = Manifest.permission.READ_EXTERNAL_STORAGE;
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private Toolbar toolbar;
    private Spinner classListSpinner;
    private Spinner subjectListSpinner;
    private EditText edtTitle;
    private ImageButton btnPickDoc;
    private Button btnUpload;
    private TextView selectedFileTV;
    private AssignmentViewModel viewModel;
    private CustomSpinnerAdapter<SchoolClass> classListAdapter;
    private List<SchoolClass> classList;
    private CustomSpinnerAdapter<Subject> subjectListAdapter;
    private List<Subject> subjectList;


    private String selectedFileDisplayName = "";
    private File selectedFile;

    private int subjectLoaded = -1;
    private int classLoaded = -1;
    private String errorMessage = "something went wrong \n try again";

    private CustomDialog.OnSweetClickListener listener = CustomDialog -> loadClassSubjects();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(AssignmentViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_teacher_assignment, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.teacherAssignmentToolbar);

        classListSpinner = (Spinner) view.findViewById(R.id.classListSpinner);
        subjectListSpinner = (Spinner) view.findViewById(R.id.sectionListSpinner);

        edtTitle = (EditText) view.findViewById(R.id.edtAssignmentTitle);
        selectedFileTV = (TextView) view.findViewById(R.id.selectedFileNameTV);
        btnPickDoc = (ImageButton) view.findViewById(R.id.chooseAssignmentFile);
        btnUpload = (Button) view.findViewById(R.id.btnAssignmentUpload);
    }

    private void init() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        classListAdapter = new CustomSpinnerAdapter<>(getContext(), R.layout.simple_spinner_item, new SchoolClass[]{new SchoolClass(-1, "none")});
        classListSpinner.setAdapter(classListAdapter);

        subjectListAdapter = new CustomSpinnerAdapter<>(getContext(), R.layout.simple_spinner_item, new Subject[]{new Subject(-1, "none")});
        subjectListSpinner.setAdapter(subjectListAdapter);

        loadClassSubjects();

        btnPickDoc.setOnClickListener(this::pickDocument);
        btnUpload.setOnClickListener(this::uploadAssignment);
    }

    private void loadClassSubjects() {
        CustomDialog dialog = showProgressDialog(null, "loading...");
        viewModel.getClassList().observe(this, resource ->{
            if (resource == null) return;

            if (resource.isLoading()){

            }else if (resource.isSuccessful()){
                setUpClassListSpinner(resource.data);
                if (subjectLoaded != -1) {
                    // show retry dialog
                    showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_retry), listener);
                    dialog.showCancelButton(true);
                }else {
                    // hide the loading
                    dialog.dismissWithAnimation();
                }
            } else {
                classLoaded = 0;
                errorMessage = (resource.code() == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : resource.message);
                if (subjectLoaded != -1) {
                    // show retry dialog
                    showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_retry), listener);
                    dialog.showCancelButton(true);
                }
            }
        });

        viewModel.getSubjectLists().observe(this, resource ->{
            if (resource == null) return;

            if (resource.isLoading()){

            }else if (resource.isSuccessful()){
                setUpSubjectListSpinner(resource.data);
                if (classLoaded != -1) {
                    // show retry dialog
                    showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_retry), listener);
                    dialog.showCancelButton(true);
                }else {
                    // hide the loading
                    dialog.dismissWithAnimation();
                }
            }else {
                subjectLoaded = 0;
                errorMessage = (resource.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : resource.message);
                if (classLoaded != -1) {
                    // show retry dialog
                    showErrorDialog(dialog, getString(R.string.error_dialog_title), errorMessage, getString(R.string.dialog_retry), listener);
                    dialog.showCancelButton(true);
                }
            }
        });
    }

    private void setUpClassListSpinner(List<SchoolClass> classList) {
        if (classList == null) return;
        this.classList = classList;
        SchoolClass[] classArray = new SchoolClass[classList.size() + 1];
        classArray[0] = new SchoolClass(-1, "none");
        for (int i = 0; i < classList.size(); i++) {
            classArray[i + 1] = classList.get(i);
        }
        classListAdapter.updateItems(classArray);
    }

    private void setUpSubjectListSpinner(List<Subject> subjectList) {
        if (subjectList == null) return;

        this.subjectList = subjectList;
        Subject[] subjectArray = new Subject[subjectList.size() + 1];
        subjectArray[0] = new Subject(-1L, "none");
        for (int i = 0; i < subjectList.size(); i++) {
            subjectArray[i + 1] = subjectList.get(i);
        }
        subjectListAdapter.updateItems(subjectArray);
    }


    private void pickDocument(View view) {
        try {
            showFileChooser();
        } catch (SecurityException e) {
            requestPermissions(new String[]{permission}, FILES_READ_CODE);
        }
    }

    private void showFileChooser() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
//        String [] mimeTypes = {"text/csv", "text/comma-separated-values"};
//        intent.setType("*/*");
//        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        try {
            startActivityForResult(Intent.createChooser(intent, "Select a File to Upload"), FILE_SELECTED_CODE);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadAssignment(View view) {
        String title = edtTitle.getText().toString().trim();
        edtTitle.setError(null);

        SchoolClass classResponse = (SchoolClass) classListAdapter.getItem(classListSpinner.getSelectedItemPosition());
        Subject subject = (Subject) subjectListAdapter.getItem(subjectListSpinner.getSelectedItemPosition());

        if (classResponse == null || classResponse.getId() == -1) {
            Toast.makeText(getContext(), "Please select least one Class!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (subject == null || subject.getId() == -1) {
            Toast.makeText(getContext(), "Please select least one DCSubject!!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(title)) {
            edtTitle.setError("title can not be empty!!");
            return;
        }

        if (selectedFile == null) {
            Toast.makeText(getContext(), "Please pick the document to upload!!", Toast.LENGTH_SHORT).show();
            return;
        }

        AtomicReference<CustomDialog> dialog = new AtomicReference<>();
        viewModel.postAssignment(title, classResponse.getId(), subject.getId(), selectedFile).observe(this, response ->{
            if (response == null) return;

            if (response.isLoading()){
                dialog.set(showProgressDialog(dialog.get(), "posting assignment..."));
            }else if (response.isSuccessful()){
                edtTitle.setText("");
                selectedFile = null;
                selectedFileTV.setText("No File Selected*");

                showSuccessDialog(dialog.get(), "Assignment", response.data, getString(R.string.dialog_ok));
            }else {
                String errorMessage = (response.status == 1001 ? Utils.isOnline(getContext()) ? Constants.UNKNOWN_ERROR_MESSAGE : Constants.NO_INTERNET_MESSAGE : response.message);
                showErrorDialog(dialog.get(), "Assignment", errorMessage, getString(R.string.dialog_ok), null);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("permissionResult", " -request- " + requestCode + " -result- " + resultCode);
        if (requestCode == FILES_READ_CODE) {
            if (
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{permission}, FILES_READ_CODE);
                }

            } else {
                // do your intended job
                showFileChooser();
            }
        } else if (requestCode == FILE_SELECTED_CODE) {
            if (resultCode == RESULT_OK) {
                if (data.getData() != null) {
                    Uri selectedFileURI = data.getData();
                    Log.e("Files Fragment1: ", selectedFileURI.toString() + " - " + selectedFileURI.getPath());

                    String selectedFilePath = data.getDataString();
                    Log.e("Files Fragment: ", selectedFilePath);

                    selectedFileDisplayName = FileUtil.fileNameFromUri(getContext(), selectedFileURI, selectedFilePath);
                    Log.e("Files Name: ", selectedFileDisplayName);
                    selectedFile = FileUtil.createCacheFile(getContext(), selectedFileURI, selectedFileDisplayName);
                    selectedFileTV.setText(selectedFileDisplayName);

                    if (selectedFile == null) {
                        String path = FileUtil.getRealPathFromUri(getContext(), selectedFileURI);
                        Log.e("RealFilePath", path);
                        selectedFile = new File(path);
                    }

                } else {
                    Toast.makeText(getContext(), "failed to get File!", Toast.LENGTH_SHORT).show();
                }
            }


        }
    }
}
