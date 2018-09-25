package com.job.darasalecturer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
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
    @BindView(R.id.settings_manage_classes)
    MaterialButton settingsManageClasses;
    @BindView(R.id.settings_current)
    MaterialButton settingsCurrent;
    @BindView(R.id.settings_help)
    MaterialButton settingsHelp;
    @BindView(R.id.settings_faq)
    MaterialButton settingsFaq;

    private FirebaseAuth mAuth;

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

        //firebase
        mAuth = FirebaseAuth.getInstance();

    }

    @OnClick(R.id.settings_manage_account)
    public void onSettingsManageAccountClicked() {
        Intent mIntent = new Intent(this, AccountSetupActivity.class);
        startActivity(mIntent);
        finish();
    }

    @OnClick(R.id.settings_logout)
    public void onSettingsLogoutClicked() {
        mAuth.signOut();

        sendToLogin();
    }

    @OnClick(R.id.settings_password)
    public void onPasswordClicked() {
        Intent intent = new Intent(SettingsActivity.this, PasscodeActivity.class);
        startActivity(intent);
    }

    private void sendToLogin() {
        Intent loginIntent = new Intent(this, WelcomeActivity.class);
        startActivity(loginIntent);
        finish();
    }

    @OnClick(R.id.settings_manage_classes)
    public void onViewAddClassClicked() {
        Intent addclassIntent = new Intent(this, AddClassActivity.class);
        startActivity(addclassIntent);
        finish();
    }

    @OnClick(R.id.settings_current)
    public void onSettingsCurrentClicked() {
        Intent cIntent = new Intent(this, CurrentSetupActivity.class);
        startActivity(cIntent);
        finish();
    }

    @OnClick(R.id.settings_help)
    public void onSettingsHelpClicked() {
    }

    @OnClick(R.id.settings_faq)
    public void onSettingsFaqClicked() {
        Intent fIntent = new Intent(this, FaqActivity.class);
        startActivity(fIntent);

    }
}
