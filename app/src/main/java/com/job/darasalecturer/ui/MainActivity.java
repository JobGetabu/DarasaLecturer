package com.job.darasalecturer.ui;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.LecTeachTime;
import com.job.darasalecturer.model.LecUser;
import com.job.darasalecturer.ui.auth.WelcomeActivity;
import com.job.darasalecturer.util.Constants;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.util.LessonViewHolder;
import com.job.darasalecturer.viewmodel.AccountSetupViewModel;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.DKUTCOURSES;
import static com.job.darasalecturer.util.Constants.LECTEACHTIMECOL;
import static com.job.darasalecturer.util.Constants.LECUSERCOL;

public class MainActivity extends AppCompatActivity {

    //this works < 19
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    private static final String TAG = "main";
    private static final int REQUEST_CODE_REQUIRED_PERMISSIONS = 1;

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;
    @BindView(R.id.main_no_class)
    View noClassView;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirestoreRecyclerAdapter adapter;
    private Query mQuery = null;
    private AccountSetupViewModel model;
    private SharedPreferences mSharedPreferences;

    private SendMessageBottomSheet messageBottomSheet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);



        setSupportActionBar(mainToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this,R.drawable.ic_menu));


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
        messageBottomSheet = new SendMessageBottomSheet();

        mSharedPreferences = getSharedPreferences(getApplicationContext().getPackageName(),MODE_PRIVATE);

        // View model
        AccountSetupViewModel.Factory factory = new AccountSetupViewModel.Factory(
                MainActivity.this.getApplication(), mAuth, mFirestore);

        model = ViewModelProviders.of(MainActivity.this, factory)
                .get(AccountSetupViewModel.class);

        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // Sign in logic here.
                    sendToLogin();
                    finish();
                } else {

                    userId = mAuth.getCurrentUser().getUid();
                    uiObserver();

                    classListQuery(Calendar.getInstance());

                    //courses cache
                    getCoursesCache();

                }
            }
        });
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

    private void uiObserver() {

        model.getLecUserMediatorLiveData().observe(this, new Observer<LecUser>() {
            @Override
            public void onChanged(@Nullable LecUser lecUser) {
                if (lecUser != null) {
                    if (mSharedPreferences.getString(CURRENT_SEM_PREF_NAME,"").isEmpty()){
                        DoSnack.showSnackbar(MainActivity.this,getString(R.string.add_ur_sem), getString(R.string.add), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendToCurrentManage();
                            }
                        });
                    }

                    if (mSharedPreferences.getString(CURRENT_YEAR_PREF_NAME,"").isEmpty()){
                        DoSnack.showSnackbar(MainActivity.this,getString(R.string.add_ur_sem), getString(R.string.add), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                sendToCurrentManage();
                            }
                        });
                    }

                }else {
                    //lec account is null
                    DoSnack.showSnackbar(MainActivity.this,getString(R.string.add_ur_info), getString(R.string.add), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mAuth.signOut();
                            sendToLogin();
                        }
                    });
                }
            }
        });
    }

    private void sendToCurrentManage() {
        Intent cIntent = new Intent(this, CurrentSetupActivity.class);
        startActivity(cIntent);
        finish();
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

    private void sendToAdvertiseClass() {
       // Intent adIntent = new Intent(MainActivity.this, AdvertiseActivity.class);
        Intent adIntent = new Intent(MainActivity.this, AdvertClassActivity.class);
        startActivity(adIntent);
        //finish();
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

            case R.id.hmenu_sendmessage:
                    messageClassPicker();
                    //sendToAdvertiseClass();
                break;
        }

        return true;
    }

    private Query classListQuery(Calendar c) {


        final int day = c.get(Calendar.DAY_OF_WEEK);
        final String sDay = Constants.getDay(day);

        mFirestore.collection(LECUSERCOL).document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String currentsemester = documentSnapshot.getString("currentsemester");
                        String currentyear = documentSnapshot.getString("currentyear");

                        // Create a reference to the lecTeachTime collection
                        CollectionReference lecTeachTimeRef = mFirestore.collection(LECTEACHTIMECOL);
                        mQuery = lecTeachTimeRef
                                .whereEqualTo("lecid", userId)
                                .whereEqualTo("day", sDay)
                                .whereEqualTo("semester", currentsemester)
                                .whereEqualTo("studyyear", currentyear)
                                .orderBy("time", Query.Direction.ASCENDING);

                        setUpList(mQuery);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        // Show a snackbar on errors
                        Snackbar.make(findViewById(android.R.id.content),
                                "Update Settings or check connection.", Snackbar.LENGTH_INDEFINITE).show();
                    }
                });


        return mQuery;
    }

    private void setUpList(Query mQuery) {

        initList();

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

                holder.init(MainActivity.this, mFirestore, mAuth, model);
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
                    mainList.setVisibility(View.INVISIBLE);
                    noClassView.setVisibility(View.VISIBLE);
                } else {
                    mainList.setVisibility(View.VISIBLE);
                    noClassView.setVisibility(View.INVISIBLE);
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

                classListQuery(myCalendar);
                adapter.notifyDataSetChanged();
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

    private void showDateOfClasses(Calendar c) {

        DateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM d, ''yy"); //Wed, Jul 4, '18

        mainToolbar.setSubtitle("Showing: " + dateFormat2.format(c.getTime()));
        mainToolbar.setSubtitleTextAppearance(this, R.style.ToolbarSubtitleAppearance);

        int day = c.get(Calendar.DAY_OF_WEEK);
        int daydate = c.get(Calendar.DAY_OF_MONTH);
    }

    /** Handles user acceptance (or denial) of our permission request. */
    @CallSuper
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode != REQUEST_CODE_REQUIRED_PERMISSIONS) {
            return;
        }

        for (int grantResult : grantResults) {
            if (grantResult == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, R.string.error_missing_permissions, Toast.LENGTH_LONG).show();
                finish();
                return;
            }
        }
        recreate();
    }

    private void getCoursesCache(){
        //TODO: Change this to remote configue value
        //caches the courses when activity loads up with internet connection
        mFirestore.collection(DKUTCOURSES).document("dkut")
                .get();
    }

    private void messageClassPicker(){

        messageBottomSheet.show(getSupportFragmentManager(),SendMessageBottomSheet.TAG);
    }
}
