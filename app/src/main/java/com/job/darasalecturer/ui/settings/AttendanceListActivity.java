package com.job.darasalecturer.ui.settings;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AttendanceListActivity extends AppCompatActivity {

    @BindView(R.id.fragment_container) ConstraintLayout fragmentContainer;
    @BindView(R.id.attn_list_toolbar) Toolbar attnListToolbar;
    @BindView(R.id.attn_list_container) LinearLayout attnListContainer;
    @BindView(R.id.atn_list) RecyclerView atnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        ButterKnife.bind(this);

    }

}
