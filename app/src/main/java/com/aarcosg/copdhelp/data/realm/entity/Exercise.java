package com.aarcosg.copdhelp.data.realm.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Exercise extends RealmObject{

    public static final int TYPE_WALKING = 0;
    public static final int TYPE_STATIONARY_BICYCLE = 1;
    public static final int TYPE_BREATHING = 2;
    public static final int TYPE_ARMS = 3;

    @PrimaryKey
    private Long id;
    private Integer type;
    private Integer duration;
    private String note;
    private Date timestamp;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer dayOfWeek;
    private Integer weekOfYear;

    public Exercise() {
    }

    public Exercise(Integer type, Integer duration, String note, Date timestamp) {
        this.type = type;
        this.duration = duration;
        this.note = note;
        this.timestamp = timestamp;
    }

    public Exercise(Long id, Integer type, Integer duration, String note, Date timestamp) {
        this.id = id;
        this.type = type;
        this.duration = duration;
        this.note = note;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getWeekOfYear() {
        return weekOfYear;
    }

    public void setWeekOfYear(Integer weekOfYear) {
        this.weekOfYear = weekOfYear;
    }
}
