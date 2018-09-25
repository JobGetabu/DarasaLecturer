package com.job.darasalecturer.model;

/**
 * Created by Job on Wednesday : 8/8/2018.
 */

public class LecAuth {
    private String localpasscode;

    public LecAuth() {
    }

    public LecAuth(String localpasscode) {
        this.localpasscode = localpasscode;
    }

    public String getLocalpasscode() {
        return localpasscode;
    }

    public void setLocalpasscode(String localpasscode) {
        this.localpasscode = localpasscode;
    }

    @Override
    public String toString() {
        return "LecAuth{" +
                "localpasscode='" + localpasscode + '\'' +
                '}';
    }
}
