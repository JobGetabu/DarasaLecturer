package com.job.darasalecturer.ui;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hanks.passcodeview.PasscodeView;
import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PasscodeActivity extends AppCompatActivity {


    public static final String PASSCODEEXTRA = "PASSCODEEXTRA";
    @BindView(R.id.passcodeView)
    PasscodeView passcodeView;

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseFirestore mFirestore;
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

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();


        if (getIntent().getStringExtra(PASSCODEEXTRA).isEmpty()) {
            //setting up a passcode for first time
            //check if previous data is available

            passcodeView.setListener(new PasscodeView.PasscodeViewListener() {
                @Override
                public void onFail() {

                }

                @Override
                public void onSuccess(String number) {
                    Toast.makeText(getApplication(),"Saved",Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });


        } else {

            passcodeView
                    .setPasscodeLength(4)
                    .setLocalPasscode("5555")
                    .setListener(new PasscodeView.PasscodeViewListener() {
                        @Override
                        public void onFail() {

                        }

                        @Override
                        public void onSuccess(String number) {
                            Toast.makeText(getApplication(), "finish", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        }
                    });
        }
    }
}
