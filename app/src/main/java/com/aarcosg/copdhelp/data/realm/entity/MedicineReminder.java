package com.aarcosg.copdhelp.data.realm.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MedicineReminder extends RealmObject{

    @PrimaryKey
    private Long id;
    private boolean enabled;
    private String medicine;
    private Integer frequency;
    private String dose;
    private Date timestamp;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer dayOfWeek;
    private Integer weekOfYear;

    public MedicineReminder() {
    }

    public MedicineReminder(String medicine, Integer frequency, String dose, Date timestamp) {
        this.medicine = medicine;
        this.frequency = frequency;
        this.dose = dose;
        this.timestamp = timestamp;
    }

    public MedicineReminder(Long id, String medicine, Integer frequency, String dose, Date timestamp) {
        this.id = id;
        this.medicine = medicine;
        this.frequency = frequency;
        this.dose = dose;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getMedicine() {
        return medicine;
    }

    public void setMedicine(String medicine) {
        this.medicine = medicine;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
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
