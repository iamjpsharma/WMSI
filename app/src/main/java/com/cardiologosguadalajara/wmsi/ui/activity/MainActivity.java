package com.cardiologosguadalajara.wmsi.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cardiologosguadalajara.wmsi.BuildConfig;
import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.databinding.ActivityMainBinding;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.List;
import java.util.Objects;

import me.zhanghai.android.materialratingbar.MaterialRatingBar;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final int WRITE_STORAGE_REQUEST = 111;
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;

    SharedPreferences mPrefSubscribe;
    private boolean isSubscribe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, "2");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "MainActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mPrefSubscribe = getSharedPreferences(AppConstant.PREF_SUBSCRIBE, MODE_PRIVATE);
        isSubscribe = mPrefSubscribe.getBoolean(AppConstant.IS_SUBSCRIBE, false);




        askPermission();

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        NavigationView view = findViewById(R.id.nav_view);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDevice = size.x;

        view.getLayoutParams().width = (int) (widthDevice / 1.6);

        ImageButton ib_close = view.findViewById(R.id.ib_close);
        ConstraintLayout cl_info = view.findViewById(R.id.cl_info);
        ConstraintLayout cl_report = view.findViewById(R.id.cl_report);
        ConstraintLayout cl_contact = view.findViewById(R.id.cl_contact);
        ConstraintLayout cl_refer_friend = view.findViewById(R.id.cl_refer_friend);
        ConstraintLayout cl_feedback = view.findViewById(R.id.cl_feedback);
        TextView tv_version = view.findViewById(R.id.tv_version);

        String versionName = BuildConfig.VERSION_NAME;
        tv_version.setText("Version " + versionName);

        cl_report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSubscribe) {
                    Intent intent = new Intent(MainActivity.this, ReportListActivity.class);
                    startActivity(intent);

                }
            }
        });


        ib_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawers();
            }
        });

        cl_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                startActivity(intent);
            }
        });

        cl_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareToGMail(new String[]{"info@cardiologosguadalajara.com.mx",}, "WMSI Android App Feedback", "");
            }
        });

        cl_refer_friend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareImageWithAllApp();
            }
        });

        cl_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoxForFeedback(MainActivity.this);
            }
        });

    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage(getResources().getString(R.string.alerttile))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.alertYes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        MainActivity.super.onBackPressed();
                    }
                })
                .setNegativeButton(getResources().getString(R.string.alertNo), null)
                .show();
    }

    public void shareToGMail(String[] email, String subject, String content) {

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL, email);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.setType("text/html");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        final PackageManager pm = getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(emailIntent);

    }

    private void shareImageWithAllApp() {
        Intent ShareIntent = new Intent();
        ShareIntent.setAction(Intent.ACTION_SEND);
        ShareIntent.setType("text/*");
        String link = "https://play.google.com/store/apps/details?id=com.cardiologosguadalajara.wmsi";
        ShareIntent.putExtra(Intent.EXTRA_TEXT, link);
        startActivity(Intent.createChooser(ShareIntent, "Share"));
    }

    public static void rateApp(Context context) {
        Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details", context);
        context.startActivity(rateIntent);
    }

    public static Intent rateIntentForUrl(String url, Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, context.getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
    }

    @AfterPermissionGranted(WRITE_STORAGE_REQUEST)
    private void askPermission() {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {

        } else {
            EasyPermissions.requestPermissions(this, "This app may not work correctly without the requested permission.!",
                    WRITE_STORAGE_REQUEST, perms);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AppConstant.hideSoftKeyboard(MainActivity.this);
        return super.dispatchTouchEvent(ev);
    }

    public static void DialogBoxForFeedback(Context context) {
        Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDevice = size.x;
        int heightDevice = size.y;

        TextView tv_notnow;
        MaterialRatingBar ratingBar;
        ConstraintLayout cl_notnow, cl_option;
        TextView tv_submit, tv_cancel;

        Dialog mDialog = new Dialog(context);
        try {
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception ignored) {
        }
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_feedback);

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        tv_notnow = mDialog.findViewById(R.id.tv_notnow);
        cl_notnow = mDialog.findViewById(R.id.cl_notnow);
        cl_option = mDialog.findViewById(R.id.cl_option);
        ratingBar = mDialog.findViewById(R.id.ratingBar);
        tv_submit = mDialog.findViewById(R.id.tv_submit);
        tv_cancel = mDialog.findViewById(R.id.tv_cancel);

        ratingBar.setOnRatingChangeListener(new MaterialRatingBar.OnRatingChangeListener() {
            @Override
            public void onRatingChanged(MaterialRatingBar ratingBar, float rating) {
                if (rating > 0) {
                    cl_notnow.setVisibility(View.GONE);
                    cl_option.setVisibility(View.VISIBLE);
                }
            }
        });

        tv_notnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        tv_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateApp(context);
                mDialog.dismiss();
            }
        });

        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            mDialog.getWindow().setLayout((6 * width) / 7, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (Exception e) {
        }
    }

}