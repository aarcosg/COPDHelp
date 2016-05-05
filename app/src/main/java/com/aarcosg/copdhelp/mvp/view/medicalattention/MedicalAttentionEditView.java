package com.aarcosg.copdhelp.mvp.view.medicalattention;

import com.aarcosg.copdhelp.data.entity.MedicalAttention;
import com.aarcosg.copdhelp.mvp.view.View;

public interface MedicalAttentionEditView extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindMedicalAttention(MedicalAttention medicalAttention);

    void onRealmSuccess();

    void onRealmError();
}
