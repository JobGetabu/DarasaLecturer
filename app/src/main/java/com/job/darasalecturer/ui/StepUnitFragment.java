package com.job.darasalecturer.ui;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
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
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.step_unit_time_btn)
    public void onStepUnitTimeBtnClicked() {
    }

    @OnClick(R.id.step_unit_back)
    public void onStepUnitBackClicked() {
    }

    @OnClick(R.id.step_unit_next)
    public void onStepUnitNextClicked() {
    }
}
