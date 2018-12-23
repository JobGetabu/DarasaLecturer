package com.job.darasalecturer.ui.settings;

import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.job.darasalecturer.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class StatsMenuBottomSheet extends BottomSheetDialogFragment {

    public static final String TAG = "StatsMenuSheet";

    @BindView(R.id.frg_stats_classlist) MaterialButton frgStatsClasslist;
    @BindView(R.id.frg_stats_attendancelist) MaterialButton frgStatsAttendancelist;
    @BindView(R.id.frg_stats_studstarts) MaterialButton frgStatsStudstarts;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_stats, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.frg_stats_classlist)
    public void classOnClick(){}
    @OnClick(R.id.frg_stats_attendancelist)
    public void attendOnClick(){}
    @OnClick(R.id.frg_stats_studstarts)
    public void studOnClick(){}

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
