package com.aarcosg.copdhelp.data.realm.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MedicalAttention extends RealmObject{

    public static final int TYPE_CHECKUP = 0;
    public static final int TYPE_EMERGENCY = 1;

    @PrimaryKey
    private Long id;
    private Integer type;
    private Date timestamp;
    private String note;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer dayOfWeek;
    private Integer weekOfYear;

    public MedicalAttention() {
    }

    public MedicalAttention(Integer type, Date timestamp, String note) {
        this.type = type;
        this.timestamp = timestamp;
        this.note = note;
    }

    public MedicalAttention(Long id, Integer type, Date timestamp, String note) {
        this.id = id;
        this.type = type;
        this.timestamp = timestamp;
        this.note = note;
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
