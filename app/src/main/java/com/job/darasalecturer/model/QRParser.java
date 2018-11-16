package com.job.darasalecturer.model;

import android.support.annotation.Keep;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Job on Sunday : 8/12/2018.
 */

@Keep
public class QRParser   {
    private ArrayList<String> courses;
    private Date classtime;
    private String lecteachtimeid;
    private String unitname;
    private String unitcode;
    private Date date;
    private String semester;
    private String year;


    public QRParser() {
    }

    public String classToGson(Gson gson, QRParser qrParser) {

        return gson.toJson(qrParser);

    }

    public QRParser gsonToQRParser(Gson gson, String decodedString) {

        try {

            return gson.fromJson(decodedString, QRParser.class);
        }catch (JsonParseException e) {
            return null;
        }
    }





    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public Date getClasstime() {
        return classtime;
    }

    public void setClasstime(Date classtime) {
        this.classtime = classtime;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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


    private String lessonTimeInString(Date timestamp) {
        //Timestamp timestamp = model.getTimestamp();
        if (timestamp != null) {

            Date date = timestamp;
            Calendar c = Calendar.getInstance();
            c.setTime(date);

            DateFormat dateFormat2 = new SimpleDateFormat("hh.mm aa");
            return dateFormat2.format(date);
        }
        return null;
    }


}
