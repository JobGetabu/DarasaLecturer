package com.job.darasalecturer.ui.settings;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by Job on Sunday : 12/23/2018.
 */
public class ClassListBottomSheet extends BottomSheetDialogFragment {

    @BindView(R.id.stats_course_list)
    RecyclerView statsCourseList;
    @BindView(R.id.stats_selectcourses)
    MaterialButton statsSelectcourses;
    @BindView(R.id.frg_stats_gen_classlist)
    MaterialButton frgStatsGenClasslist;
    Unbinder unbinder;
    @BindView(R.id.meso_ll_rv)
    LinearLayout mesoLlRv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_classlistgen, null);

        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @OnClick(R.id.stats_selectcourses)
    public void onStatsSelectcoursesClicked() {
    }

    @OnClick(R.id.frg_stats_gen_classlist)
    public void onFrgStatsGenClasslistClicked() {
    }
}
