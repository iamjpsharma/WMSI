package com.cardiologosguadalajara.wmsi.ui.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.pdfview.PDFView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TermsAndPolicyActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.pdfView)
    PDFView pdfView;
    @BindView(R.id.ib_back)
    ImageButton ib_back;
    private boolean isTerms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_policy);

        ButterKnife.bind(this);

        isTerms = getIntent().getBooleanExtra(AppConstant.IS_TERMS, false);
        if (isTerms) {
            pdfView.fromAsset(getString(R.string.TERMS_CONDITION)).show();
            tv_title.setText(getString(R.string.app_terms));
        } else {
            tv_title.setText(getString(R.string.app_policy));
            pdfView.fromAsset(getString(R.string.PRIVACY_POLICY)).show();

        }
        ib_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
}