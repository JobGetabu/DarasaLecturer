package com.job.darasalecturer.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.job.darasalecturer.R;
import com.job.darasalecturer.viewmodel.AddClassViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepXinfoFragment extends Fragment {


    @BindView(R.id.step_x_semester)
    TextInputLayout stepXSemester;
    @BindView(R.id.step_x_year)
    TextInputLayout stepXYear;
    @BindView(R.id.step_x_school)
    TextInputLayout stepXSchool;
    @BindView(R.id.step_x_acadyear)
    TextInputLayout stepXAcadyear;
    @BindView(R.id.step_x_back)
    TextView stepXBack;
    @BindView(R.id.step_x_finish)
    TextView stepXFinish;
    Unbinder unbinder;

    private AddClassViewModel model;

    public StepXinfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_xinfo, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(AddClassViewModel.class);
    }

    @OnClick(R.id.step_x_back)
    public void onStepXBackClicked() {

        if (validate()) {
            model.setCurrentStep(2);
        }
    }

    @OnClick(R.id.step_x_finish)
    public void onStepXFinishClicked() {
        //finish
        if (validate()){

        }
    }

    public boolean validate() {
        boolean valid = true;

        String sem = stepXSemester.getEditText().getText().toString();
        String yr = stepXYear.getEditText().getText().toString();
        String academ = stepXAcadyear.getEditText().getText().toString();
        String school = stepXSchool.getEditText().getText().toString();

        if (sem.isEmpty()) {
            stepXSemester.setError("enter semester");
            valid = false;
        } else {
            stepXSemester.setError(null);
        }

        if (yr.isEmpty()) {
            stepXYear.setError("enter year");
            valid = false;
        } else {
            stepXYear.setError(null);
        }

        if (academ.isEmpty()) {
            stepXAcadyear.setError("enter academic year");
            valid = false;
        } else {
            stepXAcadyear.setError(null);
        }

        if (school.isEmpty()) {
            stepXSchool.setError("enter school");
            valid = false;
        } else {
            stepXSchool.setError(null);
        }

        return valid;
    }
}
