package com.job.darasalecturer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.datasource.StudentDetails;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.util.StudentViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.job.darasalecturer.util.Constants.STUDENTDETAILSCOL;

public class AddAttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AddStudent";

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

        setUpList();
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

    private void initList() {
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        studList.setLayoutManager(linearLayoutManager);
        studList.setHasFixedSize(true);
    }
    private void setUpList(){

        initList();

        // Create a reference to the lecTeachTime collection
        CollectionReference studentDetailsRef = mFirestore.collection(STUDENTDETAILSCOL);
        Query mQuery = studentDetailsRef
                //.whereEqualTo("lecid", userId)
                .orderBy("regnumber", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<StudentDetails> options = new FirestoreRecyclerOptions.Builder<StudentDetails>()
                .setQuery(mQuery, StudentDetails.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<StudentDetails, StudentViewHolder>(options) {

            @NonNull
            @Override
            public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_attendance, parent, false);

                return new StudentViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final StudentViewHolder holder, int position, @NonNull StudentDetails model) {

                holder.init(AddAttendanceActivity.this, mFirestore,model);
                holder.setUpUi(model);

            }


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();

                Log.d(TAG, "onError: ", e);
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        studList.setAdapter(adapter);
    }
}