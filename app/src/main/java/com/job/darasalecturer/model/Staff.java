package com.job.darasalecturer.model;

import androidx.annotation.Keep;

/**
 * Created by Job on Tuesday : 7/24/2018.
 */

@Keep
public class Staff {
    private String uid;
    private String firstname;
    private String lastname;
    private String email;
    private String initials;
    private String role;


    public Staff() {
    }

    public Staff(String uid, String firstname, String lastname, String email, String initials, String role) {
        this.uid = uid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.initials = initials;
        this.role = role;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "uid='" + uid + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                ", initials='" + initials + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
