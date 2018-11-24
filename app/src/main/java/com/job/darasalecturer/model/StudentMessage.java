package com.job.darasalecturer.model;

/**
 * Created by Job on Saturday : 11/24/2018.
 */
public class StudentMessage {
    private String mUUID;
    private String studFirstName;
    private String studSecondName;
    private String regNo;

    public StudentMessage() {
    }

    public StudentMessage(String mUUID, String studFirstName, String studSecondName, String regNo) {
        this.mUUID = mUUID;
        this.studFirstName = studFirstName;
        this.studSecondName = studSecondName;
        this.regNo = regNo;
    }
}
