package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public class BMIInteractorImpl implements BMIInteractor {

    private static final String TAG = BMIInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public BMIInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<RealmResults<BMI>> realmFindAll(
            @Nullable Func1<RealmQuery<BMI>, RealmQuery<BMI>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<BMI> query = getRealm().where(BMI.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<BMI> results;
        if(sortFieldName != null && sortOrder != null){
            results = query.findAllSorted(sortFieldName,sortOrder);
        }else{
            results = query.findAll();
        }
        return Observable.just(results);
    }

    @RxLogObservable
    @SuppressWarnings("unchecked")
    @Override
    public Observable<BMI> realmFindById(Long id) {
        return Observable.just(getRealm().where(BMI.class)
                .equalTo("id",id).findFirst());
    }

    @RxLogObservable
    @Override
    public Observable<BMI> realmCreate(BMI bmi) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bmi.getTimestamp());
        getRealm().beginTransaction();
        BMI realmBMI = getRealm().createObject(BMI.class,
                PrimaryKeyFactory.getInstance().nextKey(BMI.class));
        realmBMI.setWeight(bmi.getWeight());
        realmBMI.setHeight(bmi.getHeight());
        realmBMI.setBmi(bmi.getBmi());
        realmBMI.setTimestamp(bmi.getTimestamp());
        realmBMI.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmBMI.setMonth(calendar.get(Calendar.MONTH));
        realmBMI.setYear(calendar.get(Calendar.YEAR));
        realmBMI.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmBMI.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmBMI);
    }

    @RxLogObservable
    @Override
    public Observable<BMI> realmUpdate(Long id, BMI bmi) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(bmi.getTimestamp());
        getRealm().beginTransaction();
        BMI realmBMI = getRealm().where(BMI.class)
                .equalTo("id",id).findFirst();
        realmBMI.setWeight(bmi.getWeight());
        realmBMI.setHeight(bmi.getHeight());
        realmBMI.setBmi(bmi.getBmi());
        realmBMI.setTimestamp(bmi.getTimestamp());
        realmBMI.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmBMI.setMonth(calendar.get(Calendar.MONTH));
        realmBMI.setYear(calendar.get(Calendar.YEAR));
        realmBMI.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmBMI.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmBMI);
    }

    @RxLogObservable
    @Override
    public Observable<BMI> realmRemove(Long id) {
        getRealm().beginTransaction();
        BMI realmBMI = getRealm().where(BMI.class)
                .equalTo("id",id).findFirst();
        realmBMI.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmBMI);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}