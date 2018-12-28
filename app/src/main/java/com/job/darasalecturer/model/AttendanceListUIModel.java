package com.job.darasalecturer.model;

import android.content.Context;

import com.job.darasalecturer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Job on Friday : 12/28/2018.
 */
public class AttendanceListUIModel {
    private List<CourseYear> courseYears;
    private boolean combiner;
    private String unitname;
    private String unitcode;


    public static List<String> loadDates(Context context, DoneClasses doneClass){
        int num = (int) doneClass.getNumber();

        ArrayList<String> strings = new ArrayList<>(num);
        for (int i = 1; i <= num; i++) {
            String s = context.getString(R.string.num_class,ordinal(i));
            strings.add(s);
        }

        return strings;
    }

    public static List<String> loadCourses(List<CourseYear> courseYears){
        ArrayList<String> strings = new ArrayList<>();

        for (CourseYear s: courseYears){
            strings.add(s.getCourse()+" - "+s.getYearofstudy());
        }

        return strings;
    }

    public static int coursePos(List<CourseYear> courseYears, String course){
        int j;

        for (j = 0; j < courseYears.size(); j++) {
            //s.getCourse()+" - "+s.getYearofstudy()
            if (course.equals(courseYears.get(j).getCourse()+" - "+courseYears.get(j).getYearofstudy())){
                return j;
            }
        }
        return j;
    }

    private static String ordinal(int i) {
        String[] sufixes = new String[] { "th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th" };
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }
}
