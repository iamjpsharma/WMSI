package com.cardiologosguadalajara.wmsi.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cardiologosguadalajara.wmsi.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.pdfview.PDFView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomSheetPrivacyDialog extends BottomSheetDialogFragment {
    public static BottomSheetPrivacyDialog getInstance() {
        return new BottomSheetPrivacyDialog();
    }

    @BindView(R.id.pdfViewTerms)
    PDFView pdfViewTerms;
    @BindView(R.id.ib_close_terms)
    ImageButton ib_close_terms;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_layout, container, false);

        ButterKnife.bind(this, view);

        pdfViewTerms.fromAsset(getString(R.string.PRIVACY_POLICY)).show();
        ib_close_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        return view;
    }
}