package com.job.darasalecturer.model;

import com.google.firebase.Timestamp;

/**
 * Created by Job on Monday : 12/17/2018.
 * model class notification
 */
public class Notif {
    private String topic;
    private String title;
    private String message;
    private Timestamp time;

    public Notif() {
    }

    public Notif(String topic, String title, String message, Timestamp time) {
        this.topic = topic;
        this.title = title;
        this.message = message;
        this.time = time;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Notif{" +
                "topic='" + topic + '\'' +
                ", title='" + title + '\'' +
                ", message='" + message + '\'' +
                ", time=" + time +
                '}';
    }
}
