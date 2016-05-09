package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;

import io.realm.RealmResults;
import rx.Observable;

public interface MedicalAttentionInteractor extends Interactor {

    Observable<RealmResults<MedicalAttention>> realmFindAll();

    Observable<MedicalAttention> realmFindById(Long id);

    Observable<MedicalAttention> realmCreate(MedicalAttention medicalAttention);

    Observable<MedicalAttention> realmUpdate(Long id, MedicalAttention medicalAttention);

    Observable<MedicalAttention> realmRemove(Long id);
}
