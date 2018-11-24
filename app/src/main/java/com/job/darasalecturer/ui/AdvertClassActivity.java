package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.card.MaterialCardView;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.model.QRParser;
import com.job.darasalecturer.util.AppStatus;
import com.job.darasalecturer.util.DoSnack;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AdvertClassActivity extends AppCompatActivity {

    //region CONSTANTS

    private static final String TAG = "Advert";
    public static final String QRPARSEREXTRA = "QRPARSEREXTRA";
    public static final String VENUEEXTRA = "VENUEEXTRA";
    public static final String LECTEACHIDEXTRA = "LECTEACHIDEXTRA";


    //endregion

    //region binding views
    @BindView(R.id.ad_card_top)
    MaterialCardView adCardTop;
    @BindView(R.id.ad_unit_name)
    TextView adUnitName;
    @BindView(R.id.ad_unit_code)
    TextView adUnitCode;
    @BindView(R.id.ad_course_chipgrp)
    ChipGroup adCourseChipgrp;
    @BindView(R.id.ad_status_txt)
    TextView adStatusTxt;
    @BindView(R.id.ad_start_scan_btn)
    Button adStartScanBtn;
    @BindView(R.id.ad_start_scan_animation_view)
    LottieAnimationView adStartScanAnimationView;
    @BindView(R.id.ad_start_scan_bck)
    ConstraintLayout adStartScanMain;
    @BindView(R.id.ad_list_single)
    ConstraintLayout adListSingleMain;
    @BindView(R.id.ad_network_bck)
    ConstraintLayout adNetworkMain;
    @BindView(R.id.ad_bck)
    ConstraintLayout adMain;


    //endregion

    private QRParser qrParser;
    private Gson gson;

    //firebase
    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_class);
        ButterKnife.bind(this);

        //region Keep the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //endregion
        init();

        //region INIT GLOBAL VARS
        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //get qrparser
        qrParser = getIntent().getParcelableExtra(QRPARSEREXTRA);
        gson = new Gson();
        setUpUi(qrParser);

        //endregion

    }

    private void init() {
        adStartScanAnimationView.setVisibility(View.GONE);
        adStartScanBtn.setBackground(DoSnack.setDrawable(this, R.drawable.round_off_btn_bg));
    }

    @OnClick(R.id.ad_start_scan_btn)
    public void onStartScanClicked() {
        //region UI SETUP
        adStartScanAnimationView.setVisibility(View.VISIBLE);
        adStartScanBtn.setBackground(DoSnack.setDrawable(this, R.drawable.round_btn_bg));
        adStartScanBtn.setText(R.string.scan_class);
        adStartScanBtn.setTextSize(16);
        adStartScanBtn.setPadding(20, 40, 20, 40);

        //endregion

        if (AppStatus.getInstance(this).isOnline()) {

        } else {
            //region UI SETUP
            adMain.setBackgroundColor(DoSnack.setColor(this, R.color.white));
            adStartScanMain.setVisibility(View.GONE);
            adNetworkMain.setVisibility(View.VISIBLE);
            adCardTop.setVisibility(View.GONE);

            //endregion
        }
    }

    private void setUpUi(QRParser qrParser) {

        adUnitName.setText(qrParser.getUnitname());
        adUnitCode.setText(qrParser.getUnitcode());
        adCourseChipgrp.removeAllViews();
        for (CourseYear s: qrParser.getCourses()){
            addCourses(s);
        }

    }

    private void addCourses(CourseYear course) {
        Chip chip = new Chip(this);
        chip.setChipText(course.getCourse());
        //chip.setCloseIconEnabled(true);
        //chip.setCloseIconResource(R.drawable.your_icon);
        //chip.setChipIconResource(R.drawable.your_icon);
        //chip.setChipBackgroundColorResource(R.color.red);
        chip.setTextAppearanceResource(R.style.ChipTextStyle);
        chip.setChipStartPadding(4f);
        chip.setChipEndPadding(4f);

        adCourseChipgrp.addView(chip);
    }
}
