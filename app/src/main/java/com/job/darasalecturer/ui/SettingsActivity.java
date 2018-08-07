package com.job.darasalecturer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.settings_toolbar)
    Toolbar settingsToolbar;

    @BindView(R.id.settings_manage_account)
    MaterialButton settingsManageAccount;
    @BindView(R.id.settings_logout)
    MaterialButton settingsLogout;
    @BindView(R.id.settings_password)
    MaterialButton settingsPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

    }

    @OnClick(R.id.settings_manage_account)
    public void onSettingsManageAccountClicked() {
    }

    @OnClick(R.id.settings_logout)
    public void onSettingsLogoutClicked() {
    }

    @OnClick(R.id.settings_password)
    public void onPasswordClicked() {
        Intent intent = new Intent(SettingsActivity.this, PasscodeActivity.class);
        startActivity(intent);
    }
}
