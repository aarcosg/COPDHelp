package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.entity.MedicalAttention;

import java.util.List;

import rx.Observable;

public interface MedicalAttentionInteractor extends Interactor {

    Observable<List<MedicalAttention>> realmFindAll();

    Observable<MedicalAttention> realmFindById(int id);

    Observable<Boolean> realmCreate(MedicalAttention medicalAttention);

    Observable<Boolean> realmUpdate(MedicalAttention medicalAttention);
}
