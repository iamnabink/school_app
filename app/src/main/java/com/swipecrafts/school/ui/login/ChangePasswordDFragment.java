package com.swipecrafts.school.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.viewmodel.UserViewModel;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 6/17/2018.
 */
public class ChangePasswordDFragment extends DialogFragment {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private Toolbar toolbar;

    private TextInputEditText edtOldPassword;
    private TextInputEditText edtNewPassword;
    private TextInputEditText edtConfirmPassword;

    private TextInputLayout lytOldPassword;
    private TextInputLayout lytNewPassword;
    private TextInputLayout lytConfirmPassword;
    private Button btnChangePwd;

    private FrameLayout progressContainer;

    private UserViewModel userViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.ChangePasswordDialog);


        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        //Set up and subscribe (observe) to the ViewModel
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        findView(view);
        init();
        return view;
    }

    private void findView(View view){
        toolbar = (Toolbar) view.findViewById(R.id.passwordResetToolbar);

        lytOldPassword = (TextInputLayout) view.findViewById(R.id.oldPasswordInpLyt);
        lytNewPassword = (TextInputLayout) view.findViewById(R.id.newPasswordInpLyt);
        lytConfirmPassword = (TextInputLayout) view.findViewById(R.id.confPasswordInpLyt);

        edtOldPassword = (TextInputEditText) view.findViewById(R.id.edtOldPassword);
        edtNewPassword = (TextInputEditText) view.findViewById(R.id.edtNewPassword);
        edtConfirmPassword = (TextInputEditText) view.findViewById(R.id.edtConfirmPassword);

        btnChangePwd = (Button) view.findViewById(R.id.btnChangePassword);

        progressContainer = (FrameLayout) view.findViewById(R.id.progressContainer);
    }

    private void init() {
        toolbar.setTitle("change your password");
        //Set navigation icon to back button drawable
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(v -> dismiss());
        progressContainer.setVisibility(View.GONE);

        btnChangePwd.setOnClickListener(v ->{
            lytOldPassword.setError(null);
            lytNewPassword.setError(null);
            lytConfirmPassword.setError(null);
            String oldPwd = edtOldPassword.getText().toString();
            String newPwd = edtNewPassword.getText().toString();
            String confPwd = edtConfirmPassword.getText().toString();

            if (TextUtils.isEmpty(oldPwd)){
                lytOldPassword.setError("old password is empty!");
                lytOldPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(newPwd)){
                lytNewPassword.setError("new password is empty!");
                lytNewPassword.requestFocus();
                return;
            }

            if (TextUtils.isEmpty(confPwd)){
                lytConfirmPassword.setError("confirm password is empty!");
                lytConfirmPassword.requestFocus();
                return;
            }

            if (!newPwd.equals(confPwd)){
                lytConfirmPassword.setError("confirm password does not match!");
                lytConfirmPassword.requestFocus();
                return;
            }

            progressContainer.setVisibility(View.VISIBLE);
            userViewModel.changePassword(oldPwd, newPwd, confPwd).observe(this, result ->{
                if (result == null || result.isLoading()) return;

                if (result.isSuccessful()){
                    ChangePasswordDFragment.this.dismiss();
                    Toast.makeText(getContext(), "Password Change Successfully.", Toast.LENGTH_SHORT).show();
                }else {
                    progressContainer.setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Password Change Failed.", Toast.LENGTH_SHORT).show();
                }
            });

        });
    }

}
