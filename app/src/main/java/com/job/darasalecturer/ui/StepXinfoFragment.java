package com.job.darasalecturer.ui;


import android.os.Bundle;
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

    @OnClick(R.id.step_x_back)
    public void onStepXBackClicked() {
    }

    @OnClick(R.id.step_x_finish)
    public void onStepXFinishClicked() {
    }
}
