package com.job.darasalecturer.model;

/**
 * Created by Job on Monday : 12/24/2018.
 */
public class ClassListModel {

    private String name;
    private String regno;
    private String email;
    private StudentDetails studentDetails;

    public ClassListModel() {
    }

    public ClassListModel(String name, String regno, String email) {
        this.name = name;
        this.regno = regno;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegno() {
        return regno;
    }

    public void setRegno(String regno) {
        this.regno = regno;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "ClassListModel{" +
                "name='" + name + '\'' +
                ", regno='" + regno + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
