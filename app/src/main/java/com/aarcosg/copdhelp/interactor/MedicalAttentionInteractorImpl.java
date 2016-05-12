package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
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

public class MedicalAttentionInteractorImpl implements MedicalAttentionInteractor {

    private static final String TAG = MedicalAttentionInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public MedicalAttentionInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<RealmResults<MedicalAttention>> realmFindAll(
            @Nullable Func1<RealmQuery<MedicalAttention>, RealmQuery<MedicalAttention>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<MedicalAttention> query = getRealm().where(MedicalAttention.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<MedicalAttention> results;
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
    public Observable<MedicalAttention> realmFindById(Long id) {
        return Observable.just(getRealm().where(MedicalAttention.class)
                .equalTo("id",id).findFirst());
    }

    @RxLogObservable
    @Override
    public Observable<MedicalAttention> realmCreate(MedicalAttention medicalAttention) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicalAttention.getTimestamp());
        getRealm().beginTransaction();
        MedicalAttention realmMedicalAttention = getRealm().createObject(MedicalAttention.class,
                PrimaryKeyFactory.getInstance().nextKey(MedicalAttention.class));
        realmMedicalAttention.setType(medicalAttention.getType());
        realmMedicalAttention.setTimestamp(medicalAttention.getTimestamp());
        realmMedicalAttention.setNote(medicalAttention.getNote());
        realmMedicalAttention.setDay(calendar.get(Calendar.DAY_OF_YEAR));
        realmMedicalAttention.setMonth(calendar.get(Calendar.MONTH));
        realmMedicalAttention.setYear(calendar.get(Calendar.YEAR));
        realmMedicalAttention.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmMedicalAttention.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmMedicalAttention);
    }

    @RxLogObservable
    @Override
    public Observable<MedicalAttention> realmUpdate(Long id, MedicalAttention medicalAttention) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicalAttention.getTimestamp());
        getRealm().beginTransaction();
        MedicalAttention realmMedicalAttention = getRealm().where(MedicalAttention.class)
                .equalTo("id",id).findFirst();
        realmMedicalAttention.setType(medicalAttention.getType());
        realmMedicalAttention.setTimestamp(medicalAttention.getTimestamp());
        realmMedicalAttention.setNote(medicalAttention.getNote());
        realmMedicalAttention.setDay(calendar.get(Calendar.DAY_OF_YEAR));
        realmMedicalAttention.setMonth(calendar.get(Calendar.MONTH));
        realmMedicalAttention.setYear(calendar.get(Calendar.YEAR));
        realmMedicalAttention.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmMedicalAttention.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmMedicalAttention);
    }

    @RxLogObservable
    @Override
    public Observable<MedicalAttention> realmRemove(Long id) {
        getRealm().beginTransaction();
        MedicalAttention realmMedicalAttention = getRealm().where(MedicalAttention.class)
                .equalTo("id",id).findFirst();
        realmMedicalAttention.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmMedicalAttention);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}