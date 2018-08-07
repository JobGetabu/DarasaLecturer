package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsActivity extends AppCompatActivity {

    @BindView(R.id.settings_toolbar)
    Toolbar settingsToolbar;
    @BindView(R.id.settings_switch)
    SwitchCompat settingsSwitch;
    @BindView(R.id.settings_manage_account)
    MaterialButton settingsManageAccount;
    @BindView(R.id.settings_logout)
    MaterialButton settingsLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);

        setSupportActionBar(settingsToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        
        loadUserPrefs();
    }

    private void loadUserPrefs() {
    }

    @OnClick(R.id.settings_switch)
    public void onSettingsSwitchClicked() {
    }

    @OnClick(R.id.settings_manage_account)
    public void onSettingsManageAccountClicked() {
    }

    @OnClick(R.id.settings_logout)
    public void onSettingsLogoutClicked() {
    }
}
