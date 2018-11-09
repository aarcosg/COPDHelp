package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.realm.entity.User;

import rx.Observable;

public interface UserInteractor extends RealmInteractor<User> {


    Observable<User> updateCOPDIndexResult(Long id, String realmCOPDIndexTable, int result);


    Observable<User> updateCOPDScaleGrade(Long id, String realmCOPDScaleTable, double grade);

    User realmCreateIfNotExists(Long id);
}
