package com.swipecrafts.school.ui.dashboard.suggestion;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.ui.base.BaseFragment;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.viewmodel.SuggestionViewModel;

import javax.inject.Inject;


public class SuggestionFragment extends BaseFragment {

    public static final int ARG_SUGGESTION_TYPE = 0;
    public static final int ARG_APPRECIATION_TYPE = 1;
    private static final String ARG_TYPE = "Suggestion_Or_Appreciation";
    private final String TAG = SuggestionFragment.class.getSimpleName();
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private SuggestionViewModel suggestionViewModel;

    private Toolbar toolbar;
    private TextView edtTitle;
    private TextView edtBody;
    private CheckBox chkAnonymous;
    private Button btnSubmit;

    private int type = -1;

    public static SuggestionFragment newInstance(int type) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, type);

        SuggestionFragment fragment = new SuggestionFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            type = getArguments().getInt(ARG_TYPE);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);

        suggestionViewModel = ViewModelProviders.of(this, viewModelFactory).get(SuggestionViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_suggestion, container, false);

        findView(view);
        init();

        return view;
    }

    private void findView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.suggestionToolbar);
        edtTitle = (TextView) view.findViewById(R.id.edtSuggestionTitle);
        edtBody = (TextView) view.findViewById(R.id.edtSuggestionBody);
        chkAnonymous = (CheckBox) view.findViewById(R.id.chkBtnAnonymous);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmitSuggestion);
    }

    private void init() {
        String title = type == 0 ? "Suggestion" : "Appreciation";

        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.setTitle(title);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnSubmit.setOnClickListener(it -> postSuggestion());
    }

    public void postSuggestion() {
        Log.e(TAG, "Btn Clicked!!");
        edtTitle.setBackground(getContext().getResources().getDrawable(R.drawable.custom_edt_text));
        edtBody.setBackground(getContext().getResources().getDrawable(R.drawable.custom_edt_text));

        String title = edtTitle.getText().toString().trim();
        String body = edtBody.getText().toString();
        int isAnonymous = chkAnonymous.isChecked() ? 1 : 0;

        if (TextUtils.isEmpty(title)) {
            // show title error!!
            edtTitle.setError("title can not be empty!!");
            edtTitle.setBackground(getContext().getResources().getDrawable(R.drawable.custom_edt_text_error));
            return;
        }

        if (TextUtils.isEmpty(body)) {
            // show body error!!
            edtBody.setError("body can not be empty!!");
            edtBody.setBackground(getContext().getResources().getDrawable(R.drawable.custom_edt_text_error));
            return;
        }


        btnSubmit.setEnabled(false);

        if (type == 0) {
            CustomDialog dialog = showProgressDialog(null, "Submitting suggestion...");
            suggestionViewModel.postSuggestion(title, body, isAnonymous).observe(this, result -> {
                if (result == null) return;
                btnSubmit.setEnabled(true);
                if (result.second) {
                    showSuccessDialog(dialog, "Suggestion!", "Your suggestion successfully submitted!", "OK");
                    edtTitle.setText("");
                    edtBody.setText("");
                } else {
                    showErrorDialog(dialog, "Oops...", result.first, "OK", null);
                }
            });
        } else {
            CustomDialog dialog = showProgressDialog(null, "Submitting suggestion...");

            suggestionViewModel.postAppreciation(title, body, isAnonymous).observe(this, result -> {
                if (result == null) return;
                btnSubmit.setEnabled(true);
                if (result.second) {
                    showSuccessDialog(dialog, "Appreciation!", "Your appreciation successfully submitted!", "OK");
                    edtTitle.setText("");
                    edtBody.setText("");
                } else {
                    showErrorDialog(dialog, "Oops...", result.first, "OK", null);
                }
            });
        }
    }


}
