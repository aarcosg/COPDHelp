package com.aarcosg.copdhelp.mvp.view.medicalattention;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.mvp.view.View;

import io.realm.RealmResults;

public interface MedicalAttentionMainView extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindAllMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions);

    void showEmptyView();

    void hideEmptyView();

    void showLoadAllRealmErrorMessage();

    void showRemoveRealmSuccessMessage();

    void showRemoveRealmErrorMessage();
}
