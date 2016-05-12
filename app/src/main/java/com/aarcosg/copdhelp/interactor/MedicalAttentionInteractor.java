package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;

import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public interface MedicalAttentionInteractor extends Interactor {

    Observable<RealmResults<MedicalAttention>> realmFindAll(
            @Nullable Func1<RealmQuery<MedicalAttention>, RealmQuery<MedicalAttention>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder);

    Observable<MedicalAttention> realmFindById(Long id);

    Observable<MedicalAttention> realmCreate(MedicalAttention medicalAttention);

    Observable<MedicalAttention> realmUpdate(Long id, MedicalAttention medicalAttention);

    Observable<MedicalAttention> realmRemove(Long id);
}
