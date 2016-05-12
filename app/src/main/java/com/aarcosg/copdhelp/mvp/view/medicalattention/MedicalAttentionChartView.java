package com.aarcosg.copdhelp.mvp.view.medicalattention;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.mvp.view.View;

import io.realm.RealmResults;

public interface MedicalAttentionChartView extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindWeekMedicalAttentions(RealmResults<MedicalAttention> medicalAttentions);

    void showLoadWeekRealmErrorMessage();
}
