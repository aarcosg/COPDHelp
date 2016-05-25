package com.aarcosg.copdhelp.data.realm.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject{

    @PrimaryKey
    private Long id;
    private Integer indexCOPDPS;
    private Integer indexCAT;
    private Integer indexBODE;
    private Integer indexBODEx;

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
}
