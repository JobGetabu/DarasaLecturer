package com.job.darasalecturer.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Job on Sunday : 8/12/2018.
 */
public class QRParser implements Parcelable {
    private Location location;
    private ArrayList<String> courses;
    private String classtime;
    private String lecteachtimeid;
    private String unitname;
    private String unitcode;
    private Date date;


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

    public QRParser(Location location, ArrayList<String> courses, String classtime,
                    String lecteachtimeid, String unitname, String unitcode, Date date) {
        this.location = location;
        this.courses = courses;
        this.classtime = classtime;
        this.lecteachtimeid = lecteachtimeid;
        this.unitname = unitname;
        this.unitcode = unitcode;
        this.date = date;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public ArrayList<String> getCourses() {
        return courses;
    }

    public void setCourses(ArrayList<String> courses) {
        this.courses = courses;
    }

    public String getClasstime() {
        return classtime;
    }

    public void setClasstime(String classtime) {
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

    @Override
    public String toString() {
        return "QRParser{" +
                "location=" + location +
                ", courses=" + courses +
                ", classtime='" + classtime + '\'' +
                ", lecteachtimeid='" + lecteachtimeid + '\'' +
                ", unitname='" + unitname + '\'' +
                ", unitcode='" + unitcode + '\'' +
                ", date=" + date +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeStringList(this.courses);
        dest.writeString(this.classtime);
        dest.writeString(this.lecteachtimeid);
        dest.writeString(this.unitname);
        dest.writeString(this.unitcode);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected QRParser(Parcel in) {
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.courses = in.createStringArrayList();
        this.classtime = in.readString();
        this.lecteachtimeid = in.readString();
        this.unitname = in.readString();
        this.unitcode = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
    }

    public static final Creator<QRParser> CREATOR = new Creator<QRParser>() {
        @Override
        public QRParser createFromParcel(Parcel source) {
            return new QRParser(source);
        }

        @Override
        public QRParser[] newArray(int size) {
            return new QRParser[size];
        }
    };
}
