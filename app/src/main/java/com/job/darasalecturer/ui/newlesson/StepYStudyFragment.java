package com.job.darasalecturer.ui.newlesson;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepYStudyFragment extends Fragment {


    @BindView(R.id.step_ys_list)
    RelativeLayout stepYsList;
    @BindView(R.id.step_ys_back)
    TextView stepYsBack;
    @BindView(R.id.step_ys_next)
    TextView stepYsNext;
    Unbinder unbinder;

    public StepYStudyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_step_ystudy, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.step_ys_back)
    public void onStepYsBackClicked() {
    }

    @OnClick(R.id.step_ys_next)
    public void onStepYsNextClicked() {
    }
}
