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
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.gkemon.XMLtoPDF.PdfGenerator;
import com.gkemon.XMLtoPDF.PdfGeneratorListener;
import com.gkemon.XMLtoPDF.model.FailureResponse;
import com.gkemon.XMLtoPDF.model.SuccessResponse;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class ReportActivityCopy extends AppCompatActivity implements View.OnClickListener {

    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 30,
            Font.BOLD, new BaseColor(72, 85, 99)); // Set of font family alrady present with itextPdf library.
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 14,
            Font.NORMAL, BaseColor.RED);
    private static Font subFontGreen = new Font(Font.FontFamily.UNDEFINED, 20,
            Font.NORMAL, BaseColor.GREEN);

    private static Font subFontRed = new Font(Font.FontFamily.UNDEFINED, 20,
            Font.BOLD, BaseColor.RED);
    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, new BaseColor(72, 85, 99));
    private static Font smallBold2 = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font smallHeader = new Font(Font.FontFamily.TIMES_ROMAN, 20,
            Font.BOLD, new BaseColor(72, 85, 99));
    private static Font smallBoldCredit = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, new BaseColor(26, 169, 94));
    private static Font smallBoldDebit = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD, new BaseColor(223, 7, 52));

    private static Font smallNormalCredit = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, new BaseColor(26, 169, 94));

    private static Font smallNormalDebit = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.NORMAL, new BaseColor(223, 7, 52));


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
    public static final String DEST = "/Report.pdf";
    private String destPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ButterKnife.bind(this);


        BaseFont urName = null;
        try {
            urName = BaseFont.createFont("assets/circularstd_medium.otf", "UTF-8", BaseFont.EMBEDDED);
            urFontName = new Font(urName, 26, Font.NORMAL);
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
//        mPref.edit().clear().apply();


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
        int widthDevice = size.x;
        int heightDevice = size.y;


        try {
            //Logo
//            btn_upload_logo.getLayoutParams().width = widthDevice / 2;
            btn_upload_logo.getLayoutParams().height = widthDevice / 3;
//            fm_logo.getLayoutParams().width = (int) (widthDevice / 2);
//            fm_logo.getLayoutParams().height = widthDevice / 3;

//            fm_logo.getLayoutParams().width = (int) (widthDevice / 1.2);
//            fm_logo.getLayoutParams().height = widthDevice / 3;

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


            if (!signaturePath.equals("")) {
                ib_signature_remove.setVisibility(View.GONE);
            } else {
                btn_upload_logo.setVisibility(View.GONE);
                fm_logo.setVisibility(View.VISIBLE);
                iv_signature.setImageResource(R.drawable.logo_cardiologos);
            }

            if (!logoPath.equals("")) {
                ib_logo_remove.setVisibility(View.GONE);
            } else {
                btn_upload_logo.setVisibility(View.GONE);
                fm_signature.setVisibility(View.VISIBLE);
                iv_logo.setImageResource(R.drawable.logo_cardiologos);
            }

//            new AsyncTaskSPDF().execute();

            takeScreenShotXMLPDF2();
            setData();

        } else if (v.getId() == btn_upload_logo.getId() || v.getId() == iv_logo.getId()) {
            openGallery(PICK_IMAGE_LOGO);
        } else if (v.getId() == btn_upload_signature.getId() || v.getId() == iv_signature.getId()) {
            openGallery(PICK_IMAGE_SIGNATURE);
        } else if (v.getId() == ib_logo_remove.getId()) {
            fm_logo.setVisibility(View.GONE);
            btn_upload_logo.setVisibility(View.VISIBLE);
        } else if (v.getId() == ib_signature_remove.getId()) {
            fm_signature.setVisibility(View.GONE);
            btn_upload_signature.setVisibility(View.VISIBLE);
        } else if (v.getId() == ll_examiner.getId()) {
            DialogBoxForExaminer(ReportActivityCopy.this);
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
        canvas.drawBitmap(b, 0, 0, null);
        document.finishPage(page);

        File filePath = new File(getFilesDir(), "Report.pdf");

        try {
            document.writeTo(new FileOutputStream(filePath));

            // Create Temp File to save Pdf To
//            final File savedPDFFile = new File(getFilesDir(), "/pdfpdf.pdf");
//// Generate Pdf From Html
//
//            String imagePath = "file://data/" + logoPath;
//            String html = "<img src=\"" + imagePath + "\">";
//
//            System.out.println("======imagePath : :: : " + imagePath);
//
//
//            PDFUtil.generatePDFFromHTML(getApplicationContext(), savedPDFFile, " <!DOCTYPE html>\n" +
//                    "<html>\n" +
//                    "<body>\n" +
//                    "\n" +
//                    "<h1>My First Heading</h1>\n" +
//                    "<p>My first paragraph.</p>\n" +
//                    " <a href='https://www.example.com'>This is a link</a>" +
//                    "\n"
//                    + html +
//                    "<p>Hello Webview.</p>" +
//                    "</body>\n" +
//                    "</html> ", new PDFPrint.OnPDFPrintListener() {
//                @Override
//                public void onSuccess(File file) {
//                    // Open Pdf Viewer
//                    Uri pdfUri = Uri.fromFile(savedPDFFile);
//

            Intent intentPdfViewer = new Intent(ReportActivityCopy.this, FinalReportActivity.class);
            intentPdfViewer.putExtra(AppConstant.REPORT_PATH, filePath.getPath());
            startActivity(intentPdfViewer);

//                }
//
//                @Override
//                public void onError(Exception exception) {
//                    exception.printStackTrace();
//                }
//            });


//            Intent intent = new Intent(ReportActivity.this, FinalReportActivity.class);
//            Intent intent = new Intent(ReportActivity.this, FinalReportActivity.class);
//            intent.putExtra(AppConstant.REPORT_PATH, filePath.getPath());
//            startActivity(intent);

//            Intent intent = new Intent();
//            intent.setAction(Intent.ACTION_SEND);
//            intent.setType("application/pdf");
//
//            Uri uri = FileProvider.getUriForFile(ReportActivity.this, AppConstant.PROVIDER, filePath);
//
//            intent.putExtra(Intent.EXTRA_STREAM, uri);
////            intent.putExtra(Intent.EXTRA_TEXT, AppConstants.APP_LINK);
//            startActivity(Intent.createChooser(intent, "Share"));

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void takeScreenShotXMLPDF() {
        PdfGenerator.getBuilder()
                .setContext(this)
                .fromViewIDSource()
                .fromViewID(R.layout.activity_report, ReportActivityCopy.this, R.id.scrollView)
                /* "fromViewID()" takes array of view ids and the host layout xml where the view ids are belonging.
                 * You can also invoke "fromViewIDList()" method here which takes list of view ids instead of array.*/

                .setFileName("Test-PDF")
                .setFolderName(getFilesDir().getPath())
                .openPDFafterGeneration(true)
                .build(new PdfGeneratorListener() {
                    @Override
                    public void onFailure(FailureResponse failureResponse) {
                        super.onFailure(failureResponse);
                    }

                    @Override
                    public void onStartPDFGeneration() {
                        /*When PDF generation begins to start*/
                    }

                    @Override
                    public void onFinishPDFGeneration() {
                        /*When PDF generation is finished*/
                    }

                    @Override
                    public void showLog(String log) {
                        super.showLog(log);
                    }

                    @Override
                    public void onSuccess(SuccessResponse response) {
                        super.onSuccess(response);
                    }
                });

//        PdfGenerator.getBuilder()
//                .setContext(ReportActivity.this)
//                .fromViewSource()
//                .fromView(cl_report)
//                .setFileName("Test-PDF")
//                .setFolderName(getFilesDir().getPath())
//                .openPDFafterGeneration(true)
//                .build(new PdfGeneratorListener() {
//                    @Override
//                    public void onFailure(FailureResponse failureResponse) {
//                        super.onFailure(failureResponse);
//                    }
//
//                    @Override
//                    public void showLog(String log) {
//                        super.showLog(log);
//                    }
//
//                    @Override
//                    public void onStartPDFGeneration() {
//                        /*When PDF generation begins to start*/
//                    }
//
//                    @Override
//                    public void onFinishPDFGeneration() {
//                        /*When PDF generation is finished*/
//                    }
//
//                    @Override
//                    public void onSuccess(SuccessResponse response) {
//                        super.onSuccess(response);
//                    }
//                });

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
                    logoPath = getRealPathFromURI(ReportActivityCopy.this, imageUri);
                    Glide.with(ReportActivityCopy.this).load(imageUri).into(iv_logo);

                    File fileSrc = new File(getRealPathFromURI(ReportActivityCopy.this, imageUri));
                    String extension = fileSrc.getAbsolutePath().substring(fileSrc.getAbsolutePath().lastIndexOf("."));
                    File fileDst = new File(getFilesDir(), "LogoTemp" + extension);

                    copyFile(fileSrc, fileDst);
                    mPref.edit().putString(AppConstant.LOGO_PATH, fileDst.getPath()).apply();
                }
                break;

            case PICK_IMAGE_SIGNATURE:
                Uri imageUri1 = null;
                if (data != null) {
                    imageUri1 = data.getData();
                    btn_upload_signature.setVisibility(View.GONE);
                    fm_signature.setVisibility(View.VISIBLE);
                    signaturePath = getRealPathFromURI(ReportActivityCopy.this, imageUri1);
                    Glide.with(ReportActivityCopy.this).load(imageUri1).into(iv_signature);

                    File fileSrc = new File(getRealPathFromURI(ReportActivityCopy.this, imageUri1));
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

//    private void openGallery(int code) {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_PICK);
//        startActivityForResult(Intent.createChooser(intent, "Select Image"), code);
//    }

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

        ProgressDialog progressDialog = new ProgressDialog(ReportActivityCopy.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait..");
            progressDialog.setCancelable(false);
            progressDialog.show();
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

            Intent intent = new Intent(ReportActivityCopy.this, FinalReportActivity.class);
            intent.putExtra(AppConstant.REPORT_PATH, file.getPath());
            startActivity(intent);

            progressDialog.dismiss();
        }
    }

    public void createPdf() {
        String root = getFilesDir() + "/MyPDFDoc";
        final File dir = new File(root);
        if (!dir.exists())
            dir.mkdirs();
        try {
            destPath = dir.getPath() + DEST;
            PrintDocument(dir.getPath() + DEST);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createCategoryPdf() {
        final File dir = new File(getFilesDir(), getString(R.string.app_name));
        if (!dir.exists())
            dir.mkdirs();
        try {
            File file = new File(dir.getPath(), "report.pdf");
            PrintDocumentCategory(file.getPath());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void PrintDocumentCategory(String dest) throws IOException {
        try {

            Document document = new Document(PageSize.A4, 5f, 5f, 30f, 5f);//PENGUIN_SMALL_PAPERBACK used to set the paper size
            PdfWriter.getInstance(document, new FileOutputStream(dest));
            document.open();


            addContentForCategory(document, this);
            document.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void addContentForCategory(Document document, Context context) throws DocumentException {
        Chapter catPart = new Chapter(1);
        Paragraph title = new Paragraph("Account Statement", catFont);
        title.setAlignment(Element.ALIGN_CENTER);
        addEmptyLine(title, 1);


//        saveCategoryList(catPart, context);
        document.add(catPart);

    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        AppConstant.hideSoftKeyboard(ReportActivityCopy.this);
        return super.dispatchTouchEvent(ev);
    }

    public void mainJPGPDF2(String pathImage) throws Exception {
        File root = new File(getFilesDir().getPath());
        String outputFile = "output.pdf";
        List<String> files = new ArrayList<String>();
//        files.add("page1.jpg");
        files.add(pathImage);

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(new File(root, outputFile)));
        document.open();
        for (String f : files) {
            document.newPage();
            Image image = Image.getInstance(new File(f).getAbsolutePath());
            image.setAbsolutePosition(0, 0);
            image.setBorderWidth(0);
//            image.scaleAbsolute(PageSize.A4);

            float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / image.getWidth() * 100); // 0 means you have no indentation. If you have any, change it.
            image.scalePercent(scaler);
            image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);


            document.add(image);
        }
        document.close();
    }

    public void mainJPGPDF(String pathImage) throws Exception {
        Document document = new Document();

        String directoryPath = Environment.getExternalStorageDirectory().toString();

        PdfWriter.getInstance(document, new FileOutputStream(getFilesDir() + "/example.pdf")); //  Change pdf's name.

        document.open();

        Image image = Image.getInstance(pathImage);  // Change image's name and extension.

        float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                - document.rightMargin() - 0) / image.getWidth()) * 50; // 0 means you have no indentation. If you have any, change it.
        image.scalePercent(scaler);
        image.setAlignment(Image.ALIGN_CENTER | Image.ALIGN_TOP);

        document.add(image);
        document.close();
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

    private void takeScreenShot() {
        Bitmap b = takeScreenshot();

        //Save bitmap
        String fileName = "report.jpg";
        File myPath = new File(getFilesDir(), fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
//            MediaStore.Images.Media.insertImage(getContentResolver(), b, "Screen", "screen");
            mainJPGPDF(myPath.getPath());

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public void CreatePdf(String logoPath) {
        Random r = new Random();
        int i1 = r.nextInt(10000 - 5) + 5;
        String name = "codesfor" + i1 + ".pdf";
        Document doc = new Document();
//path of the pdf file which will get saved
        File exportDir = new File(getFilesDir(), "/codesforscanner/" + name);
        try {
            PdfWriter.getInstance(doc, new FileOutputStream(exportDir));
            doc.open();
//image file path which needs to be converted
            String filename = logoPath;
            Image image = Image.getInstance(filename);
            int indentation = 0;
            float scaler = ((doc.getPageSize().getWidth() - doc.leftMargin()
                    - doc.rightMargin() - indentation) / image.getWidth()) * 100;

            image.scalePercent(scaler);
            doc.add(image);
            System.setProperty("http.agent", "Chrome");

//            Intent intent = new Intent(this,PdfViewer.class);
//            intent.putExtra("name",name);
//            startActivity(intent);
//            finish();

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            doc.close();
        }
    }

    private void takeScreenshotPNR() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {

            String mPath = getFilesDir().getPath() + "/" + now + ".jpg";
            View v1 = getWindow().getDecorView().getRootView();
            scrollView.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(scrollView.getDrawingCache());
            scrollView.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

//            openScreenshot(imageFile);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
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

    public void PrintDocument(String dest) throws IOException, IOException {
        try {
            Document document = new Document(PageSize.A4, 0, 0, 0, 0);

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
//        document.addSubject("Using iText");
//        document.addKeywords("Java, PDF, iText");
//        document.addAuthor("Lars Vogel");
//        document.addCreator("Lars Vogel");
    }

    private void addTitlePage(Document document)
            throws DocumentException {


        try {

            //Line

            Drawable d = getResources().getDrawable(R.drawable.blankline);
            BitmapDrawable bitDw = ((BitmapDrawable) d);
//            Bitmap bmp = takeScreenshot();
            Bitmap bmp = bitDw.getBitmap();
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            Image imageLine = Image.getInstance(stream.toByteArray());

            float scalerLine = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / imageLine.getWidth()) * 95;
            imageLine.setAlignment(Element.ALIGN_CENTER);
            imageLine.scalePercent(scalerLine);
//            document.add(imageLine);

//            //Logo
            if (!logoPath.equalsIgnoreCase("")) {
                Image image = Image.getInstance(logoPath);
                float scaler = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / image.getWidth()) * 100;
                image.setAlignment(Element.ALIGN_CENTER);
                image.scalePercent(scaler);
                document.add(image);

                Paragraph prefaceNameDate = new Paragraph();
                addEmptyLine(prefaceNameDate, 1);

                document.add(imageLine);
            }

            Paragraph prefaceNameDate = new Paragraph();
            document.add(prefaceNameDate);
            //Name and Date
            if (!patientVAl.equalsIgnoreCase("")) {
                addEmptyLine(prefaceNameDate, 1);
                prefaceNameDate.add(new Paragraph(getString(R.string.str_name) + " " + patientVAl, urFontName));
            } else {
                addEmptyLine(prefaceNameDate, 1);
            }
            prefaceNameDate.add(new Paragraph(getString(R.string.str_date) + " " + dateVAl, urFontName));
            addEmptyLine(prefaceNameDate, 1);
            prefaceNameDate.setIndentationLeft(15);
            prefaceNameDate.setIndentationRight(15);
            prefaceNameDate.setMultipliedLeading(2.0f);
            document.add(prefaceNameDate);

            //Spin

            File picFile = new File(spinPath);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Bitmap pix = BitmapFactory.decodeFile(picFile.getPath());
            pix.compress(Bitmap.CompressFormat.PNG, 100, baos);

            Image imageSpin = Image.getInstance(baos.toByteArray());
            float scalerSpin = ((document.getPageSize().getWidth() - document.leftMargin()
                    - document.rightMargin() - 0) / imageSpin.getWidth()) * 80;
            imageSpin.setAlignment(Element.ALIGN_CENTER);
            imageSpin.scalePercent(scalerSpin);
            document.add(imageSpin);

            Paragraph prefaceWMSI = new Paragraph(new Paragraph(getString(R.string.str_wmsi) + wmsiVal, FontNameWMSI));
            prefaceWMSI.setMultipliedLeading(2.0f);
            prefaceWMSI.setAlignment(Element.ALIGN_CENTER);
            document.add(prefaceWMSI);

            Paragraph prefaceWMSI_FVEL = new Paragraph(new Paragraph(getString(R.string.str_lvef) + lvefVal, FontNameLVEF));
            prefaceWMSI_FVEL.setAlignment(Element.ALIGN_CENTER);
            document.add(prefaceWMSI_FVEL);


            //Signature
            if (!signaturePath.equalsIgnoreCase("")) {
                Image imageSign = Image.getInstance(signaturePath);
                float scalerSign = ((document.getPageSize().getWidth() - document.leftMargin()
                        - document.rightMargin() - 0) / imageSign.getWidth()) * 50;
                imageSign.setAlignment(Element.ALIGN_CENTER);
                imageSign.scalePercent(scalerSign);
                document.add(imageSign);
            }

            Paragraph prefaceExaminerName = new Paragraph();
            addEmptyLine(prefaceExaminerName, 1);
            prefaceExaminerName.add(new Paragraph(getString(R.string.str_examiner) + " " + examinerName, urFontName));
            addEmptyLine(prefaceExaminerName, 1);
            prefaceExaminerName.setIndentationLeft(15);
            prefaceExaminerName.setIndentationRight(15);
            document.add(prefaceExaminerName);

            document.add(imageLine);

            Paragraph prefaceExaminerData = new Paragraph();
            prefaceExaminerData.add(new Paragraph(getString(R.string.str_phone_no) + " " + phoneVal, urFontName));
            addEmptyLine(prefaceExaminerData, 1);

            prefaceExaminerData.add(new Paragraph(getString(R.string.str_email) + " " + emailVal, urFontName));
            addEmptyLine(prefaceExaminerData, 1);

            prefaceExaminerData.setIndentationLeft(15);
            prefaceExaminerData.setIndentationRight(15);
            document.add(prefaceExaminerData);

            PdfPTable table = new PdfPTable(2);

            table.setWidthPercentage(95);
            table.setSplitRows(false);
            table.setSplitLate(false);

            float[] columnWidths = new float[]{23f, 75f};
            table.setWidths(columnWidths);

            PdfPCell c1 = new PdfPCell(new Phrase(getString(R.string.str_address), urFontName));
            c1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorder(Rectangle.NO_BORDER);
            c1.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(c1);

            c1 = new PdfPCell(new Phrase(addressVal, urFontName));
            c1.setHorizontalAlignment(Element.ALIGN_MIDDLE);
            c1.setBorder(Rectangle.NO_BORDER);
            table.addCell(c1);

            document.add(table);


//            prefaceExaminerData.add(new Paragraph(getString(R.string.str_address), urFontName));
////            document.add(prefaceExaminerData);
//            Paragraph prefaceExaminerAdd = new Paragraph();
//            prefaceExaminerData.add(new Paragraph(" " + addressVal, urFontName));


//            prefaceExaminerData.add(new Paragraph(getString(R.string.str_address) +" "+ addressVal, urFontName));
//            addEmptyLine(prefaceExaminerData, 1);
//            prefaceExaminerData.setIndentationLeft(15);
//            prefaceExaminerData.setIndentationRight(15);
//            document.add(prefaceExaminerData);
//TODO-------------------------------------------
//            bmp.compress(Bitmap.CompressFormat.PNG, 70, stream);
//
//            image = Image.getInstance(stream.toByteArray());
//
//            if (bmp.getWidth() > documentRect.getWidth() || bmp.getHeight() > documentRect.getHeight()) {
//
//                //bitmap is larger than page,so set bitmap's size similar to the whole page
//                image.scaleAbsolute(documentRect.getWidth(), documentRect.getHeight());
//
//            } else {
//                //bitmap is smaller than page, so add bitmap simply.
//                //[note: if you want to fill page by stretching image,
//                // you may set size similar to page as above]
//
//                image.scaleAbsolute(bmp.getWidth(), bmp.getHeight());
//            }
//
//            image.setAbsolutePosition(
//                    (documentRect.getWidth() - image.getScaledWidth()) / 2,
//                    (documentRect.getHeight() - image.getScaledHeight()) / 2);
//            image.setBorder(Image.BOX);
//            image.setBorderWidth(15);
//            document.add(image);


//            img = Image.getInstance(logoPath);
//            img.setWidthPercentage(90);
//            document.add(img);
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Paragraph preface = new Paragraph();
//        // We add one empty line
//        addEmptyLine(preface, 1);
//        // Lets write a big header
//        preface.add(new Paragraph("Title of the document", catFont));

//        addEmptyLine(preface, 1);
//        // Will create: Report generated by: _name, _date
//        preface.add(new Paragraph(
//                "Report generated by: " + System.getProperty("user.name") + ", " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
//                smallBold));
//
//        addEmptyLine(preface, 3);
//        preface.add(new Paragraph(
//                "This document describes something which is very important ",
//                smallBold));
//
//        addEmptyLine(preface, 8);

//        preface.add(new Paragraph(
//                "This document is a preliminary version and not subject to your license agreement or any other agreement with vogella.com ;-).",
//                redFont));

//        document.add(preface);
        // Start a new page
        document.newPage();
    }
}