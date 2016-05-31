package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public class SmokeInteractorImpl implements SmokeInteractor {

    private static final String TAG = SmokeInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public SmokeInteractorImpl(){}


    @Override
    public Observable<RealmResults<Smoke>> realmFindAll(
            @Nullable Func1<RealmQuery<Smoke>, RealmQuery<Smoke>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<Smoke> query = getRealm().where(Smoke.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<Smoke> results;
        if(sortFieldName != null && sortOrder != null){
            results = query.findAllSorted(sortFieldName,sortOrder);
        }else{
            results = query.findAll();
        }
        return Observable.just(results);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Observable<Smoke> realmFindById(Long id) {
        return Observable.just(getRealm().where(Smoke.class)
                .equalTo("id",id).findFirst());
    }


    @Override
    public Observable<Smoke> realmCreate(Smoke smoke) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(smoke.getTimestamp());
        getRealm().beginTransaction();
        Smoke realmSmoke = getRealm().createObject(Smoke.class,
                PrimaryKeyFactory.getInstance().nextKey(Smoke.class));
        realmSmoke.setQuantity(smoke.getQuantity());
        realmSmoke.setTimestamp(smoke.getTimestamp());
        realmSmoke.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmSmoke.setMonth(calendar.get(Calendar.MONTH));
        realmSmoke.setYear(calendar.get(Calendar.YEAR));
        realmSmoke.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmSmoke.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmSmoke);
    }


    @Override
    public Observable<Smoke> realmUpdate(Long id, Smoke smoke) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(smoke.getTimestamp());
        getRealm().beginTransaction();
        Smoke realmSmoke = getRealm().where(Smoke.class)
                .equalTo("id",id).findFirst();
        realmSmoke.setQuantity(smoke.getQuantity());
        realmSmoke.setTimestamp(smoke.getTimestamp());
        realmSmoke.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmSmoke.setMonth(calendar.get(Calendar.MONTH));
        realmSmoke.setYear(calendar.get(Calendar.YEAR));
        realmSmoke.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmSmoke.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmSmoke);
    }


    @Override
    public Observable<Smoke> realmRemove(Long id) {
        getRealm().beginTransaction();
        Smoke realmSmoke = getRealm().where(Smoke.class)
                .equalTo("id",id).findFirst();
        realmSmoke.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmSmoke);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}