package com.swipecrafts.school.utils.locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

/**
 * Created by Madhusudan Sapkota on 7/2/2018.
 */

public class LocaleUtils {
    private static final String SELECTED_LANGUAGE = "Application_Language";

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ENGLISH, NEPALI})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = {ENGLISH, NEPALI};
    }

    public static final String ENGLISH = "en";
    public static final String NEPALI = "ne";

    public static void initialize(Context context) {
        String language = getPersistedData(context, Locale.getDefault().getLanguage());
        setLocale(context, language);
    }

    public static Context initialize(Context context, @LocaleDef String defaultLanguage) {
        String language = getPersistedData(context, defaultLanguage);
        return setLocale(context, language);
    }

    public static String getLanguage(Context context) {
        return getPersistedData(context, Locale.getDefault().getLanguage());
    }

    public static Context setLocale(Context context, @LocaleDef String language) {
        persist(context, language);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            return updateResources(context, language);
        }
        return updateResourcesLegacy(context, language);
    }

    public static Context setLocale(Context context, int languageIndex) {
        if (languageIndex >= LocaleDef.SUPPORTED_LOCALES.length) {
            return setLocale(context, ENGLISH);
        }
        String language = LocaleDef.SUPPORTED_LOCALES[languageIndex];
        persist(context, language);
        return setLocale(context, language);
    }

    private static String getPersistedData(Context context, String defaultLanguage) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(SELECTED_LANGUAGE, defaultLanguage);
    }

    private static void persist(Context context, String language) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString(SELECTED_LANGUAGE, language).apply();
    }

    @TargetApi(Build.VERSION_CODES.N)
    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        configuration.setLayoutDirection(locale);

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        return context.createConfigurationContext(configuration);
    }

    private static Context updateResourcesLegacy(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources resources = context.getResources();

        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1){
            configuration.setLayoutDirection(locale);
        }

        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        return context;
    }
}