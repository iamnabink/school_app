package com.swipecrafts.school.ui.login;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.transition.TransitionManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.swipecrafts.school.R;
import com.swipecrafts.school.SchoolApplication;
import com.swipecrafts.school.data.manager.Status;
import com.swipecrafts.school.data.model.api.UserType;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.ui.driver.DriverDashboard;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.UserViewModel;

import javax.inject.Inject;

/**
 * Created by Madhusudan Sapkota on 7/1/2018.
 */
public class LoginDialogFragment extends DialogFragment implements View.OnClickListener {
    public static final String ARG_LOGIN_TYPE = "LOGIN_TYPE_FROM";
    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private String userLoginType;
    private Toolbar toolbar;
    private UserViewModel userViewModel;

    private TextInputLayout userNameTIL;
    private TextInputLayout passwordTIL;
    private EditText edtEmail;
    private EditText edtPassword;

    private Button loginAsParent;
    private Button loginAsTeacher;
    private Button loginAsDriver;
    private RelativeLayout loginComponent;


    private LinearLayout dividerContainer;
    private Button primaryLoginButton;
    private Button secondaryLoginButton;
    private Button tertiaryLoginButton;
    private Animation shakeAnimation;

    public static LoginDialogFragment newInstance(UserType loginType) {
        Bundle args = new Bundle();
        args.putString(ARG_LOGIN_TYPE, loginType.getValue());
        LoginDialogFragment fragment = new LoginDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.LoginDialog);
        if (getArguments() != null) {
            userLoginType = getArguments().getString(ARG_LOGIN_TYPE);
        }

        ((SchoolApplication) getActivity().getApplication()).applicationComponent().inject(this);
        //Set up and subscribe (observe) to the ViewModel
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mainView = inflater.inflate(R.layout.activity_login, container, false);

        findView(mainView);
        init();
        return mainView;
    }

    public void findView(View view) {
        toolbar = (Toolbar) view.findViewById(R.id.loginToolbar);

        userNameTIL = (TextInputLayout) view.findViewById(R.id.userNameTIL);
        passwordTIL = (TextInputLayout) view.findViewById(R.id.passwordTIL);

        edtEmail = (EditText) view.findViewById(R.id.edtUserName);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);

        loginAsParent = (Button) view.findViewById(R.id.login_as_parent);
        loginAsTeacher = (Button) view.findViewById(R.id.login_as_teacher);
        loginAsDriver = (Button) view.findViewById(R.id.login_as_driver);

        loginComponent = (RelativeLayout) view.findViewById(R.id.login_components);
        dividerContainer = (LinearLayout) view.findViewById(R.id.divider_view);

        edtPassword.setTransformationMethod(new PasswordTransformationMethod());

        this.primaryLoginButton = loginAsParent;
        this.secondaryLoginButton = loginAsTeacher;
        this.tertiaryLoginButton = loginAsDriver;
    }


    public void init() {
        //Set navigation icon to back button drawable
        toolbar.setNavigationIcon(R.drawable.ic_left_arrow);
        toolbar.setNavigationOnClickListener(v ->{
            onBackPressed();
        });

        if (getDialog() != null) {
            getDialog().setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    onBackPressed();
                }
                return true;
            });
        }

        shakeAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.shake);

        loginAsParent.setOnClickListener(this);
        loginAsTeacher.setOnClickListener(this);
        loginAsDriver.setOnClickListener(this);

        if (userLoginType.equals(UserType.PARENT.getValue())) {
            loginAsTeacher.setVisibility(View.GONE);
            loginAsDriver.setVisibility(View.GONE);
            dividerContainer.setVisibility(View.GONE);

            Resources resource = getResources();
            String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.parent));
            loginAsParent.setText(loginAs);
        } else if (userLoginType.equals(UserType.TEACHER.getValue())) {
            loginAsParent.setVisibility(View.GONE);
            loginAsDriver.setVisibility(View.GONE);
            dividerContainer.setVisibility(View.GONE);

            Resources resource = getResources();
            String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.teacher));
            loginAsTeacher.setText(loginAs);
        } else if (userLoginType.equals(UserType.DRIVER.getValue())) {
            loginAsTeacher.setVisibility(View.GONE);
            loginAsParent.setVisibility(View.GONE);
            dividerContainer.setVisibility(View.GONE);

            Resources resource = getResources();
            String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.driver));
            loginAsDriver.setText(loginAs);
        } else {
            loginAsDriver.setVisibility(View.VISIBLE);
            loginAsTeacher.setVisibility(View.VISIBLE);
            loginAsParent.setVisibility(View.VISIBLE);
            dividerContainer.setVisibility(View.VISIBLE);

            Resources resource = getResources();
            String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.parent));
            loginAsParent.setText(loginAs);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.login_as_parent:
                if (primaryLoginButton == loginAsParent || userLoginType.equals(UserType.PARENT.getValue())) {
                    login(loginAsParent, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), UserType.PARENT);
                } else {
                    this.primaryLoginButton = loginAsParent;
                    this.secondaryLoginButton = loginAsTeacher;
                    this.tertiaryLoginButton = loginAsDriver;

                    Resources resource = getResources();
                    String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.parent));
                    this.loginAsParent.setText(loginAs);
                    this.loginAsTeacher.setText(R.string.teacher);
                    this.loginAsDriver.setText(R.string.driver);

                    animateSwitchLoginType();
                }
                break;
            case R.id.login_as_teacher:

                if (primaryLoginButton == loginAsTeacher || userLoginType.equals(UserType.TEACHER.getValue())) {
                    login(loginAsTeacher, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), UserType.TEACHER);
                } else {
                    this.primaryLoginButton = loginAsTeacher;
                    this.secondaryLoginButton = loginAsDriver;
                    this.tertiaryLoginButton = loginAsParent;


                    Resources resource = getResources();
                    String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.teacher));
                    this.loginAsParent.setText(R.string.parent);
                    this.loginAsTeacher.setText(loginAs);
                    this.loginAsDriver.setText(R.string.driver);

                    animateSwitchLoginType();
                }

                break;
            case R.id.login_as_driver:

                if (primaryLoginButton == loginAsDriver || userLoginType.equals(UserType.DRIVER.getValue())) {
                    login(loginAsDriver, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), UserType.DRIVER);
                } else {

                    this.primaryLoginButton = loginAsDriver;
                    this.secondaryLoginButton = loginAsParent;
                    this.tertiaryLoginButton = loginAsTeacher;


                    Resources resource = getResources();
                    String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.driver));

                    this.loginAsParent.setText(R.string.parent);
                    this.loginAsTeacher.setText(R.string.teacher);
                    this.loginAsDriver.setText(loginAs);

                    animateSwitchLoginType();
                }

                break;
        }
    }


    private void login(Button loginBtn, String userName, String password, UserType type) {
        userNameTIL.setError(null);
        passwordTIL.setError(null);

        if (TextUtils.isEmpty(userName)){
            userNameTIL.setError("username can not be empty!!");
            loginBtn.startAnimation(shakeAnimation);
            return;
        }

        if (TextUtils.isEmpty(password)){
            userNameTIL.setError("password can not be empty!!");
            loginBtn.startAnimation(shakeAnimation);
            return;
        }

        final CustomDialog pDialog = new CustomDialog(getContext(), CustomDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Login in...");
        pDialog.setCancelable(false);
        pDialog.show();

        String fcmKey = FirebaseInstanceId.getInstance().getToken();
        userViewModel.loginUser(userName, password, type, fcmKey).observe(this, result -> {
            if (result == null) return;

            if (result.status == Status.SUCCESS) {

                if (type == UserType.DRIVER){
                    showSuccessDialog(
                            pDialog,
                            "Great...",
                            "Login successful!",
                            getString(R.string.dialog_ok), CustomDialog -> {
                                pDialog.dismissWithAnimation();
                                LoginDialogFragment.this.dismiss();
                                startActivity(new Intent(getContext(), DriverDashboard.class));
                                getActivity().finish();
                            });
                }else{
                    if (type == UserType.TEACHER) {
                        LogUtils.infoLog("TeacherSubscription", "true");
                        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TEACHER_NOTIFICATION);
                    }
                    showSuccessDialog(
                            pDialog,
                            "Great...",
                            "Login successful!",
                            getString(R.string.dialog_ok), CustomDialog -> {
                                pDialog.dismissWithAnimation();
                                onBackPressed();
                            });
                }
            } else if (result.status == Status.ERROR) {
                String message = Utils.isOnline(getContext()) ? result.message + "" : Constants.NO_INTERNET_MESSAGE;

                showErrorDialog(
                        pDialog,
                        getString(R.string.error_dialog_title),
                        message,
                        getString(R.string.dialog_ok),
                        null);
            }
        });
    }

    private void animateSwitchLoginType() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            TransitionManager.beginDelayedTransition(loginComponent);
        }

        primaryLoginButton.setBackgroundColor(getResources().getColor(R.color.colorPrimaryButton));
        secondaryLoginButton.setBackgroundColor(getResources().getColor(R.color.colorSecondaryButton));
        tertiaryLoginButton.setBackgroundColor(getResources().getColor(R.color.colorSecondaryButton));

        RelativeLayout.LayoutParams primaryLytParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams secondaryLytParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams dividerLytParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        RelativeLayout.LayoutParams tertiaryLytParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        primaryLytParam.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        primaryLytParam.setMargins(0, 0, 0, 10);

        dividerLytParam.addRule(RelativeLayout.BELOW, primaryLoginButton.getId());
        dividerLytParam.setMargins(0, 10, 0, 10);

        secondaryLytParam.addRule(RelativeLayout.BELOW, R.id.divider_view);
        secondaryLytParam.setMargins(0, 0, 0, 20);

        tertiaryLytParam.addRule(RelativeLayout.BELOW, secondaryLoginButton.getId());
        tertiaryLytParam.setMargins(0, 0, 0, 20);

        primaryLoginButton.setLayoutParams(primaryLytParam);
        dividerContainer.setLayoutParams(dividerLytParam);
        secondaryLoginButton.setLayoutParams(secondaryLytParam);
        tertiaryLoginButton.setLayoutParams(tertiaryLytParam);
    }


    private void showSuccessDialog(CustomDialog prevDialog, String title, String message, String btnMessage, CustomDialog.OnSweetClickListener listener) {

        CustomDialog dialog = prevDialog;

        if (dialog == null) {
            dialog = new CustomDialog(getContext(), CustomDialog.SUCCESS_TYPE)
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText(btnMessage)
                    .setConfirmClickListener(listener);
            dialog.setCancelable(true);
            dialog.show();
        } else {
            dialog
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText(btnMessage)
                    .setConfirmClickListener(listener)
                    .changeAlertType(CustomDialog.SUCCESS_TYPE)
                    .setCancelable(true);
        }

    }

    private void showErrorDialog(CustomDialog prevDialog, String title, String message, String btnMessage, CustomDialog.OnSweetClickListener listener) {
        CustomDialog dialog = prevDialog;

        if (dialog == null) {
            dialog = new CustomDialog(getContext(), CustomDialog.ERROR_TYPE)
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText(btnMessage)
                    .setConfirmClickListener(listener);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText(btnMessage)
                    .setConfirmClickListener(listener)
                    .changeAlertType(CustomDialog.ERROR_TYPE);
        }

    }

    private void onBackPressed(){
        Fragment fragment = getTargetFragment();
        if (fragment != null) getTargetFragment().onActivityResult(getTargetRequestCode(), 0, new Intent());
        LoginDialogFragment.this.dismiss();
    }

}
