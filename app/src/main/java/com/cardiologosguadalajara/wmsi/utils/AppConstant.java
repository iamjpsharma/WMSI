package com.cardiologosguadalajara.wmsi.utils;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import com.cardiologosguadalajara.wmsi.BuildConfig;

public class AppConstant {
    public static final String PATIENT_NAME = "patientName";
    public static final String WMSI_VALUE = "WMSIValue";
    public static final String LVEF_VALUE = "LVEFValue";
    public static final String SPIN_PATH = "spinPath";
    public static final String PREF_KEY_EXAMINER = "PrefKeyExaminer";
    public static final String EXAMINER_VALUE = "ExaminerValue";
    public static final String PHONE_VALUE = "PhoneValue";
    public static final String EMAIL_VALUE = "EmailValue";
    public static final String ADDRESS_VALUE = "AddressValue";
    public static final String PDF_FILE_URI = "PDF_FILE_URI";
    public static final String PREF_KEY_REPORT ="PREF_KEY_REPORT" ;
    public static final String SIGNATURE_PATH = "SIGNATURE_PATH";
    public static final String LOGO_PATH = "LOGO_PATH";
    public static final String DATE_VALUE = "dateVal";
    public static final String IS_TERMS = "isTerms";
    public static final String PROVIDER = BuildConfig.APPLICATION_ID + ".provider";
    public static final String REPORT_PATH = "ReportPath";
    public static final String PREF_SUBSCRIBE = "PrefSubscribe";
    public static final String IS_SUBSCRIBE = "IsSubscribe";


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if (inputMethodManager.isAcceptingText()) {
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

}
