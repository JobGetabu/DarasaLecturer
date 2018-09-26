package com.job.darasalecturer.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.DatePicker;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.LecTeachTime;
import com.job.darasalecturer.util.Constants;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.util.LessonViewHolder;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.job.darasalecturer.util.Constants.LECTEACHTIMECOL;

public class MainActivity extends AppCompatActivity  {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;
    @BindView(R.id.main_no_class)
    View noClassView;

    private static final String TAG = "main";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirestoreRecyclerAdapter adapter;

    private DoSnack doSnack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu));

        //subtitle
        showDateOfClasses(Calendar.getInstance());

        mainToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendToSettings();
            }
        });

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.
                    sendToLogin();
                    finish();
                } else {

                    userId = mAuth.getCurrentUser().getUid();

                    setUpList(classListQuery(Calendar.getInstance()));
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

        doSnack = new DoSnack(this, MainActivity.this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.
                    finish();
                    sendToLogin();
                } else {
                    userId = mAuth.getCurrentUser().getUid();
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);

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

    private void sendToLogin() {
        Intent loginIntent = new Intent(MainActivity.this, WelcomeActivity.class);
        startActivity(loginIntent);
        finish();
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
                mAuth.signOut();

                sendToLogin();
                break;
        }

        return true;
    }

    private Query classListQuery(Calendar c){

        int day = c.get(Calendar.DAY_OF_WEEK);
        String sDay = Constants.getDay(day);


        // Create a reference to the lecTeachTime collection
        CollectionReference lecTeachTimeRef = mFirestore.collection(LECTEACHTIMECOL);
        Query mQuery = lecTeachTimeRef
                .whereEqualTo("lecid", userId)
                .whereEqualTo("day", sDay)
                .orderBy("time", Query.Direction.ASCENDING);

        return mQuery;
    }

    private void setUpList(Query mQuery) {

        initList();

        /*// Create a reference to the lecTeachTime collection
        CollectionReference lecTeachTimeRef = mFirestore.collection(LECTEACHTIMECOL);
        Query mQuery = lecTeachTimeRef
                .whereEqualTo("lecid", userId)
                .orderBy("time", Query.Direction.ASCENDING);

        */

        FirestoreRecyclerOptions<LecTeachTime> options = new FirestoreRecyclerOptions.Builder<LecTeachTime>()
                .setQuery(mQuery, LecTeachTime.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<LecTeachTime, LessonViewHolder>(options) {

            @NonNull
            @Override
            public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_lesson, parent, false);

                return new LessonViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final LessonViewHolder holder, int position, @NonNull LecTeachTime model) {

                holder.init(MainActivity.this, mFirestore,mAuth,model);
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
                    mainList.setVisibility(View.GONE);
                    noClassView.setVisibility(View.VISIBLE);
                } else {
                    mainList.setVisibility(View.VISIBLE);
                    noClassView.setVisibility(View.GONE);
                }
            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        mainList.setAdapter(adapter);
    }

    private void sendToQr() {
        Intent qrintent = new Intent(this, ScannerActivity.class);
        qrintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        qrintent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(qrintent);
        finish();
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(linearLayoutManager);
        mainList.setHasFixedSize(true);
    }


    @OnClick(R.id.main_fab)
    public void onFabClicked() {

        final Calendar myCalendar = Calendar.getInstance();
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                //mDate = myCalendar.getTime();
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);


                //Toast.makeText(MainActivity.this, sdf.format(myCalendar.getTime()) , Toast.LENGTH_SHORT).show();
                //subtitle
                showDateOfClasses(myCalendar);
                setUpList(classListQuery(myCalendar));
            }
        };

        new DatePickerDialog(MainActivity.this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void sendToSettings() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void showDateOfClasses(Calendar c){

        DateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM d, ''yy"); //Wed, Jul 4, '18

        mainToolbar.setSubtitle("Showing: "+dateFormat2.format(c.getTime()));
        mainToolbar.setSubtitleTextAppearance(this,R.style.ToolbarSubtitleAppearance);

        int day = c.get(Calendar.DAY_OF_WEEK);
        int daydate = c.get(Calendar.DAY_OF_MONTH);
    }
}
