package com.job.darasalecturer.ui;

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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.datasource.LecTeachTime;
import com.job.darasalecturer.util.LessonViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.job.darasalecturer.util.Constants.LECTEACHTIMECOL;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.main_list)
    RecyclerView mainList;
    @BindView(R.id.main_fab)
    FloatingActionButton mainFab;

    private static final String TAG = "main";

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private String userId;
    private FirebaseFirestore mFirestore;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirestoreRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(mainToolbar);

        //firebase
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = mUser.getUid();

        setUpList();
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
            case R.id.hmenu_settings:
                Toast.makeText(this, "TODO -> settings", Toast.LENGTH_SHORT).show();
                break;
        }

        return true;
    }

    private void setUpList() {

        initList();

        // Create a reference to the lecTeachTime collection
        CollectionReference lecTeachTimeRef = mFirestore.collection(LECTEACHTIMECOL);
        Query mQuery = lecTeachTimeRef
                .whereEqualTo("lecid", userId)
                .orderBy("time", Query.Direction.DESCENDING);

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
            protected void onBindViewHolder(@NonNull LessonViewHolder holder, int position, @NonNull LecTeachTime model) {

                holder.init(MainActivity.this,mFirestore);
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
        mainList.setAdapter(adapter);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        mainList.setLayoutManager(linearLayoutManager);
        mainList.setHasFixedSize(true);
    }


    @OnClick(R.id.main_fab)
    public void onFabClicked() {
    }
}
