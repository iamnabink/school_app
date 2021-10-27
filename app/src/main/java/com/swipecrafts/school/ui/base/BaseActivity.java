package com.swipecrafts.school.ui.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.swipecrafts.library.preetydialog.CustomDialog;

/**
 * Created by Madhusudan Sapkota on 2/21/2018.
 */

public class BaseActivity extends AppCompatActivity {

    protected void replaceStateLossFragment(int resource, Fragment fragment, String tag){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resource, fragment, tag)
                .commitAllowingStateLoss();
    }

    protected void replaceFragment(int resource, Fragment fragment, String tag, String backStackKey){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(resource, fragment, tag)
                .addToBackStack(backStackKey)
                .commit();
    }

    protected CustomDialog showProgressDialog(CustomDialog prevDialog, String title) {
        CustomDialog dialog = prevDialog;

        if (dialog == null) {
            dialog = new CustomDialog(this, CustomDialog.PROGRESS_TYPE).setTitleText(title);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog
                    .setTitleText(title)
                    .changeAlertType(CustomDialog.PROGRESS_TYPE);
        }
        return dialog;
    }

    protected CustomDialog showErrorDialog(CustomDialog prevDialog, String title, String message, String btnMessage, CustomDialog.OnSweetClickListener listener) {
        CustomDialog dialog = prevDialog;

        if (dialog == null) {
            dialog = new CustomDialog(this, CustomDialog.ERROR_TYPE)
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

        return dialog;
    }

    protected CustomDialog showWarningDialog(CustomDialog prevDialog, String title, String message, String btnMessage) {

        CustomDialog dialog = prevDialog;

        if (dialog == null) {
            dialog = new CustomDialog(this, CustomDialog.WARNING_TYPE)
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText(btnMessage)
                    .setConfirmClickListener(null);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            dialog
                    .setTitleText(title)
                    .setContentText(message)
                    .setConfirmText(btnMessage)
                    .setConfirmClickListener(null)
                    .changeAlertType(CustomDialog.WARNING_TYPE);
        }

        return dialog;
    }

    protected CustomDialog showSuccessDialog(CustomDialog prevDialog, String title, String message, String btnMessage) {
        return this.showSuccessDialog(prevDialog, title, message, btnMessage, null);
    }

    protected CustomDialog showSuccessDialog(CustomDialog prevDialog, String title, String message, String btnMessage, CustomDialog.OnSweetClickListener listener) {

        CustomDialog dialog = prevDialog;

        if (dialog == null) {
            dialog = new CustomDialog(this, CustomDialog.SUCCESS_TYPE)
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
                    .changeAlertType(CustomDialog.SUCCESS_TYPE);
        }

        return dialog;
    }
}
