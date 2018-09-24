package com.job.darasalecturer.ui;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.chip.ChipGroup;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepVenueFragment extends Fragment {


    @BindView(R.id.step_venue_name)
    TextInputLayout stepVenueName;
    @BindView(R.id.step_venue_department)
    TextInputLayout stepVenueDepartment;
    @BindView(R.id.step_venue_chipgroup)
    ChipGroup stepVenueChipgroup;
    @BindView(R.id.step_venue_course_btn)
    MaterialButton stepVenueCourseBtn;
    @BindView(R.id.step_venue_back)
    TextView stepVenueBack;
    @BindView(R.id.step_venue_next)
    TextView stepVenueNext;
    Unbinder unbinder;

    public StepVenueFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_venue, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.step_venue_course_btn)
    public void onStepVenueCourseBtnClicked() {
    }

    @OnClick(R.id.step_venue_back)
    public void onStepVenueBackClicked() {
    }

    @OnClick(R.id.step_venue_next)
    public void onStepVenueNextClicked() {
    }
}
