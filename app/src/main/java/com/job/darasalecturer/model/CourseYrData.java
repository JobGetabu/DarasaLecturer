package com.job.darasalecturer.model;

import java.util.ArrayList;

/**
 * Created by Job on Sunday : 11/18/2018.
 */
public class CourseYrData {
    private ArrayList<Object> courses;

    public CourseYrData() {
    }

    public CourseYrData(ArrayList<Object> courses) {
        this.courses = courses;
    }

    public ArrayList<Object> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<Object> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "CourseYrData{" +
                "courses=" + courses +
                '}';
    }
}
