package com.job.darasalecturer.model;

import android.support.annotation.Keep;

/**
 * Created by Job on Tuesday : 7/24/2018.
 */

@Keep
public class LecUser {
    private String firstname;
    private String lastname;
    private String devicetoken;
    private String school;
    private String department;
    private String currentsemester;
    private String currentyear;
    private String currentacademicyear;

    public LecUser() {
    }

    public LecUser(String firstname, String lastname,
                   String devicetoken, String school, String department,
                   String currentsemester, String currentyear, String currentacademicyear) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.devicetoken = devicetoken;
        this.school = school;
        this.department = department;
        this.currentsemester = currentsemester;
        this.currentyear = currentyear;
        this.currentacademicyear = currentacademicyear;
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

    public String getDevicetoken() {
        return devicetoken;
    }

    public void setDevicetoken(String devicetoken) {
        this.devicetoken = devicetoken;
    }

    @Override
    public String toString() {
        return "LecUser{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", devicetoken='" + devicetoken + '\'' +
                ", school='" + school + '\'' +
                ", department='" + department + '\'' +
                ", currentsemester='" + currentsemester + '\'' +
                ", currentyear='" + currentyear + '\'' +
                ", currentacademicyear='" + currentacademicyear + '\'' +
                '}';
    }
}
