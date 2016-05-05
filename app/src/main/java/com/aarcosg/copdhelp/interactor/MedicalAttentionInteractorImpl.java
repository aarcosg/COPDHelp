package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.entity.MedicalAttention;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.List;

import javax.inject.Inject;

import io.realm.Realm;
import rx.Observable;

public class MedicalAttentionInteractorImpl implements MedicalAttentionInteractor {

    private static final String TAG = MedicalAttentionInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public MedicalAttentionInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<List<MedicalAttention>> realmFindAll() {
        mRealm = Realm.getDefaultInstance();
        Observable<List<MedicalAttention>> observable = mRealm
                .where(MedicalAttention.class)
                .findAllAsync()
                .asObservable()
                .flatMap(results -> Observable.just(results.subList(0,results.size())));
        return observable;
    }

    @RxLogObservable
    @Override
    public Observable<MedicalAttention> realmFindById(int id) {
        mRealm = Realm.getDefaultInstance();
        Observable<MedicalAttention> observable = mRealm
                .where(MedicalAttention.class)
                .equalTo("id",id)
                .findFirstAsync()
                .asObservable();
        return observable;
    }

    @RxLogObservable
    @Override
    public Observable<Boolean> realmCreate(MedicalAttention medicalAttention) {
        mRealm = Realm.getDefaultInstance();
        try{
            mRealm.executeTransaction(realm -> {
                MedicalAttention realmMedicalAttention =
                        realm.createObject(MedicalAttention.class,
                                PrimaryKeyFactory.getInstance().nextKey(MedicalAttention.class));
                realmMedicalAttention.setType(medicalAttention.getType());
                realmMedicalAttention.setTime(medicalAttention.getTime());
                realmMedicalAttention.setNote(medicalAttention.getNote());
            });
            return Observable.just(true);
        } catch (IllegalArgumentException e){
            return Observable.just(false);
        }
    }

    @RxLogObservable
    @Override
    public Observable<Boolean> realmUpdate(MedicalAttention medicalAttention) {
        mRealm = Realm.getDefaultInstance();
        try{
            mRealm.executeTransaction(realm -> realm.copyToRealm(medicalAttention));
            return Observable.just(true);
        } catch (IllegalArgumentException e){
            return Observable.just(false);
        }
    }
}