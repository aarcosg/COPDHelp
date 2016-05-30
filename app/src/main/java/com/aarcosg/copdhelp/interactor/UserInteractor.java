package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.realm.entity.User;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import rx.Observable;

public interface UserInteractor extends RealmInteractor<User> {

    @RxLogObservable
    Observable<User> updateCOPDIndexResult(Long id, String realmCOPDIndexTable, int result);

    @RxLogObservable
    Observable<User> updateCOPDScaleGrade(Long id, String realmCOPDScaleTable, double grade);

    User realmCreateIfNotExists(Long id);
}
