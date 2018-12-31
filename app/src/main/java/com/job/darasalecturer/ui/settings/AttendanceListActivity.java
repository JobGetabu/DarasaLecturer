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
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.job.darasalecturer.R;
import com.job.darasalecturer.appexecutor.DefaultExecutorSupplier;
import com.job.darasalecturer.model.AttendanceListUIModel;
import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.model.IconPowerMenuCourse;
import com.job.darasalecturer.model.IconPowerMenuDate;
import com.job.darasalecturer.model.LecTeach;
import com.job.darasalecturer.model.LecTeachTime;
import com.job.darasalecturer.model.SavedClasses;
import com.job.darasalecturer.model.StudentScanClass;
import com.job.darasalecturer.ui.MainActivity;
import com.job.darasalecturer.util.DoSnack;
import com.job.darasalecturer.util.PowerMenuUtils;
import com.job.darasalecturer.util.StudentListVH;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.LECTEACHTIMECOL;
import static com.job.darasalecturer.util.Constants.SAVEDCLASSESCOL;
import static com.job.darasalecturer.util.Constants.STUDENTDETAILSCOL;
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
    private CustomPowerMenu customPowerMenuDates;
    private CustomPowerMenu customPowerMenuCourse;
    private String querydate;
    private String mCourse;
    private int mYos;
    private int mStudCount;

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

    private void loadUpUI(Date date) {
        String datef = SavedClasses.formatDate(date);
        atnDate.setText(datef);
    }

    private void preCourseUI(boolean comb) {
        if (comb) {
            atnBtnCourse.setVisibility(View.VISIBLE);
        } else {
            atnBtnCourse.setVisibility(View.GONE);
        }
    }

    private void loadDates(final String lecteachid) {
        FirebaseFirestore.getInstance().collection(SAVEDCLASSESCOL).document(lecteachid)
                .get()
                .addOnSuccessListener(DefaultExecutorSupplier.getInstance().forMainThreadTasks(),
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(final DocumentSnapshot docSnapshot) {

                                List<Timestamp> timestamps = new ArrayList<>();
                                for (int i = 1; ; i++) {
                                    Timestamp timestamp = docSnapshot.getTimestamp(String.valueOf(i));
                                    if (timestamp != null) {
                                        timestamps.add(timestamp);

                                    } else {
                                        break;
                                    }
                                }

                                loadUpUI(timestamps.get(0).toDate());
                            }
                        });
    }

    private void loadData(final String lecteachid) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading attendance list...");
        pDialog.setCancelable(false);
        pDialog.show();

        FirebaseFirestore.getInstance().collection(SAVEDCLASSESCOL).document(lecteachid)
                .get()
                .addOnSuccessListener(
                        DefaultExecutorSupplier.getInstance().forMainThreadTasks(),
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot docSnapshot) {
                                //dates classes have loaded

                                List<Timestamp> timestamps = new ArrayList<>();

                                for (int i = 1; ; i++) {
                                    Timestamp timestamp = docSnapshot.getTimestamp(String.valueOf(i));
                                    if (timestamp != null) {
                                        timestamps.add(timestamp);

                                    } else {
                                        break;
                                    }
                                }

                                querydate = SavedClasses.formatDateKey(timestamps.get(0).toDate());

                                FirebaseFirestore.getInstance().collection(LECTEACHTIMECOL)
                                        .whereEqualTo("lecteachid", lecteachid)
                                        .get()
                                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                            @Override
                                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                pDialog.dismiss();

                                                LecTeachTime lecTeachTime = queryDocumentSnapshots.toObjects(LecTeachTime.class).get(0);

                                                if (lecTeachTime != null) {

                                                    mCourse = lecTeachTime.getCourses().get(0).getCourse();
                                                    mYos = lecTeachTime.getCourses().get(0).getYearofstudy();

                                                    loadDates(lecteachid);
                                                    loadUpUI(AttendanceListUIModel.loadCourses(lecTeachTime.getCourses()).get(0));
                                                    setUpList(lecTeachTime.getCourses().get(0).getCourse(),
                                                            lecTeachTime.getCourses().get(0).getYearofstudy(), querydate);
                                                }
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e(TAG, "onFailure: ", e);
                                            }
                                        });
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pDialog.dismiss();
                        DoSnack.errorPrompt(AttendanceListActivity.this, "Loading failed", e.getMessage());
                    }
                });
    }

    private void setUpList(final String course, final int yearofstudy, String querydate) {

        SharedPreferences preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        final String currentsemester = preferences.getString(CURRENT_SEM_PREF_NAME, "0");
        final String currentyear = preferences.getString(CURRENT_YEAR_PREF_NAME, "2000");

        Query query = FirebaseFirestore.getInstance().collection(STUDENTSCANCLASSCOL)
                .whereEqualTo("year", currentyear)
                .whereEqualTo("semester", currentsemester)
                .whereEqualTo("course", course)
                .whereEqualTo("yearofstudy", String.valueOf(yearofstudy))
                .whereEqualTo("querydate", querydate)
                .orderBy("regno", Query.Direction.ASCENDING);

        Log.d(TAG, "setUpList: " + " currentyear =" + currentyear + " querydate =" + querydate +
                "course = " + course + " yearofstudy = " + String.valueOf(yearofstudy)
                + "currentsemester = " + currentsemester
        );

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
                    atnStudCount.setText(getString(R.string.num_students_vs, getItemCount(), mStudCount)); calculateCount(course, yearofstudy, new StudentCountCallback() {
                        @Override
                        public void onSuccess(int value) {
                            atnStudCount.setText(getString(R.string.num_students_vs, getItemCount(), value));
                        }
                    });

                } else {

                    atnList.setVisibility(View.VISIBLE);
                    noStudView.setVisibility(View.GONE);
                    calculateCount(course, yearofstudy, new StudentCountCallback() {
                        @Override
                        public void onSuccess(int value) {
                            atnStudCount.setText(getString(R.string.num_students_vs, getItemCount(), value));
                        }
                    });

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

    @OnClick({R.id.atn_btn_date, R.id.atn_date})
    public void dateOnClick() {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading attendance dates...");
        pDialog.setCancelable(false);
        pDialog.show();

        FirebaseFirestore.getInstance().collection(SAVEDCLASSESCOL).document(lecteachid)
                .get()
                .addOnSuccessListener(DefaultExecutorSupplier.getInstance().forMainThreadTasks(),
                        new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(final DocumentSnapshot docSnapshot) {

                                pDialog.dismiss();

                                List<Timestamp> timestamps = new ArrayList<>();
                                for (int i = 1; ; i++) {
                                    Timestamp timestamp = docSnapshot.getTimestamp(String.valueOf(i));
                                    if (timestamp != null) {
                                        timestamps.add(timestamp);

                                    } else {
                                        break;
                                    }
                                }

                                //Creating the instance of PopupMenu
                                customPowerMenuDates = PowerMenuUtils.getDatesCustomDialogPowerMenu(AttendanceListActivity.this,
                                        AttendanceListActivity.this, timestamps,
                                        onIconDateMenuItemClickListener);

                                customPowerMenuDates.setShowBackground(false);
                                customPowerMenuDates.showAsAnchorRightBottom(atnDate);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pDialog.dismiss();
                DoSnack.errorPrompt(AttendanceListActivity.this, "Loading failed", e.getMessage());
            }
        });
    }

    @OnClick({R.id.atn_btn_course, R.id.atn_course})
    public void courseOnClick() {
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

                            List<IconPowerMenuCourse> iconPowerMenuCoures = new ArrayList<>();
                            for (CourseYear s : lecTeachTime.getCourses()) {
                                IconPowerMenuCourse ipmd = new IconPowerMenuCourse(String.valueOf(s.getYearofstudy()), s.getCourse());
                                iconPowerMenuCoures.add(ipmd);
                            }


                            //Creating the instance of PopupMenu
                            customPowerMenuCourse = PowerMenuUtils.getCoursesCustomDialogPowerMenu(AttendanceListActivity.this,
                                    AttendanceListActivity.this, lecTeachTime.getCourses(),
                                    onIconCourseMenuItemClickListener);

                            customPowerMenuCourse.setShowBackground(false);
                            customPowerMenuCourse.showAsAnchorRightBottom(atnDate);
                        }
                    }
                });
    }

    private OnMenuItemClickListener<IconPowerMenuDate> onIconDateMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuDate>() {
        @Override
        public void onItemClick(int position, IconPowerMenuDate item) {

            atnDate.setText(item.getTitle());
            querydate = item.getKey();
            setUpList(mCourse, mYos, querydate);

            if (customPowerMenuDates.isShowing()) {
                customPowerMenuDates.dismiss();
            }
        }
    };

    private OnMenuItemClickListener<IconPowerMenuCourse> onIconCourseMenuItemClickListener = new OnMenuItemClickListener<IconPowerMenuCourse>() {
        @Override
        public void onItemClick(int position, IconPowerMenuCourse item) {

            mCourse = item.getTitle();
            mYos = Integer.parseInt(item.getKey());

            loadUpUI(item.getTitle() + " - " + item.getKey());
            setUpList(item.getTitle(), Integer.parseInt(item.getKey()), querydate);
            if (customPowerMenuCourse.isShowing()) {
                customPowerMenuCourse.dismiss();
            }
        }
    };

    private void calculateCount(String course, int yos, final StudentCountCallback callback) {
        SharedPreferences preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        final String currentsemester = preferences.getString(CURRENT_SEM_PREF_NAME, "0");
        final String currentyear = preferences.getString(CURRENT_YEAR_PREF_NAME, "2000");

        Query query = FirebaseFirestore.getInstance().collection(STUDENTDETAILSCOL)
                .whereEqualTo("currentyear", currentyear)
                .whereEqualTo("currentsemester", currentsemester)
                .whereEqualTo("course", course)
                .whereEqualTo("yearofstudy", String.valueOf(yos))
                .orderBy("regnumber", Query.Direction.ASCENDING);

        query.get().addOnSuccessListener(DefaultExecutorSupplier.getInstance().forBackgroundTasks(),
                new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        mStudCount = queryDocumentSnapshots.size();
                        callback.onSuccess(mStudCount);
                    }
                });
    }

    interface StudentCountCallback {
        void onSuccess(int value);
    }
}
