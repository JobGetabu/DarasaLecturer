package com.job.darasalecturer.ui.settings;

import android.os.Bundle;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.job.darasalecturer.R;
import com.job.darasalecturer.util.PowerMenuUtils;
import com.skydoves.powermenu.CustomPowerMenu;

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

    private ClassListBottomSheet classListBottomSheet;
    private ClassAttendanceListFragment classAttendanceListFragment;
    private  CustomPowerMenu powerMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_stats, null);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        classListBottomSheet = new ClassListBottomSheet();
        classAttendanceListFragment = new ClassAttendanceListFragment();
        initializeCustomDialogMenu();
    }

    @OnClick(R.id.frg_stats_classlist)
    public void classOnClick(){
        classListBottomSheet.show(getActivity().getSupportFragmentManager(),ClassListBottomSheet.TAG);
        dismiss();
    }
    @OnClick(R.id.frg_stats_attendancelist)
    public void attendOnClick(){

        classAttendanceListFragment.show(getActivity().getSupportFragmentManager(),ClassAttendanceListFragment.TAG);
        dismiss();
    }
    @OnClick(R.id.frg_stats_studstarts)
    public void studOnClick(){

        dismiss();
        View layout = getActivity().findViewById(R.id.settings_main);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthLcl = (int) (displayMetrics.widthPixels*0.9f);
        powerMenu.setWidth(widthLcl);
        powerMenu.showAtCenter(layout);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void initializeCustomDialogMenu() {
        powerMenu = PowerMenuUtils.getCustomDialogPowerMenu(getContext(), getActivity());
        View footerView = powerMenu.getFooterView();
        TextView textView_yes = footerView.findViewById(R.id.footer_yes);
        textView_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                powerMenu.dismiss();
            }
        });
        TextView textView_no = footerView.findViewById(R.id.footer_dismiss);
        textView_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                powerMenu.dismiss();
            }
        });
    }
}
