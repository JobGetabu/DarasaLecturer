package com.job.darasalecturer.ui;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.QRParser;
import com.job.darasalecturer.model.StudentDetails;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.util.StudentViewHolder;
import com.job.darasalecturer.viewmodel.AddStudentViewModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.STUDENTDETAILSCOL;

public class AddAttendanceActivity extends AppCompatActivity {

    private static final String TAG = "AddStudent";
    public static final String ADDATTENDANCE_EXTRA = "ADDATTENDANCE_EXTRA";

    @BindView(R.id.stud_toolbar)
    Toolbar studToolbar;
    @BindView(R.id.stud_chipgroup)
    ChipGroup studChipgroup;
    @BindView(R.id.stud_list)
    RecyclerView studList;
    @BindView(R.id.stud_no_student)
    View noStudView;

    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirestoreRecyclerAdapter adapter;

    private MenuItem saveMenu;
    private boolean anySelected;
    private DoSnack doSnack;
    private Query mQuery = null;
    private SharedPreferences mSharedPreferences;

    private AddStudentViewModel addStudentViewModel;
    private List<StudentDetails> studentDetailsList = new ArrayList<>();

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

        //init model
        addStudentViewModel = ViewModelProviders.of(this).get(AddStudentViewModel.class);

        doSnack = new DoSnack(this, AddAttendanceActivity.this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //retrieve courses

        QRParser qrParser = getIntent().getParcelableExtra(ADDATTENDANCE_EXTRA);


        constructQuery(qrParser);

        //init observers
        studentListObserver();
    }

    private void constructQuery(QRParser qrParser) {


        String currentsemester = mSharedPreferences.getString(CURRENT_SEM_PREF_NAME, "0");
        String currentyear = mSharedPreferences.getString(CURRENT_YEAR_PREF_NAME, "2000");


        ArrayList<String> courses = qrParser.getCourses();

        mQuery = mFirestore.collection(STUDENTDETAILSCOL)
                .whereEqualTo("currentyear", currentyear)
                .whereEqualTo("currentsemester", currentsemester)
                .orderBy("regnumber", Query.Direction.ASCENDING);

        /*for (String c : courses) {
            mQuery.whereEqualTo("course", c);

        }*/
        Log.d(TAG, "sem: " + qrParser.getSemester() + " year:" + qrParser.getYear());


        setUpList(mQuery);

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

        switch (id) {
            case R.id.amenu_save:
                Toast.makeText(this, "Save the attendees", Toast.LENGTH_SHORT).show();
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

    private void setUpList(Query query) {

        initList();

        FirestoreRecyclerOptions<StudentDetails> options = new FirestoreRecyclerOptions.Builder<StudentDetails>()
                .setQuery(query, StudentDetails.class)
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

                holder.init(AddAttendanceActivity.this, mFirestore, model, addStudentViewModel);
                holder.setUpUi(model);

            }


            @Override
            public void onError(FirebaseFirestoreException e) {
                // Show a snackbar on errors
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();

                Log.d(TAG, "onError: ", e);
            }

            @Override
            public void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    studList.setVisibility(View.GONE);
                    noStudView.setVisibility(View.VISIBLE);
                } else {
                    studList.setVisibility(View.VISIBLE);
                    noStudView.setVisibility(View.GONE);
                }
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        studList.setAdapter(adapter);
    }

    private void studentListObserver() {
        addStudentViewModel.getStudListMediatorLiveData().observe(this, new Observer<List<StudentDetails>>() {
            @Override
            public void onChanged(@Nullable List<StudentDetails> studentDetails) {
                if (studentDetails != null) {

                    studentDetailsList = studentDetails;

                    studChipgroup.removeAllViews();

                    for (StudentDetails stud : studentDetails) {
                        addChipStudent(stud);


                    }

                    if (studentDetails.isEmpty()) {

                        anySelected = false;
                        updateSaveStatus();
                    } else {

                        anySelected = true;
                        updateSaveStatus();
                    }

                }
            }
        });
    }

    private void addChipStudent(final StudentDetails model) {
        Chip chip = new Chip(this);
        chip.setChipText(model.getFirstname());
        //chip.setCloseIconEnabled(true);
        //chip.setCloseIconResource(R.drawable.ic_clear);

        chip.setChipBackgroundColorResource(R.color.lightGrey);
        chip.setTextAppearanceResource(R.style.ChipTextStyle2);
        chip.setChipStartPadding(4f);
        chip.setChipEndPadding(4f);

        studChipgroup.addView(chip);

    }

    @OnClick(R.id.textView_to_current_settings)
    public void onTextViewClicked() {
        Intent cIntent = new Intent(this, CurrentSetupActivity.class);
        startActivity(cIntent);
        Toast.makeText(this, "Click save to update settings", Toast.LENGTH_LONG).show();
    }
}
