package com.job.darasalecturer.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.job.darasalecturer.R;

import butterknife.ButterKnife;

public class ClassListActivity extends AppCompatActivity {

    public static final String TAG = "ClassList";
    public static final String COURSE_EXTRA = "COURSE_EXTRA";
    public static final String YEAROFSTUDY_EXTRA = "YEAROFSTUDY_EXTRA";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        ButterKnife.bind(this);


    }


}
