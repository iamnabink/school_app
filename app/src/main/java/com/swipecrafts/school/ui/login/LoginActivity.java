package com.swipecrafts.school.ui.login;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.TransitionManager;
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
import com.swipecrafts.school.ui.base.BaseActivity;
import com.swipecrafts.school.ui.driver.DriverDashboard;
import com.swipecrafts.school.utils.Constants;
import com.swipecrafts.school.utils.LogUtils;
import com.swipecrafts.library.preetydialog.CustomDialog;
import com.swipecrafts.school.utils.Utils;
import com.swipecrafts.school.viewmodel.UserViewModel;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {

    public static final String ARG_LOGIN_FROM = "LOGIN_CLICKED_FROM";

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private UserViewModel userViewModel;

    private Toolbar toolbar;
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

//    private String navigateTo = DashboardFragment.class.getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        navigateTo = getIntent().getStringExtra(ARG_LOGIN_FROM);
        ((SchoolApplication) getApplication()).applicationComponent().inject(this);
        //Set up and subscribe (observe) to the ViewModel
        userViewModel = ViewModelProviders.of(this, viewModelFactory).get(UserViewModel.class);

        findView();
        init();
    }


    public void findView() {
        toolbar = (Toolbar) findViewById(R.id.loginToolbar);

        userNameTIL = (TextInputLayout) findViewById(R.id.userNameTIL);
        passwordTIL = (TextInputLayout) findViewById(R.id.passwordTIL);

        edtEmail = (EditText) findViewById(R.id.edtUserName);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        loginAsParent = (Button) findViewById(R.id.login_as_parent);
        loginAsTeacher = (Button) findViewById(R.id.login_as_teacher);
        loginAsDriver = (Button) findViewById(R.id.login_as_driver);

        loginComponent = (RelativeLayout) findViewById(R.id.login_components);
        dividerContainer = (LinearLayout) findViewById(R.id.divider_view);

        this.primaryLoginButton = loginAsParent;
        this.secondaryLoginButton = loginAsTeacher;
        this.tertiaryLoginButton = loginAsDriver;
    }


    public void init() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);

        shakeAnimation = AnimationUtils.loadAnimation(this, R.anim.shake);

        loginAsParent.setOnClickListener(this);
        loginAsTeacher.setOnClickListener(this);
        loginAsDriver.setOnClickListener(this);

        String userLoginType = getIntent().getStringExtra(ARG_LOGIN_FROM);



        if (UserType.PARENT.getValue().equals(userLoginType)) {
            loginAsTeacher.setVisibility(View.GONE);
            loginAsDriver.setVisibility(View.GONE);
            dividerContainer.setVisibility(View.GONE);

            Resources resource = getResources();
            String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.parent));
            loginAsParent.setText(loginAs);
        } else if (UserType.TEACHER.getValue().equals(userLoginType)) {
            loginAsParent.setVisibility(View.GONE);
            loginAsDriver.setVisibility(View.GONE);
            dividerContainer.setVisibility(View.GONE);

            Resources resource = getResources();
            String loginAs = String.format(resource.getString(R.string.login_as), resource.getString(R.string.teacher));
            loginAsTeacher.setText(loginAs);
        } else if (UserType.DRIVER.getValue().equals(userLoginType)) {
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

                if (primaryLoginButton == loginAsParent) {
                    login(loginAsParent, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), UserType.PARENT);
                } else {
                    this.primaryLoginButton = loginAsParent;
                    this.secondaryLoginButton = loginAsTeacher;
                    this.tertiaryLoginButton = loginAsDriver;

                    this.loginAsParent.setText("Login as Parent");
                    this.loginAsTeacher.setText("Teacher");
                    this.loginAsDriver.setText("Driver");

                    animateSwitchLoginType();
                }

                break;
            case R.id.login_as_teacher:

                if (primaryLoginButton == loginAsTeacher) {
                    login(loginAsTeacher, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), UserType.TEACHER);
                } else {
                    this.primaryLoginButton = loginAsTeacher;
                    this.secondaryLoginButton = loginAsDriver;
                    this.tertiaryLoginButton = loginAsParent;

                    this.loginAsTeacher.setText("Login as Teacher");
                    this.loginAsDriver.setText("Driver");
                    this.loginAsParent.setText("Parent");

                    animateSwitchLoginType();
                }

                break;
            case R.id.login_as_driver:

                if (primaryLoginButton == loginAsDriver) {
                    login(loginAsDriver, edtEmail.getText().toString().trim(), edtPassword.getText().toString().trim(), UserType.DRIVER);
                } else {

                    this.primaryLoginButton = loginAsDriver;
                    this.secondaryLoginButton = loginAsParent;
                    this.tertiaryLoginButton = loginAsTeacher;

                    this.loginAsDriver.setText("Login as Driver");
                    this.loginAsParent.setText("Parent");
                    this.loginAsTeacher.setText("Teacher");

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

        final CustomDialog pDialog = new CustomDialog(this, CustomDialog.PROGRESS_TYPE);
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
                            "Hello " + userName,
                            "You have successfully logged in.",
                            getString(R.string.dialog_ok), CustomDialog -> {
                                pDialog.dismissWithAnimation();
                                startActivity(new Intent(this, DriverDashboard.class));
                                finish();
                            });
                }else{
                    if (type == UserType.TEACHER) {
                        LogUtils.infoLog("TeacherSubscription", "true");
                        FirebaseMessaging.getInstance().subscribeToTopic(Constants.TEACHER_NOTIFICATION);
                    }
                    showSuccessDialog(
                            pDialog,
                            "Hello " + userName,
                            "You have successfully logged in.",
                            getString(R.string.dialog_ok), CustomDialog -> {
                                pDialog.dismissWithAnimation();
                                finish();
                            });
                }
            } else if (result.status == Status.ERROR) {
                String message = Utils.isOnline(this) ? result.message + "" : Constants.NO_INTERNET_MESSAGE;

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
