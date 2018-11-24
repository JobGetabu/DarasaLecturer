package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.design.card.MaterialCardView;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AdvertClassActivity extends AppCompatActivity {

    //region binding views
    @BindView(R.id.card_top)
    MaterialCardView cardTop;
    @BindView(R.id.ad_unit_name)
    TextView adUnitName;
    @BindView(R.id.ad_unit_code)
    TextView adUnitCode;
    @BindView(R.id.ad_course_chipgrp)
    ChipGroup adCourseChipgrp;
    @BindView(R.id.ad_status_txt)
    TextView adStatusTxt;


    //endregion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert_class);
        ButterKnife.bind(this);

    }

}
