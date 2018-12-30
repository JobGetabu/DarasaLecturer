package com.job.darasalecturer.model;

import com.google.firebase.Timestamp;

import java.util.List;

/**
 * Created by Job on Sunday : 12/30/2018.
 * holds the dates of saved classes atomically added or removed
 */
public class SavedClasses {
    private List<Timestamp> classes;

    public SavedClasses() {
    }

    public SavedClasses(List<Timestamp> classes) {
        this.classes = classes;
    }

    public List<Timestamp> getClasses() {
        return classes;
    }

    public void setClasses(List<Timestamp> classes) {
        this.classes = classes;
    }
}
