package com.job.darasalecturer.ui.settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.content.res.AppCompatResources;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.job.darasalecturer.R;
import com.job.darasalecturer.appexecutor.DefaultExecutorSupplier;
import com.job.darasalecturer.model.AttendanceListUIModel;
import com.job.darasalecturer.model.DoneClasses;
import com.job.darasalecturer.model.LecTeach;
import com.job.darasalecturer.model.LecTeachTime;
import com.job.darasalecturer.model.StudentScanClass;
import com.job.darasalecturer.ui.MainActivity;
import com.job.darasalecturer.util.StudentListVH;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.DONECLASSES;
import static com.job.darasalecturer.util.Constants.LECTEACHTIMECOL;
import static com.job.darasalecturer.util.Constants.STUDENTSCANCLASSCOL;

public class AttendanceListActivity extends AppCompatActivity {

    public static final String TAG = "atnlist";
    public static final String LECTEACH_EXTRA = "LECTEACH_EXTRA";

    @BindView(R.id.attn_list_toolbar)
    Toolbar attnListToolbar;
    @BindView(R.id.atn_list)
    RecyclerView atnList;
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.atn_stud_count)
    TextView atnStudCount;
    @BindView(R.id.atn_date)
    TextView atnDate;
    @BindView(R.id.atn_course)
    TextView atnCourse;
    @BindView(R.id.atn_btn_date)
    ImageButton atnBtnDate;
    @BindView(R.id.atn_btn_course)
    ImageButton atnBtnCourse;
    @BindView(R.id.stud_no_student)
    View noStudView;

    private FirestoreRecyclerAdapter adapter;
    private LecTeach lecTeach;
    private String lecteachid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_list);
        ButterKnife.bind(this);

        setSupportActionBar(attnListToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(AppCompatResources.getDrawable(this, R.drawable.ic_clear));
        attnListToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendanceListActivity.this, MainActivity.class));
                finish();
            }
        });

        LecTeach lecTeach = getIntent().getParcelableExtra(LECTEACH_EXTRA);

        lecteachid = lecTeach.getLecteachid();
        setUpUI(lecTeach.getUnitname(), lecTeach.getUnitcode());
        preCourseUI(lecTeach.getCombiner());
        initList();
        loadData(lecteachid);

    }

    private void initList() {
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this.getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        atnList.setLayoutManager(linearLayoutManager);
        atnList.setHasFixedSize(true);
        atnList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        atnList.setAdapter(adapter);
    }

    private void setUpUI(String unitname, String unitcode) {
        attnListToolbar.setSubtitle(unitname + " " + unitcode);
    }

    private void loadUpUI(String courseyos) {
        atnCourse.setText(courseyos);
    }

    private void preCourseUI(boolean comb) {
        if (comb) {
            atnBtnCourse.setVisibility(View.VISIBLE);
        } else {
            atnBtnCourse.setVisibility(View.GONE);
        }
    }

    private void loadData(final String lecteachid) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading attendance list...");
        pDialog.setCancelable(false);
        pDialog.show();

        FirebaseFirestore.getInstance().collection(DONECLASSES).document(lecteachid)
                .get()
                .addOnSuccessListener(
                        DefaultExecutorSupplier.getInstance().forBackgroundTasks(),
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                //done classes have loaded

                                FirebaseFirestore.getInstance().collection(LECTEACHTIMECOL)
                                        .whereEqualTo("lecteachid", lecteachid)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                pDialog.dismiss();

                                                LecTeachTime lecTeachTime = queryDocumentSnapshots.toObjects(LecTeachTime.class).get(0);

                                                if (lecTeachTime != null) {

                                                    loadUpUI(AttendanceListUIModel.loadCourses(lecTeachTime.getCourses()).get(0));
                                                    setUpList(lecTeachTime.getCourses().get(0).getCourse(),
                                                            lecTeachTime.getCourses().get(0).getYearofstudy());
                                                }
                                            }
                                        })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.e(TAG, "onFailure: ",e );
                                    }
                                });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        pDialog.setTitleText("Loading attendance list...");
                        pDialog.setCancelable(false);
                        pDialog.show();
                    }
                });
    }

    private void setUpList(String course, int yearofstudy) {

        SharedPreferences preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        final String currentsemester = preferences.getString(CURRENT_SEM_PREF_NAME, "0");
        final String currentyear = preferences.getString(CURRENT_YEAR_PREF_NAME, "2000");

        Query query = FirebaseFirestore.getInstance().collection(STUDENTSCANCLASSCOL)
                .whereEqualTo("currentyear", currentyear)
                .whereEqualTo("currentsemester", currentsemester)
                .whereEqualTo("course", course)
                .whereEqualTo("yearofstudy", String.valueOf(yearofstudy))
                .orderBy("regnumber", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<StudentScanClass> options = new FirestoreRecyclerOptions.Builder<StudentScanClass>()
                .setQuery(query, StudentScanClass.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<StudentScanClass, StudentListVH>(options) {

            @NonNull
            @Override
            public StudentListVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.single_class_list, parent, false);

                return new StudentListVH(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull final StudentListVH holder, int position, @NonNull StudentScanClass model) {

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
                    atnList.setVisibility(View.GONE);
                    noStudView.setVisibility(View.VISIBLE);
                    atnStudCount.setText(getString(R.string.num_students_vs, getItemCount(),0));
                } else {

                    atnList.setVisibility(View.VISIBLE);
                    noStudView.setVisibility(View.GONE);
                    atnStudCount.setText(getString(R.string.num_students_vs, getItemCount(),0));
                }

            }

        };

        adapter.startListening();
        adapter.notifyDataSetChanged();
        atnList.setAdapter(adapter);
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
        if (adapter != null) {
            adapter.stopListening();
        }
        super.onStop();
    }

    @OnClick(R.id.atn_btn_date)
    public void dateOnClick() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading attendance dates...");
        pDialog.setCancelable(false);
        pDialog.show();

        FirebaseFirestore.getInstance().collection(DONECLASSES).document(lecteachid)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        pDialog.dismiss();
                        DoneClasses doneClasses = documentSnapshot.toObject(DoneClasses.class);

                        //Creating the instance of PopupMenu
                        final PopupMenu popup = new PopupMenu(AttendanceListActivity.this, atnBtnDate);
                        //Inflating the Popup using xml file
                        for (String s : AttendanceListUIModel.loadDates(AttendanceListActivity.this, doneClasses)) {
                            popup.getMenu().add(s);
                        }
                        popup.setGravity(Gravity.BOTTOM);

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {

                                //courseYearList.get(position).setYearofstudy(Integer.parseInt(item.getTitle().toString()));
                                adapter.notifyDataSetChanged();
                                return true;
                            }
                        });

                        popup.show(); //showing popup menu
                    }
                });
    }

    @OnClick(R.id.atn_btn_course)
    public void dateOnCourse() {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading attendance courses...");
        pDialog.setCancelable(false);
        pDialog.show();

        FirebaseFirestore.getInstance().collection(LECTEACHTIMECOL)
                .whereEqualTo("lecteachid", lecteachid)
                .limit(1)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        final LecTeachTime lecTeachTime = queryDocumentSnapshots.toObjects(LecTeachTime.class).get(0);
                        if (lecTeachTime != null) {

                            pDialog.dismiss();

                            //Creating the instance of PopupMenu
                            final PopupMenu popup = new PopupMenu(AttendanceListActivity.this, atnBtnDate);
                            //Inflating the Popup using xml file
                            for (String s : AttendanceListUIModel.loadCourses(lecTeachTime.getCourses())) {
                                popup.getMenu().add(s);
                            }
                            popup.setGravity(Gravity.BOTTOM);

                            //registering popup with OnMenuItemClickListener
                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                public boolean onMenuItemClick(MenuItem item) {

                                    int z = AttendanceListUIModel.coursePos(lecTeachTime.getCourses(),item.getTitle().toString());
                                    //loadUpUI(AttendanceListUIModel.loadCourses(lecTeachTime.getCourses()).get(z));

                                    atnCourse.setText(item.getTitle().toString());

                                    setUpList(lecTeachTime.getCourses().get(z).getCourse(), lecTeachTime.getCourses().get(z).getYearofstudy());

                                    adapter.notifyDataSetChanged();
                                    return true;
                                }
                            });

                            popup.show(); //showing popup menu
                        }
                    }
                });
    }
}
