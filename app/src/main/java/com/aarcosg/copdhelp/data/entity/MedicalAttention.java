package com.aarcosg.copdhelp.data.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MedicalAttention extends RealmObject{

    @PrimaryKey
    private Integer id;
    private Integer type;
    private Date time;
    private String note;

    public MedicalAttention() {
    }

    public MedicalAttention(Integer type, Date time, String note) {
        this.type = type;
        this.time = time;
        this.note = note;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
