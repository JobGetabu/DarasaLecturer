package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.design.chip.ChipGroup;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.job.darasalecturer.R;
import com.job.darasalecturer.util.DoSnack;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAttendanceActivity extends AppCompatActivity {

    @BindView(R.id.stud_toolbar)
    Toolbar studToolbar;
    @BindView(R.id.stud_chipgroup)
    ChipGroup studChipgroup;
    @BindView(R.id.stud_list)
    RecyclerView studList;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirestoreRecyclerAdapter adapter;

    private MenuItem saveMenu;
    private boolean anySelected;
    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_attendance);
        ButterKnife.bind(this);

        setSupportActionBar(studToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_back));

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        doSnack = new DoSnack(this, AddAttendanceActivity.this);
    }

    public void updateSaveStatus() {
        if (anySelected) {
            saveMenu.setVisible(true);
        } else {
            saveMenu.setVisible(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.amenu, menu);
        saveMenu = menu.findItem(R.id.amenu_save);
        updateSaveStatus();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();

        switch (id){
            case R.id.amenu_save:
                break;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (adapter != null) {
            adapter.stopListening();
        }
    }
}
