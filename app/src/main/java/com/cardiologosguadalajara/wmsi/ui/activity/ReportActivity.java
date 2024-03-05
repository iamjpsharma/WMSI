package com.cardiologosguadalajara.wmsi.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.cardiologosguadalajara.wmsi.utils.SharedInstance;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    ProgressDialog progressDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    private static final int PADDING = 15;
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 30,
            Font.BOLD, new BaseColor(72, 85, 99)); // Set of font family alrady present with itextPdf library.

    private static final int PICK_IMAGE_SIGNATURE = 123;
    private static final int PICK_IMAGE_LOGO = 111;
    private static final int WRITE_STORAGE_REQUEST = 565;

    @BindView(R.id.ib_back)
    ImageButton ib_back;
    @BindView(R.id.ib_download_report)
    ImageButton ib_download_report;

    @BindView(R.id.btn_upload_logo)
    Button btn_upload_logo;
    @BindView(R.id.ib_logo_remove)
    ImageButton ib_logo_remove;
    @BindView(R.id.iv_logo)
    ImageView iv_logo;
    @BindView(R.id.fm_logo)
    FrameLayout fm_logo;

    @BindView(R.id.btn_upload_signature)
    Button btn_upload_signature;
    @BindView(R.id.ib_signature_remove)
    ImageButton ib_signature_remove;
    @BindView(R.id.iv_signature)
    ImageView iv_signature;
    @BindView(R.id.fm_signature)
    FrameLayout fm_signature;

    @BindView(R.id.tvCurrentDate)
    TextView tvCurrentDate;
    @BindView(R.id.tv_patientName)
    TextView tv_patientName;
    @BindView(R.id.cl_patient)
    ConstraintLayout cl_patient;

    @BindView(R.id.iv_spin)
    ImageView iv_spin;

    @BindView(R.id.tv_wmsi_value)
    TextView tv_wmsi_value;
    @BindView(R.id.tv_lvef_value)
    TextView tv_lvef_value;

    private String patientName;
    private String WMSIValue;
    private String LVEFValue;
    private String spinPath;

    @BindView(R.id.ll_examiner)
    LinearLayoutCompat ll_examiner;
    @BindView(R.id.tv_examinerValue)
    TextView tv_examinerValue;
    @BindView(R.id.tv_phoneValue)
    TextView tv_phoneValue;
    @BindView(R.id.tv_emailValue)
    TextView tv_emailValue;
    @BindView(R.id.tv_addressValue)
    TextView tv_addressValue;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.cl_report)
    LinearLayoutCompat cl_report;
    @BindView(R.id.textView1)
    TextView textView1;
    @BindView(R.id.textView2)
    TextView textView2;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.textView7)
    TextView textView7;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView10)
    TextView textView10;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.textView12)
    TextView textView12;
    @BindView(R.id.textView13)
    TextView textView13;
    @BindView(R.id.textView14)
    TextView textView14;
    @BindView(R.id.textView15)
    TextView textView15;
    @BindView(R.id.textView16)
    TextView textView16;

    //Dialog's View
    private Dialog mDialog;
    EditText editExaminerName;
    ImageButton ib_remove_examiner;
    EditText editPhone;
    ImageButton ib_remove_phone;
    EditText editEmail;
    ImageButton ib_remove_email;
    EditText editAddress;
    ImageButton ib_remove_address;
    AppCompatButton btn_save;
    AppCompatButton btn_cancel;

    SharedPreferences mPref;
    SharedPreferences mPrefReport;
    private String logoPath = "";
    private String signaturePath = "";

    private String examinerName;
    private String emailVal;
    private String addressVal;
    private String phoneVal;
    private String wmsiVal;
    private String lvefVal;
    private String patientVAl;
    private String dateVAl;

    Font urFontName;
    Font FontNameWMSI;
    Font FontNameLVEF;
    public static final String DEST = "Report.pdf";
    private String destPath;
    private int widthDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ReportActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(ReportActivity.this);

        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/circularstd_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            urFontName = new Font(urName, 28, Font.NORMAL);
            FontNameWMSI = new Font(urName, 32, Font.NORMAL);
            urName = BaseFont.createFont("assets/circularstd_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            FontNameLVEF = new Font(urName, 52, Font.NORMAL);
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            urFontName = new Font(Font.FontFamily.TIMES_ROMAN, 24,
                    Font.NORMAL);
        }

        mPref = getSharedPreferences(AppConstant.PREF_KEY_EXAMINER, MODE_PRIVATE);
        mPrefReport = getSharedPreferences(AppConstant.PREF_KEY_REPORT, MODE_PRIVATE);

        ib_back.setOnClickListener(this);
        ib_download_report.setOnClickListener(this);
        btn_upload_logo.setOnClickListener(this);
        iv_logo.setOnClickListener(this);
        ib_logo_remove.setOnClickListener(this);
        btn_upload_signature.setOnClickListener(this);
        ib_signature_remove.setOnClickListener(this);
        iv_signature.setOnClickListener(this);
        ll_examiner.setOnClickListener(this);

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthDevice = size.x;
        int heightDevice = size.y;

        try {
            //Logo
            btn_upload_logo.getLayoutParams().height = (int) (widthDevice / 3);
            fm_logo.getLayoutParams().height = (int) (widthDevice / 3);

            //Signature
            btn_upload_signature.getLayoutParams().width = (int) (widthDevice / 2);
            btn_upload_signature.getLayoutParams().height = (int) (widthDevice / 3.5);
            fm_signature.getLayoutParams().width = (int) (widthDevice / 2);
            fm_signature.getLayoutParams().height = (int) (widthDevice / 3.5);

            //Spin
            iv_spin.getLayoutParams().width = (int) (widthDevice / 1.5);
            iv_spin.getLayoutParams().height = (int) (widthDevice / 1.5);

            String currentDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
            tvCurrentDate.setText(currentDate);

            if (getIntent().hasExtra(AppConstant.PATIENT_NAME) && getIntent().hasExtra(AppConstant.WMSI_VALUE)
                    && getIntent().hasExtra(AppConstant.LVEF_VALUE) && getIntent().hasExtra(AppConstant.SPIN_PATH)) {

                patientName = getIntent().getStringExtra(AppConstant.PATIENT_NAME);
                WMSIValue = getIntent().getStringExtra(AppConstant.WMSI_VALUE);
                LVEFValue = getIntent().getStringExtra(AppConstant.LVEF_VALUE);
                spinPath = getIntent().getStringExtra(AppConstant.SPIN_PATH);

                if (!patientName.equals("")) {
                    tv_patientName.setText(patientName);
                    cl_patient.setVisibility(View.VISIBLE);
                } else {
                    cl_patient.setVisibility(View.GONE);
                }
                tv_wmsi_value.setText(WMSIValue);
                tv_lvef_value.setText(LVEFValue);
                Glide.with(this).load(spinPath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv_spin);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        btn_upload_logo.setVisibility(View.VISIBLE);
        fm_logo.setVisibility(View.GONE);

        btn_upload_signature.setVisibility(View.VISIBLE);
        fm_signature.setVisibility(View.GONE);

        setData();
    }
    private Animation outToLeftAnimation(int duration) {
        Animation outtoLeft = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, -1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoLeft.setDuration(duration);
        outtoLeft.setInterpolator(new AccelerateInterpolator());
        return outtoLeft;
    }

    private Animation outToRightAnimation(int duration) {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(duration);
        outtoRight.setInterpolator(new AccelerateInterpolator());
        return outtoRight;
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == ib_back.getId()) {
            onBackPressed();
        } else if (v.getId() == ib_download_report.getId()) {

            mPrefReport.edit().putString(AppConstant.EXAMINER_VALUE, tv_examinerValue.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.PHONE_VALUE, tv_phoneValue.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.EMAIL_VALUE, tv_emailValue.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.ADDRESS_VALUE, tv_addressValue.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.WMSI_VALUE, tv_wmsi_value.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.LVEF_VALUE, tv_lvef_value.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.PATIENT_NAME, tv_patientName.getText().toString()).apply();
            mPrefReport.edit().putString(AppConstant.LOGO_PATH, logoPath).apply();
            mPrefReport.edit().putString(AppConstant.SIGNATURE_PATH, signaturePath).apply();
            mPrefReport.edit().putString(AppConstant.SPIN_PATH, spinPath).apply();
            mPrefReport.edit().putString(AppConstant.DATE_VALUE, tvCurrentDate.getText().toString()).apply();


            mPrefReport = getSharedPreferences(AppConstant.PREF_KEY_REPORT, MODE_PRIVATE);

            examinerName = mPrefReport.getString(AppConstant.EXAMINER_VALUE, "");
            emailVal = mPrefReport.getString(AppConstant.EMAIL_VALUE, "");
            addressVal = mPrefReport.getString(AppConstant.ADDRESS_VALUE, "");
            phoneVal = mPrefReport.getString(AppConstant.PHONE_VALUE, "");
            wmsiVal = mPrefReport.getString(AppConstant.WMSI_VALUE, "");
            lvefVal = mPrefReport.getString(AppConstant.LVEF_VALUE, "");
            patientVAl = mPrefReport.getString(AppConstant.PATIENT_NAME, "");
            logoPath = mPrefReport.getString(AppConstant.LOGO_PATH, "");
            signaturePath = mPrefReport.getString(AppConstant.SIGNATURE_PATH, "");
            spinPath = mPrefReport.getString(AppConstant.SPIN_PATH, "");
            dateVAl = mPrefReport.getString(AppConstant.DATE_VALUE, "");

            if (logoPath.equalsIgnoreCase("")) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.header);

                if (bitmap.getWidth() < bitmap.getHeight()) {
                    fm_logo.getLayoutParams().height = widthDevice - 16;
                } else {
                    fm_logo.getLayoutParams().height = (int) (widthDevice / 3);
                }

                fm_logo.setVisibility(View.VISIBLE);
                ib_logo_remove.setVisibility(View.GONE);
                btn_upload_logo.setVisibility(View.GONE);

                Glide.with(ReportActivity.this).load(R.drawable.header).into(iv_logo);
            } else {
                ib_logo_remove.setVisibility(View.GONE);
                btn_upload_logo.setVisibility(View.GONE);

            }

            if (signaturePath.equalsIgnoreCase("")) {
                btn_upload_signature.setVisibility(View.GONE);
                fm_signature.setVisibility(View.GONE);
                ib_signature_remove.setVisibility(View.GONE);
            } else {
                ib_signature_remove.setVisibility(View.GONE);
                btn_upload_signature.setVisibility(View.GONE);
            }

            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

//                    new AsyncTaskSPDF().execute();
                    takeScreenShotXMLPDF2();
                    setData();

                }
            }, 2000);


        } else if (v.getId() == btn_upload_logo.getId() || v.getId() == iv_logo.getId()) {
            openGallery(PICK_IMAGE_LOGO);
        } else if (v.getId() == btn_upload_signature.getId() || v.getId() == iv_signature.getId()) {
            openGallery(PICK_IMAGE_SIGNATURE);
        } else if (v.getId() == ib_logo_remove.getId()) {
            fm_logo.setVisibility(View.GONE);
            btn_upload_logo.setVisibility(View.VISIBLE);
            logoPath = "";
            mPref.edit().putString(AppConstant.LOGO_PATH, logoPath).apply();
        } else if (v.getId() == ib_signature_remove.getId()) {
            fm_signature.setVisibility(View.GONE);
            btn_upload_signature.setVisibility(View.VISIBLE);
            signaturePath = "";
            mPref.edit().putString(AppConstant.SIGNATURE_PATH, signaturePath).apply();
        } else if (v.getId() == ll_examiner.getId()) {
            DialogBoxForExaminer(ReportActivity.this);
        }
    }

    private void takeScreenShotXMLPDF2() {
        PdfDocument document;
        document = new PdfDocument();
        final Bitmap b = takeScreenshot();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(b.getWidth(), b.getHeight(), 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ffffff"));
        canvas.drawPaint(paint);
        canvas.drawBitmap(b, 0, 0, paint);
        document.finishPage(page);
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        File filePath = new File(getFilesDir(), "/"+patientName+"_"+examinerName+"_"+ts+"_"+DEST);

        try {
            document.writeTo(new FileOutputStream(filePath));
            progressDialog.dismiss();
            Intent intentPdfViewer = new Intent(ReportActivity.this, FinalReportActivity.class);
            intentPdfViewer.putExtra(AppConstant.REPORT_PATH, filePath.getPath());
            startActivity(intentPdfViewer);


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case PICK_IMAGE_LOGO:
                Uri imageUri = null;
                if (data != null) {
                    imageUri = data.getData();
                    btn_upload_logo.setVisibility(View.GONE);
                    fm_logo.setVisibility(View.VISIBLE);
                    logoPath = getRealPathFromURI(ReportActivity.this, imageUri);

                    File fileSrc = new File(getRealPathFromURI(ReportActivity.this, imageUri));
                    String extension = fileSrc.getAbsolutePath().substring(fileSrc.getAbsolutePath().lastIndexOf("."));
                    File fileDst = new File(getFilesDir(), "LogoTemp" + extension);

                    copyFile(fileSrc, fileDst);

                    Bitmap bitmap = BitmapFactory.decodeFile(fileDst.getPath());

                    if (bitmap.getWidth() < bitmap.getHeight()) {
                        fm_logo.getLayoutParams().height = widthDevice - 16;
                    } else {
                        fm_logo.getLayoutParams().height = (int) (widthDevice / 3);
                    }

                    Glide.with(ReportActivity.this).load(imageUri).into(iv_logo);
                    mPref.edit().putString(AppConstant.LOGO_PATH, fileDst.getPath()).apply();

                }
                break;

            case PICK_IMAGE_SIGNATURE:
                Uri imageUri1 = null;
                if (data != null) {
                    imageUri1 = data.getData();
                    btn_upload_signature.setVisibility(View.GONE);
                    fm_signature.setVisibility(View.VISIBLE);
                    signaturePath = getRealPathFromURI(ReportActivity.this, imageUri1);
                    Glide.with(ReportActivity.this).load(imageUri1).into(iv_signature);

                    File fileSrc = new File(getRealPathFromURI(ReportActivity.this, imageUri1));
                    String extension = fileSrc.getAbsolutePath().substring(fileSrc.getAbsolutePath().lastIndexOf("."));
                    File fileDst = new File(getFilesDir(), "SignatureTemp" + extension);

                    copyFile(fileSrc, fileDst);

                    mPref.edit().putString(AppConstant.SIGNATURE_PATH, fileDst.getPath()).apply();
                }
                break;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @AfterPermissionGranted(WRITE_STORAGE_REQUEST)
    private void openGallery(int code) {
        String[] perms = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_PICK);
            startActivityForResult(Intent.createChooser(intent, "Select Image"), code);
        } else {
            EasyPermissions.requestPermissions(this, "This app may not work correctly without the requested permission.!",
                    WRITE_STORAGE_REQUEST, perms);
        }
    }

    public void DialogBoxForExaminer(Context context) {


        mDialog = new Dialog(context);
        try {
            Objects.requireNonNull(mDialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        } catch (Exception ignored) {
        }
        mDialog.setCancelable(false);
        mDialog.setContentView(R.layout.dialog_examiner);

        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        editExaminerName = mDialog.findViewById(R.id.editExaminerName);
        ib_remove_examiner = mDialog.findViewById(R.id.ib_remove_examiner);
        editPhone = mDialog.findViewById(R.id.editPhone);
        ib_remove_phone = mDialog.findViewById(R.id.ib_remove_phone);
        editEmail = mDialog.findViewById(R.id.editEmail);
        ib_remove_email = mDialog.findViewById(R.id.ib_remove_email);
        editAddress = mDialog.findViewById(R.id.editAddress);
        ib_remove_address = mDialog.findViewById(R.id.ib_remove_address);
        btn_save = mDialog.findViewById(R.id.btn_save);
        btn_cancel = mDialog.findViewById(R.id.btn_cancel);

        editExaminerName.addTextChangedListener(EditListener());
        editPhone.addTextChangedListener(EditListener());
        editEmail.addTextChangedListener(EditListener());
        editAddress.addTextChangedListener(EditListener());

        ib_remove_examiner.setOnClickListener(ButtonClickListener());
        ib_remove_phone.setOnClickListener(ButtonClickListener());
        ib_remove_email.setOnClickListener(ButtonClickListener());
        ib_remove_address.setOnClickListener(ButtonClickListener());
        btn_save.setOnClickListener(ButtonClickListener());
        btn_cancel.setOnClickListener(ButtonClickListener());

        String examinerName = mPref.getString(AppConstant.EXAMINER_VALUE, "");
        editExaminerName.setText(examinerName);
        String phoneValue = mPref.getString(AppConstant.PHONE_VALUE, "");
        editPhone.setText(phoneValue);
        String emailValue = mPref.getString(AppConstant.EMAIL_VALUE, "");
        editEmail.setText(emailValue);
        String addressValue = mPref.getString(AppConstant.ADDRESS_VALUE, "");
        editAddress.setText(addressValue);


        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            int width = metrics.widthPixels;
            int height = metrics.heightPixels;

            mDialog.getWindow().setLayout(width - width / 16, ViewGroup.LayoutParams.WRAP_CONTENT);

        } catch (Exception ignored) {
        }

    }

    private View.OnClickListener ButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == ib_remove_examiner.getId()) {
                    editExaminerName.setText("");
                } else if (v.getId() == ib_remove_phone.getId()) {
                    editPhone.setText("");
                } else if (v.getId() == ib_remove_email.getId()) {
                    editEmail.setText("");
                } else if (v.getId() == ib_remove_address.getId()) {
                    editAddress.setText("");
                } else if (v.getId() == btn_save.getId()) {
                    mPref.edit().putString(AppConstant.EXAMINER_VALUE, editExaminerName.getText().toString()).apply();
                    mPref.edit().putString(AppConstant.PHONE_VALUE, editPhone.getText().toString()).apply();
                    mPref.edit().putString(AppConstant.EMAIL_VALUE, editEmail.getText().toString()).apply();
                    mPref.edit().putString(AppConstant.ADDRESS_VALUE, editAddress.getText().toString()).apply();
                    updateExaminer();
                    mDialog.dismiss();
                } else if (v.getId() == btn_cancel.getId()) {
                    mDialog.dismiss();
                }
            }
        };
    }

    private void updateExaminer() {
        String examinerName = mPref.getString(AppConstant.EXAMINER_VALUE, "");
        tv_examinerValue.setText(examinerName);
        String phoneValue = mPref.getString(AppConstant.PHONE_VALUE, "");
        tv_phoneValue.setText(phoneValue);
        String emailValue = mPref.getString(AppConstant.EMAIL_VALUE, "");
        tv_emailValue.setText(emailValue);
        String addressValue = mPref.getString(AppConstant.ADDRESS_VALUE, "");
        tv_addressValue.setText(addressValue);

    }


    private void setData() {
        String examinerName = mPref.getString(AppConstant.EXAMINER_VALUE, "");
        tv_examinerValue.setText(examinerName);
        String phoneValue = mPref.getString(AppConstant.PHONE_VALUE, "");
        tv_phoneValue.setText(phoneValue);
        String emailValue = mPref.getString(AppConstant.EMAIL_VALUE, "");
        tv_emailValue.setText(emailValue);
        String addressValue = mPref.getString(AppConstant.ADDRESS_VALUE, "");
        tv_addressValue.setText(addressValue);
         logoPath = mPref.getString(AppConstant.LOGO_PATH, "");
        if (!logoPath.equals("")) {
            btn_upload_logo.setVisibility(View.GONE);
            fm_logo.setVisibility(View.VISIBLE);
            ib_logo_remove.setVisibility(View.VISIBLE);

            Bitmap bitmap = BitmapFactory.decodeFile(logoPath);
            if (bitmap.getWidth() < bitmap.getHeight()) {
                fm_logo.getLayoutParams().height = widthDevice - 16;
            } else {
                fm_logo.getLayoutParams().height = (int) (widthDevice / 3);
            }

            Glide.with(this).load(logoPath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv_logo);
        } else {
            fm_logo.setVisibility(View.GONE);
            btn_upload_logo.setVisibility(View.VISIBLE);
        }

        signaturePath = mPref.getString(AppConstant.SIGNATURE_PATH, "");
        if (!signaturePath.equals("")) {
            btn_upload_signature.setVisibility(View.GONE);
            fm_signature.setVisibility(View.VISIBLE);
            ib_signature_remove.setVisibility(View.VISIBLE);
            Glide.with(this).load(signaturePath).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(iv_signature);
        } else {
            fm_signature.setVisibility(View.GONE);
            btn_upload_signature.setVisibility(View.VISIBLE);
        }

        SharedInstance instance = SharedInstance.getInstance();
        textView1.setText(instance.ringFinalValue1);
        textView2.setText(instance.ringFinalValue2);
        textView3.setText(instance.ringFinalValue3);
        textView4.setText(instance.ringFinalValue4);
        textView5.setText(instance.ringFinalValue5);
        textView6.setText(instance.ringFinalValue6);
        textView7.setText(instance.ringFinalValue7);
        textView8.setText(instance.ringFinalValue8);
        textView9.setText(instance.ringFinalValue9);
        textView10.setText(instance.ringFinalValue10);
        textView11.setText(instance.ringFinalValue11);
        textView12.setText(instance.ringFinalValue12);
        textView13.setText(instance.ringFinalValue13);
        textView14.setText(instance.ringFinalValue14);
        textView15.setText(instance.ringFinalValue15);
        textView16.setText(instance.ringFinalValue16);
    }

    private TextWatcher EditListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editExaminerName.getText().toString().length() > 0) {
                    ib_remove_examiner.setVisibility(View.VISIBLE);
                } else {
                    ib_remove_examiner.setVisibility(View.INVISIBLE);
                }

                if (editPhone.getText().toString().length() > 0) {
                    ib_remove_phone.setVisibility(View.VISIBLE);
                } else {
                    ib_remove_phone.setVisibility(View.INVISIBLE);
                }

                if (editEmail.getText().toString().length() > 0) {
                    ib_remove_email.setVisibility(View.VISIBLE);
                } else {
                    ib_remove_email.setVisibility(View.INVISIBLE);
                }

                if (editAddress.getText().toString().length() > 0) {
                    ib_remove_address.setVisibility(View.VISIBLE);
                } else {
                    ib_remove_address.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
    }

    private class AsyncTaskSPDF extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            createPdf();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            File file = new File(destPath);

            Intent intent = new Intent(ReportActivity.this, FinalReportActivity.class);
            intent.putExtra(AppConstant.REPORT_PATH, file.getPath());
            startActivity(intent);

            progressDialog.dismiss();
        }
    }

    public void createPdf() {
        Long tsLong = System.currentTimeMillis()/1000;
        String ts = tsLong.toString();
        String root = getFilesDir() + "/WMSI/MyPDFDoc/";
        final File dir = new File(root);
        if (!dir.exists())
            dir.mkdirs();
        try {
            destPath = dir.getPath()+"/"+"ts"+DEST;
            PrintDocument(dir.getPath()+"/"+"ts"+DEST);
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }

    void addContentForCategory(Document document, Context context) throws DocumentException {
        Chapter catPart = new Chapter(1);
        Paragraph title = new Paragraph("Account Statement", catFont);
        title.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(title, 1);
        document.add(catPart);
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AppConstant.hideSoftKeyboard(ReportActivity.this);
        return super.dispatchTouchEvent(ev);
    }


    public Bitmap takeScreenshot() {
        ScrollView iv = (ScrollView) findViewById(R.id.scrollView);
        Bitmap bitmap = Bitmap.createBitmap(
                iv.getChildAt(0).getWidth(),
                iv.getChildAt(0).getHeight(),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);

        Drawable bgDrawable = iv.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);

        iv.getChildAt(0).draw(canvas);
        return bitmap;
    }

    public void copyFile(File sourceLocation, File targetLocation) {
        // your sd card
        String sdCard = Environment.getExternalStorageDirectory().toString();

        // just to take note of the location sources
        Log.v("TAG", "sourceLocation: " + sourceLocation);
        Log.v("TAG", "targetLocation: " + targetLocation);

        try {
            if (sourceLocation.exists()) {
                InputStream in = new FileInputStream(sourceLocation);
                OutputStream out = new FileOutputStream(targetLocation);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                in.close();
                out.close();
                Log.v("TAG", "Copy file successful.");
            } else {
                Log.v("TAG", "Copy file failed. Source file missing.");
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void PrintDocument(String dest) throws IOException, java.io.IOException {
        try {
            Document document = new Document(PageSize.A4, 8, 8, 0, 0);

            PdfWriter.getInstance(document, new FileOutputStream(dest));

            document.open();

            addMetaData(document);

            addTitlePage(document);

            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addMetaData(Document document) {
        document.addTitle("My first PDF");
    }

    private void addTitlePage(Document document)
            throws DocumentException {
        try {
            Bitmap bmp1 = takeScreenshot();
            ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
            bmp1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
            Image imageLogo = Image.getInstance(stream1.toByteArray());

            float scalerLogo = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / imageLogo.getWidth()) * 40;
            imageLogo.setAlignment(Element.ALIGN_CENTER);
            imageLogo.scalePercent(scalerLogo);

            document.add(imageLogo);

        } catch (IOException e) {
            e.printStackTrace();
        }
        document.newPage();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setData();
    }
}