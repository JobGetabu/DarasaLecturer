package com.job.darasalecturer.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;
import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ShowPasscodeActivity extends AppCompatActivity {

    public static final String SHOWPASSCODEACTIVITYEXTRA = "SHOWPASSCODEACTIVITYEXTRA";
    public static final String SHOWPASSCODEACTIVITYEXTRA2 = "SHOWPASSCODEACTIVITYEXTRA2";
    @BindView(R.id.passcodeView)
    PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_passcode);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra(SHOWPASSCODEACTIVITYEXTRA) != null){

            passcodeView
                    .setPasscodeLength(4)
                    .setLocalPasscode(getIntent().getStringExtra(SHOWPASSCODEACTIVITYEXTRA) )
                    .setFirstInputTip(getResources().getString(R.string.enter_your_pin))
                    .setPasscodeType(PasscodeView.PasscodeViewType.TYPE_CHECK_PASSCODE)
                    .setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {

                            onBackPressed();
                        }

                        @Override
                        public void onSuccess(String number) {
                            //notification will be better.
                            Toast.makeText(getApplication(), "Class attendance recorded", Toast.LENGTH_LONG).show();
                            sendToMain(number);
                        }
                    });
        }
    }

    private void sendToMain(String pin) {

        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOWPASSCODEACTIVITYEXTRA2, pin);
        setResult(Activity.RESULT_OK, resultIntent);
        finish();
    }
}
