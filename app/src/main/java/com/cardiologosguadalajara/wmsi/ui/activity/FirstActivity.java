package com.cardiologosguadalajara.wmsi.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.google.firebase.analytics.FirebaseAnalytics;

public class FirstActivity extends AppCompatActivity {


    private FirebaseAnalytics mFirebaseAnalytics;


    private SharedPreferences mPrefSubscribe;


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.alerttile))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.alertYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        FirstActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.alertNo), null)
                .show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, "5");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FirstActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        // Have to remove for release. This is for debugging
        mPrefSubscribe = getSharedPreferences(AppConstant.PREF_SUBSCRIBE, MODE_PRIVATE);
        mPrefSubscribe.edit().putBoolean(AppConstant.IS_SUBSCRIBE, true).apply();

        boolean isSubscribe = mPrefSubscribe.getBoolean(AppConstant.IS_SUBSCRIBE, false);
        startActivity(new Intent(FirstActivity.this, MainActivity.class));
        finish();
        if (isSubscribe) {
            startActivity(new Intent(FirstActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(FirstActivity.this, SubscriptionActivity.class));
            finish();
        }

    }
}