package com.job.darasalecturer.ui.settings;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.evrencoskun.tableview.filter.Filter;
import com.evrencoskun.tableview.pagination.Pagination;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.job.darasalecturer.R;
import com.job.darasalecturer.appexecutor.DefaultExecutorSupplier;
import com.job.darasalecturer.model.ClassListModel;
import com.job.darasalecturer.model.StudentDetails;
import com.job.darasalecturer.ui.settings.tableview.TableViewAdapter;
import com.job.darasalecturer.ui.settings.tableview.TableViewListener;
import com.job.darasalecturer.ui.settings.tableview.TableViewModel;
import com.job.darasalecturer.util.DoSnack;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static com.job.darasalecturer.util.Constants.CURRENT_SEM_PREF_NAME;
import static com.job.darasalecturer.util.Constants.CURRENT_YEAR_PREF_NAME;
import static com.job.darasalecturer.util.Constants.STUDENTDETAILSCOL;

public class ClassListActivity extends AppCompatActivity {

    public static final String TAG = "ClassList";
    public static final String COURSE_EXTRA = "COURSE_EXTRA";
    public static final String YEAROFSTUDY_EXTRA = "YEAROFSTUDY_EXTRA";

    @BindView(R.id.query_string)
    EditText queryString;
    @BindView(R.id.mood_spinner)
    Spinner moodSpinner;
    @BindView(R.id.gender_spinner)
    Spinner genderSpinner;
    @BindView(R.id.search_field)
    LinearLayout searchField;
    @BindView(R.id.previous_button)
    ImageButton previousButton;
    @BindView(R.id.page_number_text)
    EditText pageNumberText;
    @BindView(R.id.next_button)
    ImageButton nextButton;
    @BindView(R.id.table_details)
    TextView tableDetails;
    @BindView(R.id.items_per_page_spinner)
    Spinner itemsPerPageSpinner;
    @BindView(R.id.table_test_container)
    LinearLayout tableTestContainer;
    @BindView(R.id.tableview)
    TableView tableview;
    @BindView(R.id.fragment_container)
    RelativeLayout fragmentContainer;

    private Filter mTableFilter; // This is used for filtering the table.
    private Pagination mPagination; // This is used for paginating the table.
    // This is a sample class that provides the cell value objects and other configurations.
    private TableViewModel mTableViewModel;
    private TableViewAdapter mTableAdapter;

    private boolean mPaginationEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        ButterKnife.bind(this);

        if (mPaginationEnabled) {
            tableTestContainer.setVisibility(View.VISIBLE);
            itemsPerPageSpinner.setOnItemSelectedListener(onItemsPerPageSelectedListener);

            previousButton.setOnClickListener(mClickListener);
            nextButton.setOnClickListener(mClickListener);
        } else {
            tableTestContainer.setVisibility(View.GONE);
        }

        String course = getIntent().getStringExtra(COURSE_EXTRA);
        int yos = getIntent().getIntExtra(YEAROFSTUDY_EXTRA,0);


        initializeTableView(tableview);
        loadData(course,yos);

        if (mPaginationEnabled) {
            mTableFilter = new Filter(tableview); // Create an instance of a Filter and pass the
            // created TableView.

            // Create an instance for the TableView pagination and pass the created TableView.
            mPagination = new Pagination(tableview);

            // Sets the pagination listener of the TableView pagination to handle
            // pagination actions. See onTableViewPageTurnedListener variable declaration below.
            mPagination.setOnTableViewPageTurnedListener(onTableViewPageTurnedListener);
        }
    }

    private void initializeTableView(TableView tableView) {

        // Create TableView Adapter
        mTableAdapter = new TableViewAdapter(this);
        tableView.setAdapter(mTableAdapter);

        // Create listener
        tableView.setTableViewListener(new TableViewListener(tableView));
    }

    public void filterTable(String filter) {
        // Sets a filter to the table, this will filter ALL the columns.
        mTableFilter.set(filter);
    }


    // The following four methods below: nextTablePage(), previousTablePage(),
    // goToTablePage(int page) and setTableItemsPerPage(int itemsPerPage)
    // are for controlling the TableView pagination.
    public void nextTablePage() {
        mPagination.nextPage();
    }

    public void previousTablePage() {
        mPagination.previousPage();
    }

    public void goToTablePage(int page) {
        mPagination.goToPage(page);
    }

    public void setTableItemsPerPage(int itemsPerPage) {
        mPagination.setItemsPerPage(itemsPerPage);
    }

    // Handler for the changing of pages in the paginated TableView.
    private Pagination.OnTableViewPageTurnedListener onTableViewPageTurnedListener = new
            Pagination.OnTableViewPageTurnedListener() {
                @Override
                public void onPageTurned(int numItems, int itemsStart, int itemsEnd) {
                    int currentPage = mPagination.getCurrentPage();
                    int pageCount = mPagination.getPageCount();
                    previousButton.setVisibility(View.VISIBLE);
                    nextButton.setVisibility(View.VISIBLE);

                    if (currentPage == 1 && pageCount == 1) {
                        previousButton.setVisibility(View.INVISIBLE);
                        nextButton.setVisibility(View.INVISIBLE);
                    }

                    if (currentPage == 1) {
                        previousButton.setVisibility(View.INVISIBLE);
                    }

                    if (currentPage == pageCount) {
                        nextButton.setVisibility(View.INVISIBLE);
                    }

                    tableDetails.setText(getString(R.string.table_pagination_details, String
                            .valueOf(currentPage), String.valueOf(itemsStart), String.valueOf(itemsEnd)));

                }
            };


    private AdapterView.OnItemSelectedListener mItemSelectionListener = new AdapterView
            .OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // 0. index is for empty item of spinner.
            if (position > 0) {

                String filter = Integer.toString(position);


            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            // Left empty intentionally.
        }
    };


    private AdapterView.OnItemSelectedListener onItemsPerPageSelectedListener = new AdapterView
            .OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            int itemsPerPage;
            switch (parent.getItemAtPosition(position).toString()) {
                case "All":
                    itemsPerPage = 0;
                    break;
                default:
                    itemsPerPage = Integer.valueOf(parent.getItemAtPosition(position).toString());
            }

            setTableItemsPerPage(itemsPerPage);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v == previousButton) {
                previousTablePage();
            } else if (v == nextButton) {
                nextTablePage();
            }
        }
    };


    private void showProgressBar(final SweetAlertDialog pDialog) {
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#FF5521"));
        pDialog.setTitleText("Loading class list...");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    private void loadData(String course, int yearofstudy) {

        final SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        showProgressBar(pDialog);

        SharedPreferences preferences = getSharedPreferences(getApplicationContext().getPackageName(), MODE_PRIVATE);
        final String currentsemester = preferences.getString(CURRENT_SEM_PREF_NAME, "0");
        final String currentyear = preferences.getString(CURRENT_YEAR_PREF_NAME, "2000");


        FirebaseFirestore.getInstance().collection(STUDENTDETAILSCOL)
                .whereEqualTo("currentyear", currentyear)
                .whereEqualTo("currentsemester", currentsemester)
                .whereEqualTo("course", course)
                .whereEqualTo("yearofstudy", yearofstudy)
                .orderBy("regnumber", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(DefaultExecutorSupplier.getInstance().forMainThreadTasks()
                        , new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                List<ClassListModel> classListModels = new ArrayList<>();

                                for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                    //heavy operation
                                    StudentDetails studentDetails = doc.toObject(StudentDetails.class);
                                    String name = studentDetails.getFirstname() + " " + studentDetails.getLastname();
                                    ClassListModel model = new ClassListModel(name, studentDetails.getRegnumber(), "");
                                    Log.d(TAG, "onSuccess: "+model);
                                    classListModels.add(model);
                                }

                                //list filled
                                // set the list on TableViewModel
                                mTableAdapter.setUserList(classListModels);

                                if (classListModels.size() == 0) {
                                    pDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                                    pDialog.setContentText("No students data available");
                                    pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            finish();
                                        }
                                    });

                                } else {

                                    pDialog.dismissWithAnimation();
                                }


                            }
                        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pDialog.changeAlertType(SweetAlertDialog.WARNING_TYPE);
                pDialog.setContentText("No students data available");
                pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        finish();
                    }
                });

                Log.d(TAG, "onError: ", e);
                DoSnack.showShortSnackbar(ClassListActivity.this, "Error: check logs for info.");
            }
        });

    }
}
