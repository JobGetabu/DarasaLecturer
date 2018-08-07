package com.job.darasalecturer.ui;

import android.app.ActivityManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.job.darasalecturer.R;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.viewmodel.ScannerViewModel;
import com.victor.loading.newton.NewtonCradleLoading;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.widget.Toast.LENGTH_LONG;

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

    private ScannerViewModel model;
    private ActivityManager am;
    private MenuItem pinMenu;
    private boolean pinned = true;
    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setTitle("");


        //init model
        model = ViewModelProviders.of(this).get(ScannerViewModel.class);

        am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        initPinning();
        doSnack = new DoSnack(this,ScannerActivity.this);

        initTimer();

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
            pinMenu.setIcon(R.drawable.ic_pin);
        } else {
            pinMenu.setIcon(R.drawable.ic_unpin);
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        unpin();
                        pinned = false;
                        Toast.makeText(this, "Screen Unpinned", Toast.LENGTH_SHORT).show();
                        updatePinningStatus();
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        pin();
                        pinned = true;
                        Toast.makeText(this, "Screen pinned", Toast.LENGTH_SHORT).show();
                        updatePinningStatus();
                    }
                }
                break;
            case R.id.smenu_record:
                Toast.makeText(this, "TODO -> password view", Toast.LENGTH_SHORT).show();
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
        }
    }

    @Override
    public void onBackPressed() {


        if (pinned){
            doSnack.showSnackbarDissaper("Screen is pinned", "Unlock", new android.view.View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        unpin();
                    }
                }
            });
        }else {
            super.onBackPressed();
        }
    }
}
