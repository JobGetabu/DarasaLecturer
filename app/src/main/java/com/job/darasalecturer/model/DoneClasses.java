package com.job.darasalecturer.model;

/**
 * Created by Job on Thursday : 9/27/2018.
 */
public class DoneClasses {

    private String lecteachtimeid;
    private String unitname;
    private String unitcode;
    private int number;

    public DoneClasses() {
    }

    public DoneClasses(String lecteachtimeid, String unitname, String unitcode, int number) {
        this.lecteachtimeid = lecteachtimeid;
        this.unitname = unitname;
        this.unitcode = unitcode;
        this.number = number;
    }

    public DoneClasses(String unitname, String unitcode, int number) {
        this.unitname = unitname;
        this.unitcode = unitcode;
        this.number = number;
    }

    public String getLecteachtimeid() {
        return lecteachtimeid;
    }

    public void setLecteachtimeid(String lecteachtimeid) {
        this.lecteachtimeid = lecteachtimeid;
    }

    public String getUnitname() {
        return unitname;
    }

    public void setUnitname(String unitname) {
        this.unitname = unitname;
    }

    public String getUnitcode() {
        return unitcode;
    }

    public void setUnitcode(String unitcode) {
        this.unitcode = unitcode;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "DoneClasses{" +
                "lecteachtimeid='" + lecteachtimeid + '\'' +
                ", unitname='" + unitname + '\'' +
                ", unitcode='" + unitcode + '\'' +
                ", number=" + number +
                '}';
    }
}
