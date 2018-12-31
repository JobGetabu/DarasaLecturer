package com.job.darasalecturer.util;

import android.util.Log;

import com.job.darasalecturer.model.CourseYear;
import com.job.darasalecturer.model.SavedClasses;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Job on Monday : 11/19/2018.
 */
public class CoursesProvider {

    public static ArrayList<CourseYear> jsonWorker(Map<String, Object> courseObject) {

        ArrayList<CourseYear> courseYears = new ArrayList<>();
        for (int i = 0; i < courseObject.size(); i++) {
            Map<String, Object> zero = (Map<String, Object>) courseObject.get(String.valueOf(i));
            String course = (String) zero.get("course");
            long yearofstudy = (long) zero.get("yearofstudy");

            CourseYear cc = new CourseYear(course, (int) yearofstudy);
            courseYears.add(cc);
        }

        return courseYears;
    }

    public static List<SavedClasses> jsonWorkerSavedClasses(Map<String, Object> savedObject) {

        ArrayList<SavedClasses> dateKindObj = new ArrayList<>();

        for (int i = 1; i < savedObject.size(); i++) {
            Map<String, Object> zero = (Map<String, Object>) savedObject.get(String.valueOf(i));

            SavedClasses savedClasses = new SavedClasses();
            Log.d("TTT", "jsonWorkerSavedClasses: "+i);
            Log.d("TTT", "jsonWorkerSavedClasses: "+(Date)zero.get(i));

            /*savedClasses.setDate((Date) zero.get(i));
            savedClasses.setNumber(i);
            savedClasses.setFormatedDate(SavedClasses.formatDate(savedClasses.getDate()));*/

            dateKindObj.add(savedClasses);
        }

        return dateKindObj;
    }

    public static List<Timestamp> jsonWorkerSavedClasses(List<Timestamp> ts) {

        ArrayList<Timestamp> dateKindObj = new ArrayList<>(ts);

        return dateKindObj;
    }
}
