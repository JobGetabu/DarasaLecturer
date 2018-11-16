package com.job.darasalecturer.model;

/**
 * Created by Job on Friday : 11/16/2018.
 */
public class CourseYear {
    private String course;
    private double yearofstudy;

    public CourseYear() {
    }

    public CourseYear(String course, double yearofstudy) {
        this.course = course;
        this.yearofstudy = yearofstudy;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getYearofstudy() {
        return yearofstudy;
    }

    public void setYearofstudy(double yearofstudy) {
        this.yearofstudy = yearofstudy;
    }

    @Override
    public String toString() {
        return "CourseYear{" +
                "course='" + course + '\'' +
                ", yearofstudy=" + yearofstudy +
                '}';
    }
}
