package com.aarcosg.copdhelp.data.entity;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class User extends RealmObject{

    @PrimaryKey
    private Integer id;

    public User() {
    }

    public User(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
