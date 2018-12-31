package com.job.darasalecturer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.job.darasalecturer.R;
import com.job.darasalecturer.model.StatsMenu;
import com.skydoves.powermenu.MenuBaseAdapter;

/**
 * Created by Job on Monday : 12/31/2018.
 */
public class CustomDialogMenuAdapter extends MenuBaseAdapter<StatsMenu> {

    public CustomDialogMenuAdapter() {
        super();
    }

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_student_profile, viewGroup, false);
        }

        return super.getView(index, view, viewGroup);
    }
}
