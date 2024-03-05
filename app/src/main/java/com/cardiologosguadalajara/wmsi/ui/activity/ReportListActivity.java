package com.cardiologosguadalajara.wmsi.ui.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

//import com.baoyz.swipemenulistview.BaseSwipListAdapter;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import com.cardiologosguadalajara.wmsi.R;
import com.cardiologosguadalajara.wmsi.utils.AppConstant;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.pdf.BaseFont;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ReportListActivity extends AppCompatActivity implements View.OnClickListener {
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
    @BindView(R.id.listview)
    ListView listView;

    private String patientName;
    private String WMSIValue;
    private String LVEFValue;
    private String spinPath;


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
    private int widthDevice;
    private ListView mListView;
    private ArrayList<File> fileList = new ArrayList<File>();
    ArrayAdapter<String> fileListStrings;

    public void Search_Dir(File dir) {
        String pdfPattern = ".pdf";

        File FileList[] = getFilesDir().listFiles();
        ArrayList<String> arrayList = new ArrayList<String>();
        if (FileList != null) {
            for (int i = 0; i < FileList.length; i++) {

                if (FileList[i].isDirectory()) {
                    Search_Dir(FileList[i]);
                } else {
                    if (FileList[i].getName().endsWith(pdfPattern)){
                        fileList.add(FileList[i]);
                        arrayList.add(String.valueOf(FileList[i].getName()));
                    }
                }
            }
        }
        fileListStrings = new ArrayAdapter<>(this, R.layout.textview_layout, arrayList);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        Bundle bundle = new Bundle();
        bundle.putString( FirebaseAnalytics.Param.ITEM_ID, "3");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "ReportListActivity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Text");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);



        setContentView(R.layout.report_list);
        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(ReportListActivity.this);

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


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        widthDevice = size.x;
        int heightDevice = size.y;
        Search_Dir(Environment.getExternalStorageDirectory());

        listView.setAdapter(fileListStrings);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                File filePath = new File(getFilesDir(), "/"+fileListStrings.getItem(position));
                Intent intentPdfViewer = new Intent(ReportListActivity.this, FinalReportActivity.class);
                intentPdfViewer.putExtra(AppConstant.REPORT_PATH, filePath.getPath());
                startActivity(intentPdfViewer);
            }
        });
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9,
                        0xCE)));
                // set item width
                openItem.setWidth(90);
                // set item title
                openItem.setTitle("Open");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(90);
                // set a icon
                deleteItem.setTitle("Delete");
//                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);

        listView.set.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        // open
                        break;
                    case 1:
                        // delete
                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ib_back.getId()) {
            onBackPressed();
        }
    }

}

