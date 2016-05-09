package com.aarcosg.copdhelp.data.realm.entity;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class MedicalAttention extends RealmObject{

    @PrimaryKey
    private Long id;
    private Integer type;
    private Date date;
    private String note;

    public MedicalAttention() {
    }

    public MedicalAttention(Integer type, Date date, String note) {
        this.type = type;
        this.date = date;
        this.note = note;
    }

    public MedicalAttention(Long id, Integer type, Date date, String note) {
        this.id = id;
        this.type = type;
        this.date = date;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
