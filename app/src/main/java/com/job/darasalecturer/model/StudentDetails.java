package com.job.darasalecturer.model;

import android.support.annotation.Keep;

/**
 * Created by Job on Tuesday : 8/14/2018.
 */
@Keep
public class StudentDetails {

    private String devicetoken;
    private String studentid;
    private String firstname;
    private String lastname;

    private String regnumber;
    private String photourl;

    private String course;
    private String department;
    private String school;
    private String currentsemester;
    private String currentyear;
    private String currentacademicyear;

    public StudentDetails() {
    }

    public StudentDetails(String devicetoken, String studentid, String firstname, String lastname, String regnumber, String photourl,
                          String course,
                          String department, String school, String currentsemester, String currentyear, String currentacademicyear) {
        this.devicetoken = devicetoken;
        this.studentid = studentid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.regnumber = regnumber;
        this.photourl = photourl;
        this.course = course;
        this.department = department;
        this.school = school;
        this.currentsemester = currentsemester;
        this.currentyear = currentyear;
        this.currentacademicyear = currentacademicyear;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getRegnumber() {
        return regnumber;
    }

    public void setRegnumber(String regnumber) {
        this.regnumber = regnumber;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getCurrentsemester() {
        return currentsemester;
    }

    public void setCurrentsemester(String currentsemester) {
        this.currentsemester = currentsemester;
    }

    public String getCurrentyear() {
        return currentyear;
    }

    public void setCurrentyear(String currentyear) {
        this.currentyear = currentyear;
    }

    public String getCurrentacademicyear() {
        return currentacademicyear;
    }

    public void setCurrentacademicyear(String currentacademicyear) {
        this.currentacademicyear = currentacademicyear;
    }

    @Override
    public String toString() {
        return "StudentDetails{" +
                "devicetoken='" + devicetoken + '\'' +
                ", studentid='" + studentid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", regnumber='" + regnumber + '\'' +
                ", photourl='" + photourl + '\'' +
                ", course='" + course + '\'' +
                ", department='" + department + '\'' +
                ", school='" + school + '\'' +
                ", currentsemester='" + currentsemester + '\'' +
                ", currentyear='" + currentyear + '\'' +
                ", currentacademicyear='" + currentacademicyear + '\'' +
                '}';
    }
}
