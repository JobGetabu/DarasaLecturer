package com.job.darasalecturer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.login_positive)
    MaterialButton loginPositive;
    @BindView(R.id.login_negative)
    MaterialButton loginNegative;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.login_positive)
    public void onLoginPositiveClicked() {

        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);

    }

    @OnClick(R.id.login_negative)
    public void onLoginNegativeClicked() {
        //TODO 1 : Take to browser to do lecturer account set up.
    }
}
