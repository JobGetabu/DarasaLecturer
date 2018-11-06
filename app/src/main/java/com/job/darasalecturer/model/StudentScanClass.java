package com.job.darasalecturer.model;

import java.util.Date;

/**
 * Created by Job on Wednesday : 10/3/2018.
 */
public class StudentScanClass {
    private String semester;
    private String year;
    private String studentid;
    private String lecteachtimeid;
    private Date classtime;
    private Date date;
    private String studentscanid;

    public StudentScanClass() {
    }

    public StudentScanClass(String semester, String year, String studentid, String lecteachtimeid,
                            Date classtime, Date date, String studentscanid) {
        this.semester = semester;
        this.year = year;
        this.studentid = studentid;
        this.lecteachtimeid = lecteachtimeid;
        this.classtime = classtime;
        this.date = date;
        this.studentscanid = studentscanid;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getLecteachtimeid() {
        return lecteachtimeid;
    }

    public void setLecteachtimeid(String lecteachtimeid) {
        this.lecteachtimeid = lecteachtimeid;
    }

    public Date getClasstime() {
        return classtime;
    }

    public void setClasstime(Date classtime) {
        this.classtime = classtime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getStudentscanid() {
        return studentscanid;
    }

    public void setStudentscanid(String studentscanid) {
        this.studentscanid = studentscanid;
    }
}
