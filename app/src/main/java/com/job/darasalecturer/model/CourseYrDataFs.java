package com.job.darasalecturer.model;

import java.util.List;
import java.util.Map;

/**
 * Created by Job on Sunday : 11/18/2018.
 */
public class CourseYrDataFs{

    private List<Map<Integer,CourseYear>> courseYrData;

    public CourseYrDataFs() {
    }

    public CourseYrDataFs(List<Map<Integer, CourseYear>> courseYrData) {
        this.courseYrData = courseYrData;
    }

    public List<Map<Integer, CourseYear>> getCourseYrData() {
        return courseYrData;
    }

    public void setCourseYrData(List<Map<Integer, CourseYear>> courseYrData) {
        this.courseYrData = courseYrData;
    }

    @Override
    public String toString() {
        return "CourseYrDataFs{" +
                "courseYrData=" + courseYrData +
                '}';
    }
}
