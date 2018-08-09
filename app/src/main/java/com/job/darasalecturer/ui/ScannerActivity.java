package com.job.darasalecturer.ui;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasalecturer.R;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.viewmodel.ScannerViewModel;
import com.victor.loading.newton.NewtonCradleLoading;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;
import static com.job.darasalecturer.ui.ShowPasscodeActivity.SHOWPASSCODEACTIVITYEXTRA;
import static com.job.darasalecturer.ui.ShowPasscodeActivity.SHOWPASSCODEACTIVITYEXTRA2;
import static com.job.darasalecturer.util.Constants.LECAUTHCOL;

public class ScannerActivity extends AppCompatActivity {

    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;
    @BindView(R.id.scan_loading)
    NewtonCradleLoading scanLoading;
    @BindView(R.id.scan_timer_text)
    TextView scanTimerText;
    @BindView(R.id.scan_percentage_text)
    TextView scanPercentageText;
    @BindView(R.id.scan_satisfaction_progressBar)
    ProgressBar scanSatisfactionProgressBar;

    private static final int PIN_NUMBER_REQUEST_CODE = 200;
    private static final String TAG = "Scanner";

    private static final int LOCATION_PERMISSION_ID = 1001;

    private ScannerViewModel model;
    private ActivityManager am;
    private MenuItem pinMenu;
    private boolean pinned = true;
    private DoSnack doSnack;
    private String userpasscode = null;

    private FirebaseFirestore mFirestore;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setTitle("");

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        //init model
        model = ViewModelProviders.of(this).get(ScannerViewModel.class);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        //temporary
        //initPinning();
        doSnack = new DoSnack(this, ScannerActivity.this);
        initTimer();

        // Keep the screen always on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Location permission not granted
        if (ContextCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScannerActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_ID);
            return;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.smenu, menu);
        pinMenu = menu.findItem(R.id.smenu_pin);
        updatePinningStatus();
        return super.onCreateOptionsMenu(menu);
    }

    public void updatePinningStatus() {
        if (pinned) {
            pinMenu.setIcon(R.drawable.ic_unpin);
        } else {
            pinMenu.setIcon(R.drawable.ic_pin);
        }
    }

    private void initPinning() {
        Toast.makeText(this, "Screen pinned", Toast.LENGTH_SHORT).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pin();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.smenu_pin:

                if (pinned) {
                    showPasscode();
                    if (userpasscode != null) {

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            unpin();
                        }
                        Toast.makeText(this, "Screen Unpinned", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        pin();
                        Toast.makeText(this, "Screen pinned", Toast.LENGTH_SHORT).show();
                    }
                }
                updatePinningStatus();
                break;
            case R.id.smenu_record:
                showPasscode();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (userpasscode != null) {
                        if (pinned) {
                            unpin();
                        }
                    }
                }

                if (userpasscode != null) {
                    userpasscode = null;

                    //TODO: end class officially
                    //notification will be better.
                    Toast.makeText(getApplication(), "Class attendance recorded", Toast.LENGTH_LONG).show();
                }

                break;
        }

        return true;
    }

    private void initTimer() {
        scanLoading.start();
        scanLoading.setLoadingColor(R.color.colorPrimary);


        model.getTimeLiveData().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (s != null) {
                    scanTimerText.setText(s);

                    if (s.equals("done!")) {
                        scanLoading.stop();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void pin() {
        startLockTask();
        pinned = true;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void unpin() {
        if (am.isInLockTaskMode()) {
            stopLockTask();
            pinned = false;
        } else {
            Toast.makeText(this, "Application already unpinned !", LENGTH_LONG).show();
            pinned = true;
        }
    }

    @Override
    public void onBackPressed() {


        if (pinned) {
            doSnack.showSnackbarDissaper("Screen is pinned", "Unlock", new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    showPasscode();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        if (userpasscode != null) {
                            unpin();
                            userpasscode = null;
                        }
                    }
                    updatePinningStatus();
                    ScannerActivity.super.onBackPressed();
                }
            });
        } else {
            super.onBackPressed();
        }
    }

    private void showPasscode() {
        mFirestore.collection(LECAUTHCOL).document(mAuth.getUid()).get()
                .addOnSuccessListener(this, new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        String passcode = documentSnapshot.getString("localpasscode");

                        Intent intent = new Intent(ScannerActivity.this, ShowPasscodeActivity.class);
                        intent.putExtra(SHOWPASSCODEACTIVITYEXTRA, passcode);

                        startActivityForResult(intent, PIN_NUMBER_REQUEST_CODE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                doSnack.showSnackbarDissaper("Set Password to continue", "Set", new android.view.View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ScannerActivity.this, PasscodeActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case (PIN_NUMBER_REQUEST_CODE):
                if (resultCode == Activity.RESULT_OK) {
                    userpasscode = data.getStringExtra(SHOWPASSCODEACTIVITYEXTRA2);

                }
                break;


        }
    }
}
