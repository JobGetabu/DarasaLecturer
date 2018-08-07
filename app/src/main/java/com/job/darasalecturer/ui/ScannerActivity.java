package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ScannerActivity extends AppCompatActivity {

    @BindView(R.id.scan_toolbar)
    Toolbar scanToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        ButterKnife.bind(this);

        setSupportActionBar(scanToolbar);
        getSupportActionBar().setTitle("");
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
}
