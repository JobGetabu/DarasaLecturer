package com.job.darasalecturer.datasource;

/**
 * Created by Job on Tuesday : 8/14/2018.
 */
public class StudentDetails {

    private String studentid;
    private String firstname;
    private String lastname;
    private String course;
    private String regnumber;
    private String photourl;

    public StudentDetails() {
    }

    public StudentDetails(String studentid, String firstname, String lastname, String course, String regnumber, String photourl) {
        this.studentid = studentid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.course = course;
        this.regnumber = regnumber;
        this.photourl = photourl;
    }

    @Override
    public String toString() {
        return "StudentDetails{" +
                "studentid='" + studentid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", course='" + course + '\'' +
                ", regnumber='" + regnumber + '\'' +
                '}';
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
}
