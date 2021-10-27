package com.swipecrafts.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.listener.ImageLoader;

/**
 * Created by Madhusudan Sapkota on 2/26/2018.
 */

public class CustomNotificationDialog {

    private final ImageView timeIV;
    private String title = "", message = "", closeButtonText = "", conditionHeaderText = "Parent availability :", conditionText = "", timeText = "", fileLink;
    private String uri;
    private Spanned spMessage;

    private TextView titleTV;
    private TextView messageTV;
    private TextView conditionHeaderTV;
    private TextView conditionTV;
    private TextView timeTV;
    private LinearLayout fileLinkLyt;
    private TextView fileLinkTV;

    private LinearLayout conditionLayout;

    private ImageView dialogImage;

    private ImageButton dialogCloseBtn;

    private DialogListener imageListener;
    private boolean conditionLayoutVisibility = true;

    private int dialogIconResource = R.drawable.ic_notifications;

    private Dialog dialog;

    private boolean cancel = true;
    private ImageLoader<String> imageLoader = null;


    public CustomNotificationDialog(@NonNull Context context, int themeResId) {
        this.dialog = new Dialog(context, themeResId);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.dialog.setCancelable(cancel);
        this.dialog.setContentView(R.layout.custom_dialog);

        titleTV = (TextView) dialog.findViewById(R.id.dialogTitleTV);
        messageTV = (TextView) dialog.findViewById(R.id.dialogMessageTV);
        conditionHeaderTV = (TextView) dialog.findViewById(R.id.conditionTypeHeaderTV);
        conditionTV = (TextView) dialog.findViewById(R.id.conditionTV);
        timeTV = (TextView) dialog.findViewById(R.id.dialogTimeTV);
        timeIV = (ImageView) dialog.findViewById(R.id.timeIV);
        dialogImage = (ImageView) dialog.findViewById(R.id.dialogImage);

        fileLinkLyt = (LinearLayout) dialog.findViewById(R.id.fileLinkLyt);
        fileLinkTV = (TextView) dialog.findViewById(R.id.fileLinkTV);
        fileLinkTV.setPaintFlags(fileLinkTV.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        dialogCloseBtn = (ImageButton) dialog.findViewById(R.id.dialogCloseBtn);

        conditionLayout = (LinearLayout) dialog.findViewById(R.id.conditionLayout);

        dialogCloseBtn.setOnClickListener(btn -> dialog.dismiss());
    }


    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage(Spanned message) {
        this.spMessage = message;
    }

    public void setCloseBtnText(String closeBtnText) {
        this.closeButtonText = closeBtnText;
    }

    public void setConditionVisibility(boolean visibility) {
        this.conditionLayoutVisibility = visibility;
    }

    public void setConditionHeader(String conditionHeaderText) {
        this.conditionHeaderText = conditionHeaderText;
    }

    public void setCondition(String conditionText) {
        this.conditionText = conditionText;
    }

    public void setConditionTextColor(int color) {
        conditionTV.setTextColor(color);
    }

    public void setTime(String time) {
        this.timeText = time;
    }

    public void setImageListener(DialogListener imageListener) {
        this.imageListener = null;
        this.imageListener = imageListener;
    }

    public void setDialogIconResource(@DrawableRes int iconResource) {
        this.dialogIconResource = iconResource;
    }

    public void setImageUri(String uri) {
        this.uri = uri;
    }

    public void setImageLoader(ImageLoader<String> imageLoader) {
        this.imageLoader = imageLoader;
    }

    public void setFileLink(String link) {
        this.fileLink = link;
    }

    public void apply() {
        titleTV.setText(title);
        messageTV.setText(TextUtils.isEmpty(message) && spMessage != null ? spMessage : message);
        conditionHeaderTV.setText(conditionHeaderText);
        conditionTV.setText(conditionText);
        timeTV.setText(timeText);
        timeIV.setImageResource(dialogIconResource);

        if (imageLoader != null && uri != null) {
            imageLoader.loadImage(dialogImage, uri);
            dialogImage.setVisibility(View.VISIBLE);
        } else {
            dialogImage.setVisibility(View.GONE);
        }

        if (fileLink == null || TextUtils.isEmpty(fileLink)) {
            fileLinkLyt.setVisibility(View.GONE);
        } else {
            fileLinkLyt.setVisibility(View.VISIBLE);
            fileLinkTV.setText(fileLink);
        }

        conditionLayout.setVisibility(conditionLayoutVisibility ? View.VISIBLE : View.GONE);

        dialogImage.setOnClickListener(v -> {
            if (imageListener != null) imageListener.onClicked();
        });
    }

    public void show() {
        if (!dialog.isShowing()) dialog.show();
    }

    public void hide() {
        if (dialog.isShowing()) dialog.hide();
    }

    public void dismiss() {
        if (dialog.isShowing()) dialog.dismiss();
    }

    public boolean isShowing() {
        return dialog.isShowing();
    }

    public interface DialogListener {
        void onClicked();
    }
}
