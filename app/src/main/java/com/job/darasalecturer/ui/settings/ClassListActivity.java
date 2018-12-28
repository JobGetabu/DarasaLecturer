package com.job.darasalecturer.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.job.darasalecturer.R;
import com.job.darasalecturer.model.StudentDetails;
import com.job.darasalecturer.ui.MainActivity;
import com.job.darasalecturer.util.StudentListVH;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.STUDENTDETAILSCOL;

public class ClassListActivity extends AppCompatActivity {

    public static final String TAG = "ClassList";
    public static final String COURSE_EXTRA = "COURSE_EXTRA";
    public static final String YEAROFSTUDY_EXTRA = "YEAROFSTUDY_EXTRA";

    @BindView(R.id.stud_list_list_toolbar)
    Toolbar studToolbar;
    @BindView(R.id.stud_list_list)
    RecyclerView studList;
    @BindView(R.id.stud_no_student)
    View noStudView;
    @BindView(R.id.stud_list_container)
    LinearLayout containerView;
    @BindView(R.id.stud_count)
    TextView studCount;


    FirestoreRecyclerAdapter adapter;
    @BindView(R.id.textView_to_current_settings)
    TextView textViewToCurrentSettings;

    @Override
    protected void onStart() {
        super.onStart();
        if (adapter != null) {
            adapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        ButterKnife.bind(this);

        setSupportActionBar(studToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(AppCompatResources.getDrawable(this, R.drawable.ic_clear));
        studToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClassListActivity.this, MainActivity.class));
                finish();
            }
        });

        String course = getIntent().getStringExtra(COURSE_EXTRA);
        int yos = getIntent().getIntExtra(YEAROFSTUDY_EXTRA, 0);

        initList();
        setUpUI(course, yos);
        setUpList(course, yos);
    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        studList.setLayoutManager(linearLayoutManager);
        studList.setHasFixedSize(true);
        studList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        studList.setAdapter(adapter);
    }

    private void setUpUI(String course, int yearofstudy) {
        studToolbar.setTitle("Class list   Year: " + yearofstudy);
        studToolbar.setSubtitle(course);
        textViewToCurrentSettings.setText(R.string.no_students_text_error);
    }

    private void setUpList(String course, int yearofstudy) {

        SharedPreferences preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        final String currentsemester = preferences.getString(CURRENT_SEM_PREF_NAME, "0");
        final String currentyear = preferences.getString(CURRENT_YEAR_PREF_NAME, "2000");

        Query query = FirebaseFirestore.getInstance().collection(STUDENTDETAILSCOL)
                .whereEqualTo("currentyear", currentyear)
                .whereEqualTo("currentsemester", currentsemester)
                .whereEqualTo("course", course)
                .whereEqualTo("yearofstudy", String.valueOf(yearofstudy))
                .orderBy("regnumber", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<StudentDetails> options = new FirestoreRecyclerOptions.Builder<StudentDetails>()
                .setQuery(query, StudentDetails.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<StudentDetails, StudentListVH>(options) {

            @NonNull
            @Override
            public StudentListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_class_list, parent, false);

                return new StudentListVH(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final StudentListVH holder, int position, @NonNull StudentDetails model) {

                holder.setUpUi(model);
            }


            @Override
            public void onError(FirebaseFirestoreException e) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();

                Log.d(TAG, "onError: ", e);
            }

            @Override
            public void onDataChanged() {
                 //Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    containerView.setVisibility(View.GONE);
                    studList.setVisibility(View.GONE);
                    noStudView.setVisibility(View.VISIBLE);
                } else {
                    containerView.setVisibility(View.VISIBLE);
                    studList.setVisibility(View.VISIBLE);
                    noStudView.setVisibility(View.GONE);
                    studCount.setText(getString(R.string.num_students,getItemCount()));
                }

            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        studList.setAdapter(adapter);
    }
}
