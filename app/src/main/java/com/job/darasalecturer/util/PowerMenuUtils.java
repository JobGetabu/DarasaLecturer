package com.job.darasalecturer.util;

import android.arch.lifecycle.LifecycleOwner;
import android.content.Context;

import com.google.firebase.Timestamp;
import com.job.darasalecturer.R;
import com.job.darasalecturer.adapter.CustomDialogMenuAdapter;
import com.job.darasalecturer.adapter.IconMenuAdapterCourses;
import com.job.darasalecturer.adapter.IconMenuAdapterDates;
import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.model.IconPowerMenuCourse;
import com.job.darasalecturer.model.IconPowerMenuDate;
import com.job.darasalecturer.model.SavedClasses;
import com.job.darasalecturer.model.StatsMenu;
import com.skydoves.powermenu.CustomPowerMenu;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Job on Monday : 12/31/2018.
 */
public class PowerMenuUtils {

    public static CustomPowerMenu getCoursesCustomDialogPowerMenu(Context context, LifecycleOwner lifecycleOwner,List<CourseYear> courseYears,
                                                           OnMenuItemClickListener<IconPowerMenuCourse> onIconCourseMenuItemClickListener) {

        List<IconPowerMenuCourse> iconPowerMenuCourses = new ArrayList<>();
        for (CourseYear s : courseYears) {
            IconPowerMenuCourse ipmd = new IconPowerMenuCourse(String.valueOf(s.getYearofstudy()),s.getCourse());
            iconPowerMenuCourses.add(ipmd);
        }

        return new CustomPowerMenu.Builder<>(context,
                new IconMenuAdapterCourses())
                .addItemList(iconPowerMenuCourses)
                .setOnMenuItemClickListener(onIconCourseMenuItemClickListener)
                .setAnimation(MenuAnimation.SHOWUP_BOTTOM_RIGHT)
                .setLifecycleOwner(lifecycleOwner)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .build();
    }

    public static CustomPowerMenu getDatesCustomDialogPowerMenu(Context context, LifecycleOwner lifecycleOwner,List<Timestamp> timestamps,
                                                                  OnMenuItemClickListener<IconPowerMenuDate> onIconCourseMenuItemClickListener) {

        List<IconPowerMenuDate> iconPowerMenudates = new ArrayList<>();

        for (Timestamp ts : timestamps) {
            String key = SavedClasses.formatDateKey(ts.toDate());
            String title = SavedClasses.formatDate(ts.toDate());

            IconPowerMenuDate ipmd = new IconPowerMenuDate(key,title);
            iconPowerMenudates.add(ipmd);
        }

        return new CustomPowerMenu.Builder<>(context,
                new IconMenuAdapterDates())
                .addItemList(iconPowerMenudates)
                .setOnMenuItemClickListener(onIconCourseMenuItemClickListener)
                .setAnimation(MenuAnimation.SHOWUP_BOTTOM_RIGHT)
                .setLifecycleOwner(lifecycleOwner)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .build();
    }

    public static CustomPowerMenu getCustomDialogPowerMenu(Context context, LifecycleOwner lifecycleOwner) {

        return new CustomPowerMenu.Builder(context,new CustomDialogMenuAdapter())
                .setHeaderView(R.layout.dialog_header) // header used for title
                .setFooterView(R.layout.dialog_footer) // footer used for yes and no buttons
                .addItem(new StatsMenu()) // this is body
                .setLifecycleOwner(lifecycleOwner)
                .setAnimation(MenuAnimation.SHOW_UP_CENTER)
                .setMenuRadius(10f)
                .setMenuShadow(10f)
                .build();
    }
}
