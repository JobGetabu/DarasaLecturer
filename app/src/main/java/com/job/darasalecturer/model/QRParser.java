package com.job.darasalecturer.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Job on Sunday : 8/12/2018.
 */
public class QRParser implements Parcelable {
    private String latitude;
    private String longitude;
    private Date time;
    private String lecteachtimeid;
    private String unitname;
    private String unitcode;


    public QRParser() {
    }

    public QRParser(String latitude, String longitude, Date time,
                    String lecteachtimeid, String unitname, String unitcode) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.time =time;
        this.lecteachtimeid = lecteachtimeid;
        this.unitname = unitname;
        this.unitcode = unitcode;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String classToGson(Gson gson, QRParser qrParser){

        return gson.toJson(qrParser);

    }

    public QRParser gsonToQRParser(Gson gson, String decodedString){

        return gson.fromJson(decodedString , QRParser.class);

    }

    @Override
    public String toString() {
        return "QRParser{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", time=" + time +
                ", lecteachtimeid='" + lecteachtimeid + '\'' +
                ", unitname='" + unitname + '\'' +
                ", unitcode='" + unitcode + '\'' +
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
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
        dest.writeString(this.lecteachtimeid);
        dest.writeString(this.unitname);
        dest.writeString(this.unitcode);
    }

    protected QRParser(Parcel in) {
        this.latitude = in.readString();
        this.longitude = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.lecteachtimeid = in.readString();
        this.unitname = in.readString();
        this.unitcode = in.readString();
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
