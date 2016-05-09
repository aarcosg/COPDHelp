package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;

public class MedicalAttentionInteractorImpl implements MedicalAttentionInteractor {

    private static final String TAG = MedicalAttentionInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public MedicalAttentionInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<RealmResults<MedicalAttention>> realmFindAll() {
        return Observable.just(getRealm().where(MedicalAttention.class)
                .findAllSorted("date",Sort.DESCENDING));
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
        getRealm().beginTransaction();
        MedicalAttention realmMedicalAttention = getRealm().createObject(MedicalAttention.class,
                PrimaryKeyFactory.getInstance().nextKey(MedicalAttention.class));
        realmMedicalAttention.setType(medicalAttention.getType());
        realmMedicalAttention.setDate(medicalAttention.getDate());
        realmMedicalAttention.setNote(medicalAttention.getNote());
        getRealm().commitTransaction();
        return Observable.just(realmMedicalAttention);
    }

    @RxLogObservable
    @Override
    public Observable<MedicalAttention> realmUpdate(Long id, MedicalAttention medicalAttention) {
        getRealm().beginTransaction();
        MedicalAttention realmMedicalAttention = getRealm().where(MedicalAttention.class)
                .equalTo("id",id).findFirst();
        realmMedicalAttention.setType(medicalAttention.getType());
        realmMedicalAttention.setDate(medicalAttention.getDate());
        realmMedicalAttention.setNote(medicalAttention.getNote());
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