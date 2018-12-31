package com.job.darasalecturer.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.job.darasalecturer.util.Constants.DATE_SCAN_FORMAT;

/**
 * Created by Job on Sunday : 12/30/2018.
 * holds the dates of saved classes atomically added or removed
 */
public class SavedClasses {
  private int number;
  @ServerTimestamp
  private Date date;
  private String formatedDate;

    public SavedClasses() {
    }

    public SavedClasses(int number, Date date, String formatedDate) {
        this.number = number;
        this.date = date;
        this.formatedDate = formatedDate;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getFormatedDate() {
        return formatedDate;
    }

    public void setFormatedDate(String formatedDate) {
        this.formatedDate = formatedDate;
    }

    public static String formatDate(Date date){

        //"EEE, MMM d, ''yy"     =>  Wed, Jul 4, '01
        DateFormat dateFormat2 = new SimpleDateFormat("EEE, MMM d, ''yy");
        return  (dateFormat2.format(date));
    }

    public static String formatDateKey(Date date){

        //"EEE, MMM d, ''yy"     =>  Wed, Jul 4, '01
        DateFormat dateFormat2 = new SimpleDateFormat(DATE_SCAN_FORMAT);
        return  (dateFormat2.format(date));
    }

    @Override
    public String toString() {
        return "SavedClasses{" +
                "number=" + number +
                ", date=" + date +
                ", formatedDate='" + formatedDate + '\'' +
                '}';
    }
}
