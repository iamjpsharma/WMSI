package com.cardiologosguadalajara.wmsi;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import java.util.Locale;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        App.context = getApplicationContext();

        if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Spanish")) {
            setAppLocale("es");
        } else if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Chinese")) {
            setAppLocale("zh");
        } else {
            setAppLocale("en");
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Spanish")) {
            setAppLocale("es");
        } else if (Locale.getDefault().getDisplayLanguage().equalsIgnoreCase("Chinese")) {
            setAppLocale("zh");
        } else {
            setAppLocale("en");
        }
    }

    public static void setAppLocale(String localeCode) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(new Locale(localeCode.toLowerCase()));
        } else {
            config.locale = new Locale(localeCode.toLowerCase());
        }
        resources.updateConfiguration(config, dm);
    }

    public static Context getAppContext() {
        return App.context;
    }
}

