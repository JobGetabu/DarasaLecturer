package com.job.darasalecturer.ui;


import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
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
public class StepUnitFragment extends Fragment {


    @BindView(R.id.step_unit_unitname)
    TextInputLayout stepUnitUnitname;
    @BindView(R.id.step_unit_unitcode)
    TextInputLayout stepUnitUnitcode;
    @BindView(R.id.step_unit_time)
    TextInputLayout stepUnitTime;
    @BindView(R.id.step_unit_time_btn)
    MaterialButton stepUnitTimeBtn;
    @BindView(R.id.step_unit_back)
    TextView stepUnitBack;
    @BindView(R.id.step_unit_next)
    TextView stepUnitNext;
    Unbinder unbinder;

    private AddClassViewModel model;

    public StepUnitFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_unit, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        model = ViewModelProviders.of(getActivity()).get(AddClassViewModel.class);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.step_unit_time_btn)
    public void onStepUnitTimeBtnClicked() {
    }

    @OnClick(R.id.step_unit_back)
    public void onStepUnitBackClicked() {
        model.setCurrentStep(0);
    }

    @OnClick(R.id.step_unit_next)
    public void onStepUnitNextClicked() {
        if (validate()) {

            model.setCurrentStep(2);
        }
    }

    public boolean validate() {
        boolean valid = true;

        String unitname = stepUnitUnitname.getEditText().getText().toString();
        String unitcode = stepUnitUnitcode.getEditText().getText().toString();
        String time = stepUnitTime.getEditText().getText().toString();

        if (unitname.isEmpty()) {
            stepUnitUnitname.setError("enter unit");
            valid = false;
        } else {
            stepUnitUnitname.setError(null);
        }

        if (unitcode.isEmpty()) {
            stepUnitUnitcode.setError("enter unit code");
            valid = false;
        } else {
            stepUnitUnitcode.setError(null);
        }

        if (time.isEmpty()) {
            stepUnitTime.setError("Select time");
            valid = false;
        } else {
            stepUnitTime.setError(null);
        }


        return valid;
    }
}
