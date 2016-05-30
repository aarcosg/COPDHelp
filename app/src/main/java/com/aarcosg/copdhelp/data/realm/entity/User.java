package com.aarcosg.copdhelp.data.realm.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject{

    public static final int GENRE_WOMAN = 0;
    public static final int GENRE_MAN = 1;

    @PrimaryKey
    private Long id;
    private Integer age;
    private Integer genre;
    private boolean smoker;
    private Integer smokingYears;
    private Integer indexCOPDPS;
    private Integer indexCAT;
    private Integer indexBODE;
    private Integer indexBODEx;
    private Integer scaleMMRC;
    private Double scaleBORG;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIndexCOPDPS() {
        return indexCOPDPS;
    }

    public void setIndexCOPDPS(Integer indexCOPDPS) {
        this.indexCOPDPS = indexCOPDPS;
    }

    public Integer getIndexCAT() {
        return indexCAT;
    }

    public void setIndexCAT(Integer indexCAT) {
        this.indexCAT = indexCAT;
    }

    public Integer getIndexBODE() {
        return indexBODE;
    }

    public void setIndexBODE(Integer indexBODE) {
        this.indexBODE = indexBODE;
    }

    public Integer getIndexBODEx() {
        return indexBODEx;
    }

    public void setIndexBODEx(Integer indexBODEx) {
        this.indexBODEx = indexBODEx;
    }

    public Integer getScaleMMRC() {
        return scaleMMRC;
    }

    public void setScaleMMRC(Integer scaleMMRC) {
        this.scaleMMRC = scaleMMRC;
    }

    public Double getScaleBORG() {
        return scaleBORG;
    }

    public void setScaleBORG(Double scaleBORG) {
        this.scaleBORG = scaleBORG;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getGenre() {
        return genre;
    }

    public void setGenre(Integer genre) {
        this.genre = genre;
    }

    public boolean isSmoker() {
        return smoker;
    }

    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }

    public Integer getSmokingYears() {
        return smokingYears;
    }

    public void setSmokingYears(Integer smokingYears) {
        this.smokingYears = smokingYears;
    }
}
