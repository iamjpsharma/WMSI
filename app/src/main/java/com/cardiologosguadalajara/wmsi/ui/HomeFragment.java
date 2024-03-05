package com.cardiologosguadalajara.wmsi.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.databinding.FragmentHomeBinding;
import com.cardiologosguadalajara.wmsi.ui.activity.ReportActivity;
import com.cardiologosguadalajara.wmsi.ui.activity.SubscriptionActivity;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.cardiologosguadalajara.wmsi.utils.SharedInstance;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeFragment extends Fragment implements View.OnClickListener, View.OnTouchListener, View.OnLongClickListener {


    private FirebaseAnalytics mFirebaseAnalytics;

    private static final String PREF_KEY = "prefKey";
    private static final int PICK_IMAGE_SIGNATURE = 123;
    private static final int PICK_IMAGE = 111;
    @BindView(R.id.cl_circle)
    ConstraintLayout cl_circle;

    @BindView(R.id.cv_center_first)
    ConstraintLayout cv_center_first;
    @BindView(R.id.cv_center_sec)
    ConstraintLayout cv_center_sec;

    @BindView(R.id.cv_middle_first)
    ConstraintLayout cv_middle_first;
    @BindView(R.id.cv_middle_sec)
    ConstraintLayout cv_middle_sec;

    @BindView(R.id.cv_outer_first)
    ConstraintLayout cv_outer_first;
    @BindView(R.id.cv_outer_sec)
    ConstraintLayout cv_outer_sec;

    //Center
    @BindView(R.id.fm_center_1)
    FrameLayout fm_center_1;
    @BindView(R.id.fm_center_2)
    FrameLayout fm_center_2;
    @BindView(R.id.fm_center_3)
    FrameLayout fm_center_3;
    @BindView(R.id.fm_center_4)
    FrameLayout fm_center_4;

    @BindView(R.id.fm_center_1_)
    FrameLayout fm_center_1_;
    @BindView(R.id.fm_center_2_)
    FrameLayout fm_center_2_;
    @BindView(R.id.fm_center_3_)
    FrameLayout fm_center_3_;
    @BindView(R.id.fm_center_4_)
    FrameLayout fm_center_4_;

    @BindView(R.id.iv_center_1)
    ImageView iv_center_1;
    @BindView(R.id.iv_center_2)
    ImageView iv_center_2;
    @BindView(R.id.iv_center_3)
    ImageView iv_center_3;
    @BindView(R.id.iv_center_4)
    ImageView iv_center_4;

    @BindView(R.id.tv_center_1)
    TextView tv_center_1;
    @BindView(R.id.tv_center_2)
    TextView tv_center_2;
    @BindView(R.id.tv_center_3)
    TextView tv_center_3;
    @BindView(R.id.tv_center_4)
    TextView tv_center_4;

    int[] colors;
    String[] strValue;
    String[] strSubValue;
    String[] strTitleValue;

    float val_WMSI = 0;
    double val_LVEF = 0;


    int cir_center_1 = 0;
    int cir_center_2 = 0;
    int cir_center_3 = 0;
    int cir_center_4 = 0;

    int cir_middle_1 = 0;
    int cir_middle_2 = 0;
    int cir_middle_3 = 0;
    int cir_middle_4 = 0;
    int cir_middle_5 = 0;
    int cir_middle_6 = 0;

    int cir_outer_1 = 0;
    int cir_outer_2 = 0;
    int cir_outer_3 = 0;
    int cir_outer_4 = 0;
    int cir_outer_5 = 0;
    int cir_outer_6 = 0;

    //Middle
    @BindView(R.id.fm_middle_1)
    FrameLayout fm_middle_1;
    @BindView(R.id.fm_middle_1_1)
    FrameLayout fm_middle_1_1;
    @BindView(R.id.fm_middle_2)
    FrameLayout fm_middle_2;
    @BindView(R.id.fm_middle_2_1)
    FrameLayout fm_middle_2_1;
    @BindView(R.id.fm_middle_3)
    FrameLayout fm_middle_3;
    @BindView(R.id.fm_middle_3_1)
    FrameLayout fm_middle_3_1;
    @BindView(R.id.fm_middle_4)
    FrameLayout fm_middle_4;
    @BindView(R.id.fm_middle_4_1)
    FrameLayout fm_middle_4_1;
    @BindView(R.id.fm_middle_5)
    FrameLayout fm_middle_5;
    @BindView(R.id.fm_middle_5_1)
    FrameLayout fm_middle_5_1;
    @BindView(R.id.fm_middle_6)
    FrameLayout fm_middle_6;
    @BindView(R.id.fm_middle_6_1)
    FrameLayout fm_middle_6_1;

    @BindView(R.id.fm_middle_1_)
    FrameLayout fm_middle_1_;
    @BindView(R.id.fm_middle_2_)
    FrameLayout fm_middle_2_;
    @BindView(R.id.fm_middle_3_)
    FrameLayout fm_middle_3_;
    @BindView(R.id.fm_middle_4_)
    FrameLayout fm_middle_4_;
    @BindView(R.id.fm_middle_5_)
    FrameLayout fm_middle_5_;
    @BindView(R.id.fm_middle_6_)
    FrameLayout fm_middle_6_;

    @BindView(R.id.iv_middle_1)
    ImageView iv_middle_1;
    @BindView(R.id.iv_middle_2)
    ImageView iv_middle_2;
    @BindView(R.id.iv_middle_3)
    ImageView iv_middle_3;
    @BindView(R.id.iv_middle_4)
    ImageView iv_middle_4;
    @BindView(R.id.iv_middle_5)
    ImageView iv_middle_5;
    @BindView(R.id.iv_middle_6)
    ImageView iv_middle_6;

    @BindView(R.id.tv_middle_1)
    TextView tv_middle_1;
    @BindView(R.id.tv_middle_2)
    TextView tv_middle_2;
    @BindView(R.id.tv_middle_3)
    TextView tv_middle_3;
    @BindView(R.id.tv_middle_4)
    TextView tv_middle_4;
    @BindView(R.id.tv_middle_5)
    TextView tv_middle_5;
    @BindView(R.id.tv_middle_6)
    TextView tv_middle_6;

    //Outer
    @BindView(R.id.fm_outer_1)
    FrameLayout fm_outer_1;
    @BindView(R.id.fm_outer_1_1)
    FrameLayout fm_outer_1_1;
    @BindView(R.id.fm_outer_1_2)
    FrameLayout fm_outer_1_2;
    @BindView(R.id.fm_outer_2)
    FrameLayout fm_outer_2;
    @BindView(R.id.fm_outer_2_1)
    FrameLayout fm_outer_2_1;
    @BindView(R.id.fm_outer_2_2)
    FrameLayout fm_outer_2_2;
    @BindView(R.id.fm_outer_3)
    FrameLayout fm_outer_3;
    @BindView(R.id.fm_outer_3_1)
    FrameLayout fm_outer_3_1;
    @BindView(R.id.fm_outer_3_2)
    FrameLayout fm_outer_3_2;
    @BindView(R.id.fm_outer_4)
    FrameLayout fm_outer_4;
    @BindView(R.id.fm_outer_4_1)
    FrameLayout fm_outer_4_1;
    @BindView(R.id.fm_outer_4_2)
    FrameLayout fm_outer_4_2;
    @BindView(R.id.fm_outer_5)
    FrameLayout fm_outer_5;
    @BindView(R.id.fm_outer_5_1)
    FrameLayout fm_outer_5_1;
    @BindView(R.id.fm_outer_5_2)
    FrameLayout fm_outer_5_2;
    @BindView(R.id.fm_outer_6)
    FrameLayout fm_outer_6;
    @BindView(R.id.fm_outer_6_1)
    FrameLayout fm_outer_6_1;
    @BindView(R.id.fm_outer_6_2)
    FrameLayout fm_outer_6_2;

    @BindView(R.id.fm_outer_1_)
    FrameLayout fm_outer_1_;
    @BindView(R.id.fm_outer_2_)
    FrameLayout fm_outer_2_;
    @BindView(R.id.fm_outer_3_)
    FrameLayout fm_outer_3_;
    @BindView(R.id.fm_outer_4_)
    FrameLayout fm_outer_4_;
    @BindView(R.id.fm_outer_5_)
    FrameLayout fm_outer_5_;
    @BindView(R.id.fm_outer_6_)
    FrameLayout fm_outer_6_;

    @BindView(R.id.iv_outer_1)
    ImageView iv_outer_1;
    @BindView(R.id.iv_outer_2)
    ImageView iv_outer_2;
    @BindView(R.id.iv_outer_3)
    ImageView iv_outer_3;
    @BindView(R.id.iv_outer_4)
    ImageView iv_outer_4;
    @BindView(R.id.iv_outer_5)
    ImageView iv_outer_5;
    @BindView(R.id.iv_outer_6)
    ImageView iv_outer_6;

    @BindView(R.id.tv_outer_1)
    TextView tv_outer_1;
    @BindView(R.id.tv_outer_2)
    TextView tv_outer_2;
    @BindView(R.id.tv_outer_3)
    TextView tv_outer_3;
    @BindView(R.id.tv_outer_4)
    TextView tv_outer_4;
    @BindView(R.id.tv_outer_5)
    TextView tv_outer_5;
    @BindView(R.id.tv_outer_6)
    TextView tv_outer_6;

    @BindView(R.id.tv_mark_1)
    TextView tv_mark_1;
    @BindView(R.id.tv_mark_2)
    TextView tv_mark_2;
    @BindView(R.id.tv_mark_3)
    TextView tv_mark_3;
    @BindView(R.id.tv_mark_4)
    TextView tv_mark_4;
    @BindView(R.id.tv_mark_5)
    TextView tv_mark_5;
    @BindView(R.id.tv_mark_6)
    TextView tv_mark_6;
    @BindView(R.id.tv_mark_7)
    TextView tv_mark_7;
    @BindView(R.id.tv_mark_8)
    TextView tv_mark_8;
    @BindView(R.id.tv_mark_9)
    TextView tv_mark_9;
    @BindView(R.id.tv_mark_10)
    TextView tv_mark_10;
    @BindView(R.id.tv_mark_11)
    TextView tv_mark_11;
    @BindView(R.id.tv_mark_12)
    TextView tv_mark_12;
    @BindView(R.id.tv_mark_13)
    TextView tv_mark_13;
    @BindView(R.id.tv_mark_14)
    TextView tv_mark_14;
    @BindView(R.id.tv_mark_15)
    TextView tv_mark_15;
    @BindView(R.id.tv_mark_16)
    TextView tv_mark_16;

    @BindView(R.id.tv_value)
    TextView tv_value;
    @BindView(R.id.tv_value1)
    TextView tv_value1;
    @BindView(R.id.tv_wmsi_value)
    TextView tv_wmsi_value;
    @BindView(R.id.tv_lvef_value)
    TextView tv_lvef_value;
    @BindView(R.id.ib_remove_patient)
    ImageButton ib_remove_patient;
    @BindView(R.id.editPatientName)
    EditText editPatientName;


    SharedPreferences pref;

    private FragmentHomeBinding binding;
    private boolean isLongPressed;

    SharedPreferences mPrefSubscribe;
    private boolean isSubscribe;

    @Override
    public void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance( getContext());
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Home");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        ButterKnife.bind(this, root);

        mPrefSubscribe = getActivity().getSharedPreferences(AppConstant.PREF_SUBSCRIBE, Context.MODE_PRIVATE);
        isSubscribe = mPrefSubscribe.getBoolean(AppConstant.IS_SUBSCRIBE, false);

        pref = getActivity().getSharedPreferences(PREF_KEY, Context.MODE_PRIVATE);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widthDevice = size.x;
        int heightDevice = size.y;

        try {

            int centerMain = widthDevice / 3;
            int centerInner = widthDevice / 6;

            cv_center_first.getLayoutParams().width = centerInner;
            cv_center_first.getLayoutParams().height = centerMain;
            cv_center_sec.getLayoutParams().width = centerInner;
            cv_center_sec.getLayoutParams().height = centerMain;

            fm_center_1_.getLayoutParams().width = centerInner;
            fm_center_2_.getLayoutParams().width = centerInner;
            fm_center_3_.getLayoutParams().width = centerInner;
            fm_center_4_.getLayoutParams().width = centerInner;

            fm_center_1_.getLayoutParams().height = centerInner;
            fm_center_2_.getLayoutParams().height = centerInner;
            fm_center_3_.getLayoutParams().height = centerInner;
            fm_center_4_.getLayoutParams().height = centerInner;

            int middleMain = (int) (widthDevice / 1.58);
            int middleInner = (int) (widthDevice / (1.58 * 2));

            cv_middle_first.getLayoutParams().width = middleMain;
            cv_middle_first.getLayoutParams().height = middleInner;
            cv_middle_sec.getLayoutParams().width = middleMain;
            cv_middle_sec.getLayoutParams().height = middleInner;

            fm_middle_1_.getLayoutParams().width = middleInner;
            fm_middle_2_.getLayoutParams().width = middleInner;
            fm_middle_3_.getLayoutParams().width = middleInner;
            fm_middle_4_.getLayoutParams().width = middleInner;
            fm_middle_5_.getLayoutParams().width = middleInner;
            fm_middle_6_.getLayoutParams().width = middleInner;

            fm_middle_1_.getLayoutParams().height = middleInner;
            fm_middle_2_.getLayoutParams().height = middleInner;
            fm_middle_3_.getLayoutParams().height = middleInner;
            fm_middle_4_.getLayoutParams().height = middleInner;
            fm_middle_5_.getLayoutParams().height = middleInner;
            fm_middle_6_.getLayoutParams().height = middleInner;

            int outerMain = (int) (widthDevice / 1.08);
            int outerInner = (int) (widthDevice / (1.08 * 2));

            cv_outer_first.getLayoutParams().width = outerMain;
            cv_outer_first.getLayoutParams().height = outerInner;
            cv_outer_sec.getLayoutParams().width = outerMain;
            cv_outer_sec.getLayoutParams().height = outerInner;

            fm_outer_1_.getLayoutParams().width = outerInner;
            fm_outer_2_.getLayoutParams().width = outerInner;
            fm_outer_3_.getLayoutParams().width = outerInner;
            fm_outer_4_.getLayoutParams().width = outerInner;
            fm_outer_5_.getLayoutParams().width = outerInner;
            fm_outer_6_.getLayoutParams().width = outerInner;

            fm_outer_1_.getLayoutParams().height = outerInner;
            fm_outer_2_.getLayoutParams().height = outerInner;
            fm_outer_3_.getLayoutParams().height = outerInner;
            fm_outer_4_.getLayoutParams().height = outerInner;
            fm_outer_5_.getLayoutParams().height = outerInner;
            fm_outer_6_.getLayoutParams().height = outerInner;


            int widthFm = widthDevice / 7;

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.TOP;
            params.setMargins(0, (int) (widthFm / 1.5), 0, 0);
            fm_outer_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.START;
            params.setMargins(widthFm / 2, widthFm / 4, 0, 0);
            fm_outer_1_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM;
            params.setMargins(widthFm / 8, 0, 0, 0);
            fm_outer_1_2.setLayoutParams(params);

            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.TOP;
            params.setMargins(0, (int) (widthFm / 1.5), 0, 0);
            fm_outer_3.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.END;
            params.setMargins(0, widthFm / 4, widthFm / 2, 0);
            fm_outer_3_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.END;
            params.setMargins(0, 0, widthFm / 8, 0);
            fm_outer_3_2.setLayoutParams(params);

            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.START;
            params.setMargins(widthFm / 8, 0, 0, 0);
            fm_outer_6.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.START;
            params.setMargins(widthFm / 2, 0, 0, widthFm / 4);
            fm_outer_6_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER;
            params.setMargins(0, 0, 0, (int) (widthFm / 1.5));
            fm_outer_6_2.setLayoutParams(params);

            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.END | Gravity.CENTER | Gravity.TOP;
            params.setMargins(0, 0, widthFm / 8, 0);
            fm_outer_4.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.END;
            params.setMargins(0, 0, widthFm / 2, widthFm / 4);
            fm_outer_4_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER;
            params.setMargins(0, 0, 0, (int) (widthFm / 1.5));
            fm_outer_4_2.setLayoutParams(params);

            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.TOP;
            params.setMargins(0, widthFm / 10, 0, 0);
            fm_outer_2.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.TOP | Gravity.START;
            params.setMargins((int) (widthFm / 2.5), widthFm / 4, 0, 0);
            fm_outer_2_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.TOP | Gravity.END;
            params.setMargins(0, widthFm / 4, (int) (widthFm / 2.5), 0);
            fm_outer_2_2.setLayoutParams(params);

            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.BOTTOM;
            params.setMargins(0, 0, 0, widthFm / 10);
            fm_outer_5.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.BOTTOM | Gravity.START;
            params.setMargins((int) (widthFm / 2.5), 0, 0, widthFm / 4);
            fm_outer_5_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.BOTTOM | Gravity.END;
            params.setMargins(0, 0, (int) (widthFm / 2.5), widthFm / 4);
            fm_outer_5_2.setLayoutParams(params);

            widthFm = widthDevice / 7;
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP | Gravity.CENTER;
            params.setMargins(0, (int) (widthFm / 1.5), 0, 0);
            fm_middle_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM;
            params.setMargins(widthFm / 4, 0, 0, 0);
            fm_middle_1_1.setLayoutParams(params);


            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.CENTER | Gravity.TOP;
            params.setMargins(0, (int) (widthFm / 1.5), 0, 0);
            fm_middle_3.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.END;
            params.setMargins(0, 0, widthFm / 4, 0);
            fm_middle_3_1.setLayoutParams(params);

            widthFm = widthDevice / 7;
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP;
            params.setMargins(widthFm / 4, 0, 0, 0);
            fm_middle_6.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER;
            params.setMargins(0, 0, 0, (int) (widthFm / 1.5));
            fm_middle_6_1.setLayoutParams(params);

            widthFm = widthDevice / 7;
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP | Gravity.END;
            params.setMargins(0, 0, widthFm / 4, 0);
            fm_middle_4.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER;
            params.setMargins(0, 0, 0, (int) (widthFm / 1.5));
            fm_middle_4_1.setLayoutParams(params);

            widthFm = widthDevice / 7;
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP | Gravity.CENTER | Gravity.START;
            params.setMargins(widthFm / 5, widthFm / 5, 0, 0);
            fm_middle_2.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP | Gravity.CENTER | Gravity.END;
            params.setMargins(0, widthFm / 5, widthFm / 5, 0);
            fm_middle_2_1.setLayoutParams(params);

            widthFm = widthDevice / 7;
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER | Gravity.START;
            params.setMargins(widthFm / 5, 0, 0, widthFm / 5);
            fm_middle_5.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.CENTER | Gravity.END;
            params.setMargins(0, 0, widthFm / 5, widthFm / 5);
            fm_middle_5_1.setLayoutParams(params);

            widthFm = (int) (widthDevice / 8.2);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.END;
            params.setMargins(0, 0, 0, 0);
            fm_center_1.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP | Gravity.END;
            params.setMargins(0, 0, 0, 0);
            fm_center_2.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.TOP | Gravity.START;
            params.setMargins(0, 0, 0, 0);
            fm_center_3.setLayoutParams(params);
            params = new FrameLayout.LayoutParams(widthFm, widthFm);
            params.gravity = Gravity.BOTTOM | Gravity.START;
            params.setMargins(0, 0, 0, 0);
            fm_center_4.setLayoutParams(params);


        } catch (Exception ignore) {

        }

        colors = new int[]{getResources().getColor(R.color.green), getResources().getColor(R.color.yellow),
                getResources().getColor(R.color.red), getResources().getColor(R.color.blue), Color.BLACK,};
        strValue = new String[]{getString(R.string.value1), getString(R.string.value2), getString(R.string.value3), getString(R.string.value4), getString(R.string.value5),};
        strSubValue = new String[]{getString(R.string.subvalue1), getString(R.string.subvalue2), getString(R.string.subvalue3), getString(R.string.subvalue4), getString(R.string.subvalue5),};

        strTitleValue = new String[]{getString(R.string.titlevalue1), getString(R.string.titlevalue2), getString(R.string.titlevalue3),};
        fm_center_1.setOnClickListener(this);
        fm_center_2.setOnClickListener(this);
        fm_center_3.setOnClickListener(this);
        fm_center_4.setOnClickListener(this);

        fm_center_1.setOnLongClickListener(this);
        fm_center_2.setOnLongClickListener(this);
        fm_center_3.setOnLongClickListener(this);
        fm_center_4.setOnLongClickListener(this);

        fm_center_1.setOnTouchListener(this);
        fm_center_2.setOnTouchListener(this);
        fm_center_3.setOnTouchListener(this);
        fm_center_4.setOnTouchListener(this);

        fm_middle_1.setOnLongClickListener(this);
        fm_middle_1_1.setOnLongClickListener(this);
        fm_middle_2.setOnLongClickListener(this);
        fm_middle_2_1.setOnLongClickListener(this);
        fm_middle_3.setOnLongClickListener(this);
        fm_middle_3_1.setOnLongClickListener(this);
        fm_middle_4.setOnLongClickListener(this);
        fm_middle_4_1.setOnLongClickListener(this);
        fm_middle_5.setOnLongClickListener(this);
        fm_middle_5_1.setOnLongClickListener(this);
        fm_middle_6.setOnLongClickListener(this);
        fm_middle_6_1.setOnLongClickListener(this);

        fm_middle_1.setOnTouchListener(this);
        fm_middle_1_1.setOnTouchListener(this);
        fm_middle_2.setOnTouchListener(this);
        fm_middle_2_1.setOnTouchListener(this);
        fm_middle_3.setOnTouchListener(this);
        fm_middle_3_1.setOnTouchListener(this);
        fm_middle_4.setOnTouchListener(this);
        fm_middle_4_1.setOnTouchListener(this);
        fm_middle_5.setOnTouchListener(this);
        fm_middle_5_1.setOnTouchListener(this);
        fm_middle_6.setOnTouchListener(this);
        fm_middle_6_1.setOnTouchListener(this);

        fm_middle_1.setOnClickListener(this);
        fm_middle_1_1.setOnClickListener(this);
        fm_middle_2.setOnClickListener(this);
        fm_middle_2_1.setOnClickListener(this);
        fm_middle_3.setOnClickListener(this);
        fm_middle_3_1.setOnClickListener(this);
        fm_middle_4.setOnClickListener(this);
        fm_middle_4_1.setOnClickListener(this);
        fm_middle_5.setOnClickListener(this);
        fm_middle_5_1.setOnClickListener(this);
        fm_middle_6.setOnClickListener(this);
        fm_middle_6_1.setOnClickListener(this);

        fm_outer_1.setOnLongClickListener(this);
        fm_outer_1_1.setOnLongClickListener(this);
        fm_outer_1_2.setOnLongClickListener(this);
        fm_outer_2.setOnLongClickListener(this);
        fm_outer_2_1.setOnLongClickListener(this);
        fm_outer_2_2.setOnLongClickListener(this);
        fm_outer_3.setOnLongClickListener(this);
        fm_outer_3_1.setOnLongClickListener(this);
        fm_outer_3_2.setOnLongClickListener(this);
        fm_outer_4.setOnLongClickListener(this);
        fm_outer_4_1.setOnLongClickListener(this);
        fm_outer_4_2.setOnLongClickListener(this);
        fm_outer_5.setOnLongClickListener(this);
        fm_outer_5_1.setOnLongClickListener(this);
        fm_outer_5_2.setOnLongClickListener(this);
        fm_outer_6.setOnLongClickListener(this);
        fm_outer_6_1.setOnLongClickListener(this);
        fm_outer_6_2.setOnLongClickListener(this);

        fm_outer_1.setOnTouchListener(this);
        fm_outer_1_1.setOnTouchListener(this);
        fm_outer_1_2.setOnTouchListener(this);
        fm_outer_2.setOnTouchListener(this);
        fm_outer_2_1.setOnTouchListener(this);
        fm_outer_2_2.setOnTouchListener(this);
        fm_outer_3.setOnTouchListener(this);
        fm_outer_3_1.setOnTouchListener(this);
        fm_outer_3_2.setOnTouchListener(this);
        fm_outer_4.setOnTouchListener(this);
        fm_outer_4_1.setOnTouchListener(this);
        fm_outer_4_2.setOnTouchListener(this);
        fm_outer_5.setOnTouchListener(this);
        fm_outer_5_1.setOnTouchListener(this);
        fm_outer_5_2.setOnTouchListener(this);
        fm_outer_6.setOnTouchListener(this);
        fm_outer_6_1.setOnTouchListener(this);
        fm_outer_6_2.setOnTouchListener(this);

        fm_outer_1.setOnClickListener(this);
        fm_outer_1_1.setOnClickListener(this);
        fm_outer_1_2.setOnClickListener(this);
        fm_outer_2.setOnClickListener(this);
        fm_outer_2_1.setOnClickListener(this);
        fm_outer_2_2.setOnClickListener(this);
        fm_outer_3.setOnClickListener(this);
        fm_outer_3_1.setOnClickListener(this);
        fm_outer_3_2.setOnClickListener(this);
        fm_outer_4.setOnClickListener(this);
        fm_outer_4_1.setOnClickListener(this);
        fm_outer_4_2.setOnClickListener(this);
        fm_outer_5.setOnClickListener(this);
        fm_outer_5_1.setOnClickListener(this);
        fm_outer_5_2.setOnClickListener(this);
        fm_outer_6.setOnClickListener(this);
        fm_outer_6_1.setOnClickListener(this);
        fm_outer_6_2.setOnClickListener(this);

        fm_middle_5_.bringToFront();
        fm_middle_2_.bringToFront();
        fm_outer_5_.bringToFront();
        fm_outer_2_.bringToFront();

        setCirclePos();

        editPatientName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPatientName.getText().toString().length() > 0) {
                    ib_remove_patient.setVisibility(View.VISIBLE);
                } else {
                    ib_remove_patient.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ib_remove_patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editPatientName.setText("");
                ib_remove_patient.setVisibility(View.INVISIBLE);
            }
        });

        tv_value1.setText("");
        tv_value.setText("");
        return root;
    }

    private String calculateWMSI() {

        float sumOfSegment = (cir_center_1 + 1) + (cir_center_2 + 1) + (cir_center_3 + 1) + (cir_center_4 + 1) +
                (cir_middle_1 + 1) + (cir_middle_2 + 1) + (cir_middle_3 + 1) + (cir_middle_4 + 1) + (cir_middle_5 + 1) + (cir_middle_6 + 1) +
                (cir_outer_1 + 1) + (cir_outer_2 + 1) + (cir_outer_3 + 1) + (cir_outer_4 + 1) + (cir_outer_5 + 1) + (cir_outer_6 + 1);

        val_WMSI = (float) sumOfSegment / 16;
        return String.format(" %.2f", val_WMSI);
    }

    private String calculateLVEF() {
        val_LVEF = Math.round((float) (0.90 - (0.26 * val_WMSI)) * 100);

        return " " + ((int) val_LVEF) + "%";
    }

    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull @NotNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_clear) {

            if (!isSubscribe())
                return false;

            setCirclePos();
            return true;
        } else if (itemId == R.id.action_report) {

            if (!isSubscribe())
                return false;

            File file = new File(getContext().getFilesDir(), "tempSpin.png");

            Intent intent = new Intent(getContext(), ReportActivity.class);
            intent.putExtra(AppConstant.PATIENT_NAME, editPatientName.getText().toString());
            intent.putExtra(AppConstant.WMSI_VALUE, calculateWMSI());
            intent.putExtra(AppConstant.LVEF_VALUE, calculateLVEF());
            intent.putExtra(AppConstant.SPIN_PATH, savePhoto(getBitmapFromView(cl_circle), file));


            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    void setCirclePos() {

        pref.edit().clear().apply();

        cir_center_1 = pref.getInt("cir_center_1", 0);
        cir_center_2 = pref.getInt("cir_center_2", 0);
        cir_center_3 = pref.getInt("cir_center_3", 0);
        cir_center_4 = pref.getInt("cir_center_4", 0);

        cir_middle_1 = pref.getInt("cir_middle_1", 0);
        cir_middle_2 = pref.getInt("cir_middle_2", 0);
        cir_middle_3 = pref.getInt("cir_middle_3", 0);
        cir_middle_4 = pref.getInt("cir_middle_4", 0);
        cir_middle_5 = pref.getInt("cir_middle_5", 0);
        cir_middle_6 = pref.getInt("cir_middle_6", 0);

        cir_outer_1 = pref.getInt("cir_outer_1", 0);
        cir_outer_2 = pref.getInt("cir_outer_2", 0);
        cir_outer_3 = pref.getInt("cir_outer_3", 0);
        cir_outer_4 = pref.getInt("cir_outer_4", 0);
        cir_outer_5 = pref.getInt("cir_outer_5", 0);
        cir_outer_6 = pref.getInt("cir_outer_6", 0);

        if (cir_center_1 >= 5) {
            cir_center_1 = 0;
        }

        iv_center_1.setColorFilter(colors[cir_center_1]);
        tv_center_1.setText(String.valueOf(cir_center_1 + 1));

        if (cir_center_1 == 4) {
            tv_center_1.setTextColor(Color.WHITE);
        } else {
            tv_center_1.setTextColor(Color.BLACK);

        }

        if (cir_center_2 >= 5) {
            cir_center_2 = 0;
        }
        iv_center_2.setColorFilter(colors[cir_center_2]);
        tv_center_2.setText(String.valueOf(cir_center_2 + 1));
        if (cir_center_2 == 4) {
            tv_center_2.setTextColor(Color.WHITE);
        } else {
            tv_center_2.setTextColor(Color.BLACK);
        }
        if (cir_center_3 >= 5) {
            cir_center_3 = 0;
        }
        iv_center_3.setColorFilter(colors[cir_center_3]);
        tv_center_3.setText(String.valueOf(cir_center_3 + 1));
        if (cir_center_3 == 4) {
            tv_center_3.setTextColor(Color.WHITE);
        } else {
            tv_center_3.setTextColor(Color.BLACK);
        }
        if (cir_center_4 >= 5) {
            cir_center_4 = 0;
        }
        iv_center_4.setColorFilter(colors[cir_center_4]);
        tv_center_4.setText(String.valueOf(cir_center_4 + 1));
        tv_center_4.setTextColor(Color.WHITE);
        if (cir_center_4 == 4) {
            tv_center_4.setTextColor(Color.WHITE);
        } else {
            tv_center_4.setTextColor(Color.BLACK);
        }
        if (cir_middle_1 >= 5) {
            cir_middle_1 = 0;
        }
        iv_middle_1.setColorFilter(colors[cir_middle_1]);
        tv_middle_1.setText(String.valueOf(cir_middle_1 + 1));
        if (cir_middle_1 == 4) {
            tv_middle_1.setTextColor(Color.WHITE);
        } else {
            tv_middle_1.setTextColor(Color.BLACK);
        }

        if (cir_middle_2 >= 5) {
            cir_middle_2 = 0;
        }
        iv_middle_2.setColorFilter(colors[cir_middle_2]);
        tv_middle_2.setText(String.valueOf(cir_middle_2 + 1));
        if (cir_middle_2 == 4) {
            tv_middle_2.setTextColor(Color.WHITE);
        } else {
            tv_middle_2.setTextColor(Color.BLACK);
        }

        if (cir_middle_3 >= 5) {
            cir_middle_3 = 0;
        }
        iv_middle_3.setColorFilter(colors[cir_middle_3]);
        tv_middle_3.setText(String.valueOf(cir_middle_3 + 1));
        if (cir_middle_3 == 4) {
            tv_middle_3.setTextColor(Color.WHITE);
        } else {
            tv_middle_3.setTextColor(Color.BLACK);
        }

        if (cir_middle_4 >= 5) {
            cir_middle_4 = 0;
        }
        iv_middle_4.setColorFilter(colors[cir_middle_4]);
        tv_middle_4.setText(String.valueOf(cir_middle_4 + 1));
        if (cir_middle_4 == 4) {
            tv_middle_4.setTextColor(Color.WHITE);
        } else {
            tv_middle_4.setTextColor(Color.BLACK);
        }

        if (cir_middle_5 >= 5) {
            cir_middle_5 = 0;
        }
        iv_middle_5.setColorFilter(colors[cir_middle_5]);
        tv_middle_5.setText(String.valueOf(cir_middle_5 + 1));
        if (cir_middle_5 == 4) {
            tv_middle_5.setTextColor(Color.WHITE);
        } else {
            tv_middle_5.setTextColor(Color.BLACK);
        }

        if (cir_middle_6 >= 5) {
            cir_middle_6 = 0;
        }
        iv_middle_6.setColorFilter(colors[cir_middle_6]);
        tv_middle_6.setText(String.valueOf(cir_middle_6 + 1));
        if (cir_middle_6 == 4) {
            tv_middle_6.setTextColor(Color.WHITE);
        } else {
            tv_middle_6.setTextColor(Color.BLACK);
        }

        if (cir_outer_1 >= 5) {
            cir_outer_1 = 0;
        }
        iv_outer_1.setColorFilter(colors[cir_outer_1]);
        tv_outer_1.setText(String.valueOf(cir_outer_1 + 1));
        if (cir_outer_1 == 4) {
            tv_outer_1.setTextColor(Color.WHITE);
        } else {
            tv_outer_1.setTextColor(Color.BLACK);
        }
        if (cir_outer_2 >= 5) {
            cir_outer_2 = 0;
        }
        iv_outer_2.setColorFilter(colors[cir_outer_2]);
        tv_outer_2.setText(String.valueOf(cir_outer_2 + 1));
        if (cir_outer_2 == 4) {
            tv_outer_2.setTextColor(Color.WHITE);
        } else {
            tv_outer_2.setTextColor(Color.BLACK);
        }
        if (cir_outer_3 >= 5) {
            cir_outer_3 = 0;
        }
        iv_outer_3.setColorFilter(colors[cir_outer_3]);
        tv_outer_3.setText(String.valueOf(cir_outer_3 + 1));
        if (cir_outer_3 == 4) {
            tv_outer_3.setTextColor(Color.WHITE);
        } else {
            tv_outer_3.setTextColor(Color.BLACK);
        }
        if (cir_outer_4 >= 5) {
            cir_outer_4 = 0;
        }
        iv_outer_4.setColorFilter(colors[cir_outer_4]);
        tv_outer_4.setText(String.valueOf(cir_outer_4 + 1));
        if (cir_outer_4 == 4) {
            tv_outer_4.setTextColor(Color.WHITE);
        } else {
            tv_outer_4.setTextColor(Color.BLACK);
        }
        if (cir_outer_5 >= 5) {
            cir_outer_5 = 0;
        }
        iv_outer_5.setColorFilter(colors[cir_outer_5]);
        tv_outer_5.setText(String.valueOf(cir_outer_5 + 1));
        if (cir_outer_5 == 4) {
            tv_outer_5.setTextColor(Color.WHITE);
        } else {
            tv_outer_5.setTextColor(Color.BLACK);
        }
        if (cir_outer_6 >= 5) {
            cir_outer_6 = 0;
        }
        iv_outer_6.setColorFilter(colors[cir_outer_6]);
        tv_outer_6.setText(String.valueOf(cir_outer_6 + 1));
        if (cir_outer_6 == 4) {
            tv_outer_6.setTextColor(Color.WHITE);
        } else {
            tv_outer_6.setTextColor(Color.BLACK);
        }

        tv_wmsi_value.setText(calculateWMSI());
        tv_lvef_value.setText(calculateLVEF());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



    @Override
    public void onClick(View view) {

//        if (!isSubscribe())
//            return;

        if (view.getId() == fm_center_1.getId()) {
            cir_center_1++;
            if (cir_center_1 >= 5) {
                cir_center_1 = 0;
            }
            iv_center_1.setColorFilter(colors[cir_center_1]);
            tv_center_1.setText(String.valueOf(cir_center_1 + 1));
            if (cir_center_1 == 4) {
                tv_center_1.setTextColor(Color.WHITE);
            } else {
                tv_center_1.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_center_1", cir_center_1).apply();

        } else if (view.getId() == fm_center_2.getId()) {
            cir_center_2++;
            if (cir_center_2 >= 5) {
                cir_center_2 = 0;
            }
            iv_center_2.setColorFilter(colors[cir_center_2]);
            tv_center_2.setText(String.valueOf(cir_center_2 + 1));
            if (cir_center_2 == 4) {
                tv_center_2.setTextColor(Color.WHITE);
            } else {
                tv_center_2.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_center_2", cir_center_2).apply();
        } else if (view.getId() == fm_center_3.getId()) {
            cir_center_3++;
            if (cir_center_3 >= 5) {
                cir_center_3 = 0;
            }
            iv_center_3.setColorFilter(colors[cir_center_3]);
            tv_center_3.setText(String.valueOf(cir_center_3 + 1));
            if (cir_center_3 == 4) {
                tv_center_3.setTextColor(Color.WHITE);
            } else {
                tv_center_3.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_center_3", cir_center_3).apply();
        } else if (view.getId() == fm_center_4.getId()) {
            cir_center_4++;
            if (cir_center_4 >= 5) {
                cir_center_4 = 0;
            }
            iv_center_4.setColorFilter(colors[cir_center_4]);
            tv_center_4.setText(String.valueOf(cir_center_4 + 1));
            tv_center_4.setTextColor(Color.WHITE);
            if (cir_center_4 == 4) {
                tv_center_4.setTextColor(Color.WHITE);
            } else {
                tv_center_4.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_center_4", cir_center_4).apply();
        } else if (view.getId() == fm_middle_1.getId() || view.getId() == fm_middle_1_1.getId()) {
            cir_middle_1++;
            if (cir_middle_1 >= 5) {
                cir_middle_1 = 0;
            }
            iv_middle_1.setColorFilter(colors[cir_middle_1]);
            tv_middle_1.setText(String.valueOf(cir_middle_1 + 1));
            if (cir_middle_1 == 4) {
                tv_middle_1.setTextColor(Color.WHITE);
            } else {
                tv_middle_1.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_middle_1", cir_middle_1).apply();
        } else if (view.getId() == fm_middle_2.getId() || view.getId() == fm_middle_2_1.getId()) {
            cir_middle_2++;
            if (cir_middle_2 >= 5) {
                cir_middle_2 = 0;
            }
            iv_middle_2.setColorFilter(colors[cir_middle_2]);
            tv_middle_2.setText(String.valueOf(cir_middle_2 + 1));
            if (cir_middle_2 == 4) {
                tv_middle_2.setTextColor(Color.WHITE);
            } else {
                tv_middle_2.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_middle_2", cir_middle_2).apply();
        } else if (view.getId() == fm_middle_3.getId() || view.getId() == fm_middle_3_1.getId()) {
            cir_middle_3++;
            if (cir_middle_3 >= 5) {
                cir_middle_3 = 0;
            }
            iv_middle_3.setColorFilter(colors[cir_middle_3]);
            tv_middle_3.setText(String.valueOf(cir_middle_3 + 1));
            if (cir_middle_3 == 4) {
                tv_middle_3.setTextColor(Color.WHITE);
            } else {
                tv_middle_3.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_middle_3", cir_middle_3).apply();
        } else if (view.getId() == fm_middle_4.getId() || view.getId() == fm_middle_4_1.getId()) {
            cir_middle_4++;
            if (cir_middle_4 >= 5) {
                cir_middle_4 = 0;
            }
            iv_middle_4.setColorFilter(colors[cir_middle_4]);
            tv_middle_4.setText(String.valueOf(cir_middle_4 + 1));
            if (cir_middle_4 == 4) {
                tv_middle_4.setTextColor(Color.WHITE);
            } else {
                tv_middle_4.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_middle_4", cir_middle_4).apply();
        } else if (view.getId() == fm_middle_5.getId() || view.getId() == fm_middle_5_1.getId()) {
            cir_middle_5++;
            if (cir_middle_5 >= 5) {
                cir_middle_5 = 0;
            }
            iv_middle_5.setColorFilter(colors[cir_middle_5]);
            tv_middle_5.setText(String.valueOf(cir_middle_5 + 1));
            if (cir_middle_5 == 4) {
                tv_middle_5.setTextColor(Color.WHITE);
            } else {
                tv_middle_5.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_middle_5", cir_middle_5).apply();
        } else if (view.getId() == fm_middle_6.getId() || view.getId() == fm_middle_6_1.getId()) {
            cir_middle_6++;
            if (cir_middle_6 >= 5) {
                cir_middle_6 = 0;
            }
            iv_middle_6.setColorFilter(colors[cir_middle_6]);
            tv_middle_6.setText(String.valueOf(cir_middle_6 + 1));
            if (cir_middle_6 == 4) {
                tv_middle_6.setTextColor(Color.WHITE);
            } else {
                tv_middle_6.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_middle_6", cir_middle_6).apply();
        } else if (view.getId() == fm_outer_1.getId() || view.getId() == fm_outer_1_1.getId() || view.getId() == fm_outer_1_2.getId()) {
            cir_outer_1++;
            if (cir_outer_1 >= 5) {
                cir_outer_1 = 0;
            }
            iv_outer_1.setColorFilter(colors[cir_outer_1]);
            tv_outer_1.setText(String.valueOf(cir_outer_1 + 1));
            if (cir_outer_1 == 4) {
                tv_outer_1.setTextColor(Color.WHITE);
            } else {
                tv_outer_1.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_outer_1", cir_outer_1).apply();
        } else if (view.getId() == fm_outer_2.getId() || view.getId() == fm_outer_2_1.getId() || view.getId() == fm_outer_2_2.getId()) {
            cir_outer_2++;
            if (cir_outer_2 >= 5) {
                cir_outer_2 = 0;
            }
            iv_outer_2.setColorFilter(colors[cir_outer_2]);
            tv_outer_2.setText(String.valueOf(cir_outer_2 + 1));
            if (cir_outer_2 == 4) {
                tv_outer_2.setTextColor(Color.WHITE);
            } else {
                tv_outer_2.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_outer_2", cir_outer_2).apply();
        } else if (view.getId() == fm_outer_3.getId() || view.getId() == fm_outer_3_1.getId() || view.getId() == fm_outer_3_2.getId()) {
            cir_outer_3++;
            if (cir_outer_3 >= 5) {
                cir_outer_3 = 0;
            }
            iv_outer_3.setColorFilter(colors[cir_outer_3]);
            tv_outer_3.setText(String.valueOf(cir_outer_3 + 1));
            if (cir_outer_3 == 4) {
                tv_outer_3.setTextColor(Color.WHITE);
            } else {
                tv_outer_3.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_outer_3", cir_outer_3).apply();
        } else if (view.getId() == fm_outer_4.getId() || view.getId() == fm_outer_4_1.getId() || view.getId() == fm_outer_4_2.getId()) {
            cir_outer_4++;
            if (cir_outer_4 >= 5) {
                cir_outer_4 = 0;
            }
            iv_outer_4.setColorFilter(colors[cir_outer_4]);
            tv_outer_4.setText(String.valueOf(cir_outer_4 + 1));
            if (cir_outer_4 == 4) {
                tv_outer_4.setTextColor(Color.WHITE);
            } else {
                tv_outer_4.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_outer_4", cir_outer_4).apply();
        } else if (view.getId() == fm_outer_5.getId() || view.getId() == fm_outer_5_1.getId() || view.getId() == fm_outer_5_2.getId()) {
            cir_outer_5++;
            if (cir_outer_5 >= 5) {
                cir_outer_5 = 0;
            }
            iv_outer_5.setColorFilter(colors[cir_outer_5]);
            tv_outer_5.setText(String.valueOf(cir_outer_5 + 1));
            if (cir_outer_5 == 4) {
                tv_outer_5.setTextColor(Color.WHITE);
            } else {
                tv_outer_5.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_outer_5", cir_outer_5).apply();
        } else if (view.getId() == fm_outer_6.getId() || view.getId() == fm_outer_6_1.getId() || view.getId() == fm_outer_6_2.getId()) {
            cir_outer_6++;
            if (cir_outer_6 >= 5) {
                cir_outer_6 = 0;
            }
            iv_outer_6.setColorFilter(colors[cir_outer_6]);
            tv_outer_6.setText(String.valueOf(cir_outer_6 + 1));
            if (cir_outer_6 == 4) {
                tv_outer_6.setTextColor(Color.WHITE);
            } else {
                tv_outer_6.setTextColor(Color.BLACK);
            }
            pref.edit().putInt("cir_outer_6", cir_outer_6).apply();
        }
        tv_wmsi_value.setText(calculateWMSI());
        tv_lvef_value.setText(calculateLVEF());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        v.onTouchEvent(event);
//        if (event.getAction() == MotionEvent.ACTION_UP) {
//            if (isLongPressed) {
//                isLongPressed = false;
//                tv_value.setVisibility(View.INVISIBLE);
//            }
//        }
//        isLongPressed = true;
        tv_value.setVisibility(View.VISIBLE);
        SharedInstance instance = SharedInstance.getInstance();
        if (v.getId() == fm_center_1.getId()) {
            tv_value.setText(strValue[cir_center_1]);
            String title = strTitleValue[2];
            String subTitle = strSubValue[cir_center_1];
            tv_value1.setText(String.format("13 %s %s", title, subTitle));
            instance.ringFinalValue13 = String.format("13. %s: %s %s",strValue[cir_center_1], title, subTitle);
        } else if (v.getId() == fm_center_2.getId()) {
            tv_value.setText(strValue[cir_center_2]);
            String title = strTitleValue[2];
            String subTitle = strSubValue[cir_center_2];
            tv_value1.setText(String.format("14 %s %s", title, subTitle));
            instance.ringFinalValue14 = String.format("14. %s: %s %s",strValue[cir_center_2], title, subTitle);
        } else if (v.getId() == fm_center_3.getId()) {
            tv_value.setText(strValue[cir_center_3]);
            String title = strTitleValue[2];
            String subTitle = strSubValue[cir_center_3];
            tv_value1.setText(String.format("15 %s %s", title, subTitle));
            instance.ringFinalValue15 = String.format("15. %s: %s %s",strValue[cir_center_3], title, subTitle);
        } else if (v.getId() == fm_center_4.getId()) {
            tv_value.setText(strValue[cir_center_4]);
            String title = strTitleValue[2];
            String subTitle = strSubValue[cir_center_4];
            tv_value1.setText(String.format("16 %s %s", title, subTitle));
            instance.ringFinalValue16 = String.format("16. %s: %s %s",strValue[cir_center_4], title, subTitle);
        } else if (v.getId() == fm_middle_1.getId() || v.getId() == fm_middle_1_1.getId()) {
            tv_value.setText(strValue[cir_middle_1]);
            String title = strTitleValue[1];
            String subTitle = strSubValue[cir_middle_1];
            tv_value1.setText(String.format("8 %s %s", title, subTitle));
            instance.ringFinalValue8 = String.format("8. %s: %s %s",strValue[cir_middle_1], title, subTitle);
        } else if (v.getId() == fm_middle_2.getId() || v.getId() == fm_middle_2_1.getId()) {
            tv_value.setText(strValue[cir_middle_2]);
            String title = strTitleValue[1];
            String subTitle = strSubValue[cir_middle_2];
            tv_value1.setText(String.format("7 %s %s", title, subTitle));
            instance.ringFinalValue7 = String.format("7. %s: %s %s",strValue[cir_middle_2], title, subTitle);

        } else if (v.getId() == fm_middle_3.getId() || v.getId() == fm_middle_3_1.getId()) {
            tv_value.setText(strValue[cir_middle_3]);
            String title = strTitleValue[1];
            String subTitle = strSubValue[cir_middle_3];
            tv_value1.setText(String.format("12 %s %s", title, subTitle));
            instance.ringFinalValue12 = String.format("12. %s: %s %s",strValue[cir_middle_3], title, subTitle);

        } else if (v.getId() == fm_middle_4.getId() || v.getId() == fm_middle_4_1.getId()) {
            tv_value.setText(strValue[cir_middle_4]);
            String title = strTitleValue[1];
            String subTitle = strSubValue[cir_middle_4];
            tv_value1.setText(String.format("11 %s %s", title, subTitle));
            instance.ringFinalValue11 = String.format("11. %s: %s %s",strValue[cir_middle_4], title, subTitle);

        } else if (v.getId() == fm_middle_5.getId() || v.getId() == fm_middle_5_1.getId()) {
            tv_value.setText(strValue[cir_middle_5]);
            String title = strTitleValue[1];
            String subTitle = strSubValue[cir_middle_5];
            tv_value1.setText(String.format("10 %s %s", title, subTitle));
            instance.ringFinalValue10 = String.format("10. %s: %s %s",strValue[cir_middle_5], title, subTitle);

        } else if (v.getId() == fm_middle_6.getId() || v.getId() == fm_middle_6_1.getId()) {
            tv_value.setText(strValue[cir_middle_6]);
            String title = strTitleValue[1];
            String subTitle = strSubValue[cir_middle_6];
            tv_value1.setText(String.format("9 %s %s", title, subTitle));
            instance.ringFinalValue9 = String.format("9. %s: %s %s",strValue[cir_middle_6], title, subTitle);

        } else if (v.getId() == fm_outer_1.getId() || v.getId() == fm_outer_1_1.getId() || v.getId() == fm_outer_1_2.getId()) {
            tv_value.setText(strValue[cir_outer_1]);
            String title = strTitleValue[0];
            String subTitle = strSubValue[cir_outer_1];
            tv_value1.setText(String.format("2 %s %s", title, subTitle));
            instance.ringFinalValue2 = String.format("2. %s: %s %s",strValue[cir_outer_1], title, subTitle);

        } else if (v.getId() == fm_outer_2.getId() || v.getId() == fm_outer_2_1.getId() || v.getId() == fm_outer_2_2.getId()) {
            tv_value.setText(strValue[cir_outer_2]);
            String title = strTitleValue[0];
            String subTitle = strSubValue[cir_outer_2];
            tv_value1.setText(String.format("1 %s %s", title, subTitle));
            instance.ringFinalValue1 = String.format("1. %s: %s %s",strValue[cir_outer_2], title, subTitle);

        } else if (v.getId() == fm_outer_3.getId() || v.getId() == fm_outer_3_1.getId() || v.getId() == fm_outer_3_2.getId()) {
            tv_value.setText(strValue[cir_outer_3]);
            String title = strTitleValue[0];
            String subTitle = strSubValue[cir_outer_3];
            tv_value1.setText(String.format("6 %s %s", title, subTitle));
            instance.ringFinalValue6 = String.format("6. %s: %s %s",strValue[cir_outer_3], title, subTitle);

        } else if (v.getId() == fm_outer_4.getId() || v.getId() == fm_outer_4_1.getId() || v.getId() == fm_outer_4_2.getId()) {
            tv_value.setText(strValue[cir_outer_4]);
            String title = strTitleValue[0];
            String subTitle = strSubValue[cir_outer_4];
            tv_value1.setText(String.format("5 %s %s", title, subTitle));
            instance.ringFinalValue5 = String.format("5. %s: %s %s",strValue[cir_outer_4], title, subTitle);

        } else if (v.getId() == fm_outer_5.getId() || v.getId() == fm_outer_5_1.getId() || v.getId() == fm_outer_5_2.getId()) {
            tv_value.setText(strValue[cir_outer_5]);
            String title = strTitleValue[0];
            String subTitle = strSubValue[cir_outer_5];
            tv_value1.setText(String.format("4 %s %s", title, subTitle));
            instance.ringFinalValue4 = String.format("4. %s: %s %s",strValue[cir_outer_5], title, subTitle);

        } else if (v.getId() == fm_outer_6.getId() || v.getId() == fm_outer_6_1.getId() || v.getId() == fm_outer_6_2.getId()) {
            tv_value.setText(strValue[cir_outer_6]);
            String title = strTitleValue[0];
            String subTitle = strSubValue[cir_outer_6];
            tv_value1.setText(String.format("3 %s %s", title, subTitle));
            instance.ringFinalValue3 = String.format("3. %s: %s %s",strValue[cir_outer_6], title, subTitle);

        }

        return true;

    }

    @Override
    public boolean onLongClick(View view) {

//        isLongPressed = true;
//        tv_value.setVisibility(View.VISIBLE);
//        if (view.getId() == fm_center_1.getId()) {
//            tv_value.setText(strValue[cir_center_1]);
//        } else if (view.getId() == fm_center_2.getId()) {
//            tv_value.setText(strValue[cir_center_2]);
//        } else if (view.getId() == fm_center_3.getId()) {
//            tv_value.setText(strValue[cir_center_3]);
//        } else if (view.getId() == fm_center_4.getId()) {
//            tv_value.setText(strValue[cir_center_4]);
//        } else if (view.getId() == fm_middle_1.getId() || view.getId() == fm_middle_1_1.getId()) {
//            tv_value.setText(strValue[cir_middle_1]);
//        } else if (view.getId() == fm_middle_2.getId() || view.getId() == fm_middle_2_1.getId()) {
//            tv_value.setText(strValue[cir_middle_2]);
//        } else if (view.getId() == fm_middle_3.getId() || view.getId() == fm_middle_3_1.getId()) {
//            tv_value.setText(strValue[cir_middle_3]);
//        } else if (view.getId() == fm_middle_4.getId() || view.getId() == fm_middle_4_1.getId()) {
//            tv_value.setText(strValue[cir_middle_4]);
//        } else if (view.getId() == fm_middle_5.getId() || view.getId() == fm_middle_5_1.getId()) {
//            tv_value.setText(strValue[cir_middle_5]);
//        } else if (view.getId() == fm_middle_6.getId() || view.getId() == fm_middle_6_1.getId()) {
//            tv_value.setText(strValue[cir_middle_6]);
//        } else if (view.getId() == fm_outer_1.getId() || view.getId() == fm_outer_1_1.getId() || view.getId() == fm_outer_1_2.getId()) {
//            tv_value.setText(strValue[cir_outer_1]);
//        } else if (view.getId() == fm_outer_2.getId() || view.getId() == fm_outer_2_1.getId() || view.getId() == fm_outer_2_2.getId()) {
//            tv_value.setText(strValue[cir_outer_2]);
//        } else if (view.getId() == fm_outer_3.getId() || view.getId() == fm_outer_3_1.getId() || view.getId() == fm_outer_3_2.getId()) {
//            tv_value.setText(strValue[cir_outer_3]);
//        } else if (view.getId() == fm_outer_4.getId() || view.getId() == fm_outer_4_1.getId() || view.getId() == fm_outer_4_2.getId()) {
//            tv_value.setText(strValue[cir_outer_4]);
//        } else if (view.getId() == fm_outer_5.getId() || view.getId() == fm_outer_5_1.getId() || view.getId() == fm_outer_5_2.getId()) {
//            tv_value.setText(strValue[cir_outer_5]);
//        } else if (view.getId() == fm_outer_6.getId() || view.getId() == fm_outer_6_1.getId() || view.getId() == fm_outer_6_2.getId()) {
//            tv_value.setText(strValue[cir_outer_6]);
//        }

        return true;

    }

    public Bitmap getBitmapFromView(View view) {
        view.setBackgroundResource(android.R.color.transparent);
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null) {
            bgDrawable.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        view.draw(canvas);
        return returnedBitmap;
    }

    public String savePhoto(Bitmap bmp, File imageFileName) {
        Exception e;
        try {
            FileOutputStream out = new FileOutputStream(imageFileName);
            try {
                bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
                return imageFileName.toString();
            } catch (Exception e2) {
                e = e2;
                FileOutputStream fileOutputStream = out;
                e.printStackTrace();
                System.out.println("msg1" + e.getMessage());
                return "";
            }
        } catch (Exception e3) {
            e = e3;
            e.printStackTrace();
            System.out.println("msg" + e.getMessage());
            return "";
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        editPatientName.clearFocus();
    }

    public boolean isSubscribe() {
        if (!isSubscribe) {
            startActivity(new Intent(getContext(), SubscriptionActivity.class));
            getActivity().overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_bottom);
            getActivity().finish();
        }
        return isSubscribe;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (editPatientName != null)
            editPatientName.clearFocus();
    }
}