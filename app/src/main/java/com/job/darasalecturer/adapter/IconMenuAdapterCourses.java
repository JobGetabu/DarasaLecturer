package com.job.darasalecturer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.job.darasalecturer.R;
import com.job.darasalecturer.model.IconPowerMenuCourse;
import com.skydoves.powermenu.MenuBaseAdapter;

/**
 * Created by Job on Monday : 12/31/2018.
 */
public class IconMenuAdapterCourses extends MenuBaseAdapter<IconPowerMenuCourse> {

    @Override
    public View getView(int index, View view, ViewGroup viewGroup) {
        final Context context = viewGroup.getContext();

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.powermenu_course, viewGroup, false);
        }

        IconPowerMenuCourse item = (IconPowerMenuCourse) getItem(index);

        final TextView title = view.findViewById(R.id.course_menu);
        title.setText(item.getTitle());
        return super.getView(index, view, viewGroup);
    }
}