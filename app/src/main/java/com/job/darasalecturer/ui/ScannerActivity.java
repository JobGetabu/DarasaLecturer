package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.job.darasalecturer.R;
import com.victor.loading.newton.NewtonCradleLoading;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity {

    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;
    @BindView(R.id.scan_loading)
    NewtonCradleLoading scanLoading;
    @BindView(R.id.scan_timer_text)
    TextView scanTimerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setTitle("");

        initTimer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.hmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.hmenu_logout:

                break;
            case R.id.hmenu_settings:
                Toast.makeText(this, "TODO -> settings", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void initTimer(){
        scanLoading.start();
        scanLoading.setLoadingColor(R.color.colorPrimary);

        // 1min = 60 sec = 60000ms
        new CountDownTimer(120000, 1000) {

            public void onTick(long millisUntilFinished) {
                scanTimerText.setText(toMinutes(millisUntilFinished) +" : " + toSec(millisUntilFinished));
            }

            public void onFinish() {
                scanTimerText.setText("done!");
                scanLoading.stop();
            }
        }.start();
    }

    private String toMinutes(long millisUntilFinished){
       long min =  (millisUntilFinished) / (1000 * 60);
       return String.valueOf(min);
    }

    private String toSec(long millisUntilFinished){
        long remainedSecs = millisUntilFinished / 1000;
        return String.valueOf((remainedSecs % 60));
    }
}
