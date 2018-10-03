package com.job.darasalecturer.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

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
public class QRParser implements Parcelable {
    private Location location;
    private ArrayList<String> courses;
    private Date classtime;
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

    public QRParser(Location location, ArrayList<String> courses, Date classtime,
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.location, flags);
        dest.writeStringList(this.courses);
        dest.writeLong(this.classtime != null ? this.classtime.getTime() : -1);
        dest.writeString(this.lecteachtimeid);
        dest.writeString(this.unitname);
        dest.writeString(this.unitcode);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
    }

    protected QRParser(Parcel in) {
        this.location = in.readParcelable(Location.class.getClassLoader());
        this.courses = in.createStringArrayList();
        long tmpClasstime = in.readLong();
        this.classtime = tmpClasstime == -1 ? null : new Date(tmpClasstime);
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
