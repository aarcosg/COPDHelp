package com.aarcosg.copdhelp.data.realm.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class BMI extends RealmObject{

    @PrimaryKey
    private Long id;
    private Double height;
    private Double weight;
    private Double bmi;
    private Date timestamp;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer dayOfWeek;
    private Integer weekOfYear;

    public BMI() {
    }

    public BMI(Double height, Double weight, Double bmi, Date timestamp) {
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.timestamp = timestamp;
    }

    public BMI(Long id, Double height, Double weight, Double bmi, Date timestamp) {
        this.id = id;
        this.height = height;
        this.weight = weight;
        this.bmi = bmi;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
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
