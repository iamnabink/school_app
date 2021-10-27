package com.swipecrafts.library.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.DisplayUtility;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Madhusudan Sapkota on 6/4/2018.
 */
public class ErrorView extends LinearLayout {

    public static final int ERROR = 1;
    public static final int ERROR_WITH_BUTTON = 2;
    public static final int PROGRESS = 3;


    private @ViewType
    int viewType;
    private String btnText = "RETRY";
    private String errorTitleText = "No Internet Connection!";
    private String errorDescriptionText = "";
    private int errorTitleTextSize;
    private int errorDescriptionTextSize;
    private @DrawableRes
    int errorIconResource = R.drawable.ic_circle_error;
    private int errorIconSize = 60;

    private LinearLayout errorLyt;
    private LinearLayout progressLyt;


    private ProgressBar progressBar;
    private Button retryBtn;
    private ImageView errorIcon;
    private TextView errorLabel;
    private TextView errorDescription;
    private OnClickListener retryListener;

    public ErrorView(Context context) {
        super(context);
        init(context, null);
    }


    public ErrorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public void init(Context context, @Nullable AttributeSet attrs) {

        errorTitleTextSize = DisplayUtility.spToPixels(getContext(), 16);
        errorDescriptionTextSize = DisplayUtility.spToPixels(getContext(), 14);

        loadComponent(context, attrs);

        setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setOrientation(VERTICAL);
        setWeightSum(1);


        errorLyt = new LinearLayout(context);
        errorLyt.setOrientation(VERTICAL);
        errorLyt.setGravity(Gravity.CENTER);

        errorIcon = new ImageView(context);
        errorLyt.addView(errorIcon, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));


        errorLabel = new TextView(context);
        errorLabel.setGravity(Gravity.CENTER);
        errorLabel.setPadding(10, 5, 10, 5);
        errorLyt.addView(errorLabel, new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        errorDescription = new TextView(context);
        errorDescription.setGravity(Gravity.CENTER);
        errorDescription.setPadding(15, 5, 15, 5);
        errorLyt.addView(errorDescription, new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        retryBtn = new Button(new ContextThemeWrapper(context, R.style.ColorButton), null, 0);
        retryBtn.setText(btnText);
        errorLyt.addView(retryBtn, new LayoutParams(LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        errorLyt.setVisibility(GONE);
        addView(errorLyt, new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        progressLyt = new LinearLayout(context);
        progressLyt.setOrientation(VERTICAL);
        progressLyt.setGravity(Gravity.CENTER);
        progressBar = new ProgressBar(context);
        progressLyt.addView(progressBar, new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        progressLyt.setVisibility(GONE);
        addView(progressLyt, new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        retryBtn.setOnClickListener(v -> {
            if (retryListener != null){
                retryListener.onClick(v);
                changeType(PROGRESS);
            }
        });
        apply();
    }

    private void loadComponent(Context context, @Nullable AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ErrorView);
        try {
            setViewType(
                    a.getInteger(R.styleable.ErrorView_viewType, ERROR)
            );

            setButtonText(
                    a.getString(R.styleable.ErrorView_buttonText)
            );

            setErrorTitle(
                    a.getString(R.styleable.ErrorView_errorTitle)
            );

            setErrorDescription(
                    a.getString(R.styleable.ErrorView_errorDescription)
            );

            setErrorDescriptionTextSize(
                    a.getDimensionPixelSize(R.styleable.ErrorView_errorTitleTextSize, errorTitleTextSize)
            );

            setErrorDescriptionTextSize(
                    a.getDimensionPixelSize(R.styleable.ErrorView_errorDescTextSize, errorDescriptionTextSize)
            );

            setErrorIcon(
                    a.getResourceId(R.styleable.ErrorView_errorIcon, errorIconResource)
            );
        } finally {
            a.recycle();
        }
    }

    public void changeType(@ViewType int type) {
        this.viewType = type;
        switch (type) {
            case ERROR:
                progressLyt.setVisibility(GONE);
                retryBtn.setVisibility(GONE);
                errorLyt.setVisibility(VISIBLE);
                break;
            case ERROR_WITH_BUTTON:
                progressLyt.setVisibility(GONE);
                retryBtn.setVisibility(VISIBLE);
                errorLyt.setVisibility(VISIBLE);
                break;
            case PROGRESS:
                progressLyt.setVisibility(VISIBLE);
                retryBtn.setVisibility(GONE);
                errorLyt.setVisibility(GONE);
                break;
        }
    }

    public ErrorView setOnRetryListener(OnClickListener listener) {
        this.retryListener = listener;
//        retryBtn.setOnClickListener(listener);
        return this;
    }

    public ErrorView setErrorTitle(String title) {
        this.errorTitleText = title == null ? getResources().getString(R.string.no_internet_message) : title;
        return this;
    }

    public ErrorView setErrorDescription(String errorDescription) {
        this.errorDescriptionText = errorDescription;
        return this;
    }

    public ErrorView setErrorIcon(int errorIcon) {
        this.errorIconResource = errorIcon;
        return this;
    }

    public ErrorView setErrorTitleTextSize(int errorTitleTextSize) {
        this.errorTitleTextSize = errorTitleTextSize;
        return this;
    }

    public ErrorView setErrorDescriptionTextSize(int errorDescriptionTextSize) {
        this.errorDescriptionTextSize = errorDescriptionTextSize;
        return this;
    }

    public ErrorView setErrorIconSize(int size) {
        this.errorIconSize = size;
        return this;
    }

    public ErrorView setViewType(@ViewType int viewType) {
        this.viewType = viewType;
        return this;
    }

    public ErrorView setButtonText(String btnText) {
        this.btnText = btnText == null ? getResources().getString(R.string.dialog_retry) : btnText;
        return this;
    }

    public void apply() {
        setBackgroundColor(Color.WHITE);
        try {errorIcon.setBackgroundResource(errorIconResource);}
        catch (NullPointerException e){
            errorIcon.setBackgroundResource(0);
        }
        errorIcon.setContentDescription(errorTitleText);
        int size = DisplayUtility.dipToPixels(getContext(), errorIconSize);
        errorIcon.setMinimumHeight(size);
        errorIcon.setMinimumWidth(size);


        errorLabel.setTextColor(getResources().getColor(R.color.colorPrimaryText));
        errorLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorTitleTextSize);
        errorLabel.setText(errorTitleText);

        errorDescription.setTextColor(getResources().getColor(R.color.colorSecondaryText));
        errorDescription.setTextSize(TypedValue.COMPLEX_UNIT_PX, errorDescriptionTextSize);
        errorDescription.setText(errorDescriptionText);

        if (errorDescriptionText == null) errorDescription.setVisibility(GONE);
        changeType(viewType);
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({ERROR, ERROR_WITH_BUTTON, PROGRESS})
    public @interface ViewType {
    }

    class LayoutParams extends LinearLayout.LayoutParams {

        public LayoutParams(int width, int height, float weight) {
            super(width, height, weight);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
