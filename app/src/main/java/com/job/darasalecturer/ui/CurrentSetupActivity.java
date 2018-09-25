package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurrentSetupActivity extends AppCompatActivity {

    @BindView(R.id.current_toolbar)
    Toolbar currentToolbar;
    @BindView(R.id.current_semester)
    TextInputLayout currentSemester;
    @BindView(R.id.current_year)
    TextInputLayout currentYear;
    @BindView(R.id.current_acadyear)
    TextInputLayout currentAcadyear;
    @BindView(R.id.current_btn)
    TextView currentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_setup);
        ButterKnife.bind(this);

        setSupportActionBar(currentToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));
    }

    @OnClick(R.id.current_btn)
    public void onViewClicked() {
    }
}
