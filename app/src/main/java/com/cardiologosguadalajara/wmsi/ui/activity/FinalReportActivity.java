package com.cardiologosguadalajara.wmsi.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pdfview.PDFView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FinalReportActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;

    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.ib_back)
    ImageButton ib_back;
    @BindView(R.id.ib_share)
    ImageButton ib_share;
    private File filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, "4");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "FinalReportActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);


        setContentView(R.layout.activity_final_report
        );

        ButterKnife.bind(this);

        filePath = new File(getIntent().getStringExtra(AppConstant.REPORT_PATH));

        pdfView.fromFile(filePath).show();

        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ib_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.setType("application/pdf");

                Uri uri = FileProvider.getUriForFile(FinalReportActivity.this, AppConstant.PROVIDER, filePath);

                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(intent, "Share"));
            }
        });

    }
}