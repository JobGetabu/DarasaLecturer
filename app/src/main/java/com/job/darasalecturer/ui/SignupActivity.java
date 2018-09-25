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

public class SignupActivity extends AppCompatActivity {

    @BindView(R.id.signup_toolbar)
    Toolbar signupToolbar;
    @BindView(R.id.signup_input_email)
    TextInputLayout signupInputEmail;
    @BindView(R.id.signup_input_password)
    TextInputLayout signupInputPassword;
    @BindView(R.id.signup_btn)
    TextView signupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.signup_btn)
    public void onViewClicked() {
    }
}
