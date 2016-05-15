package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public interface RealmInteractor<T extends RealmObject> extends Interactor {

    Observable<RealmResults<T>> realmFindAll(
            @Nullable Func1<RealmQuery<T>, RealmQuery<T>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder);

    Observable<T> realmFindById(Long id);

    Observable<T> realmCreate(T t);

    Observable<T> realmUpdate(Long id, T t);

    Observable<T> realmRemove(Long id);
}
