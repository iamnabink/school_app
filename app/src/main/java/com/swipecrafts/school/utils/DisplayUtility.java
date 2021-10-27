package com.swipecrafts.school.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Madhusudan Sapkota on 3/6/2018.
 */

public class DisplayUtility {

    public static int getScreenWidth(@NonNull Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.x;
    }

    public static int getScreenHeight(@NonNull Context context) {
        Point size = new Point();
        ((Activity) context).getWindowManager().getDefaultDisplay().getSize(size);
        return size.y;
    }

    public static boolean isInLandscapeMode(@NonNull Context context) {
        boolean isLandscape = false;
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
        }
        return isLandscape;
    }

    public static int dipToPixels(Context context, int dp) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm != null ? wm.getDefaultDisplay() : null;

        DisplayMetrics displaymetrics = new DisplayMetrics();
        if (display != null) {
            display.getMetrics(displaymetrics);
        }

        return (int) (dp * displaymetrics.density + 0.5f);
    }

    public static int spToPixels(Context context, float sp) {
        return (int) (sp * context.getResources().getDisplayMetrics().scaledDensity);
    }
}