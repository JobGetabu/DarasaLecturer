package com.job.darasalecturer.model;

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
    private String latitude;
    private String longitude;
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

    public QRParser(String latitude, String longitude, ArrayList<String> courses, Date classtime,
                    String lecteachtimeid, String unitname, String unitcode, Date date, String semester, String year) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.courses = courses;
        this.classtime = classtime;
        this.lecteachtimeid = lecteachtimeid;
        this.unitname = unitname;
        this.unitcode = unitcode;
        this.date = date;
        this.semester = semester;
        this.year = year;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    @Override
    public String toString() {
        return "QRParser{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", courses=" + courses +
                ", classtime=" + classtime +
                ", lecteachtimeid='" + lecteachtimeid + '\'' +
                ", unitname='" + unitname + '\'' +
                ", unitcode='" + unitcode + '\'' +
                ", date=" + date +
                ", semester='" + semester + '\'' +
                ", year='" + year + '\'' +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeStringList(this.courses);
        dest.writeLong(this.classtime != null ? this.classtime.getTime() : -1);
        dest.writeString(this.lecteachtimeid);
        dest.writeString(this.unitname);
        dest.writeString(this.unitcode);
        dest.writeLong(this.date != null ? this.date.getTime() : -1);
        dest.writeString(this.semester);
        dest.writeString(this.year);
    }

    protected QRParser(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.courses = in.createStringArrayList();
        long tmpClasstime = in.readLong();
        this.classtime = tmpClasstime == -1 ? null : new Date(tmpClasstime);
        this.lecteachtimeid = in.readString();
        this.unitname = in.readString();
        this.unitcode = in.readString();
        long tmpDate = in.readLong();
        this.date = tmpDate == -1 ? null : new Date(tmpDate);
        this.semester = in.readString();
        this.year = in.readString();
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
