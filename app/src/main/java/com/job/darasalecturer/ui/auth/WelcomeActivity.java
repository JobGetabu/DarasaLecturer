package com.job.darasalecturer.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

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

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.login_negative)
    public void onLoginNegativeClicked() {

        Intent intent = new Intent(this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
}
