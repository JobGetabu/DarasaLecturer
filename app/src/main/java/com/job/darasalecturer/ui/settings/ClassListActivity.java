package com.job.darasalecturer.ui.settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.evrencoskun.tableview.TableView;
import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ClassListActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);
        ButterKnife.bind(this);
    }
}
