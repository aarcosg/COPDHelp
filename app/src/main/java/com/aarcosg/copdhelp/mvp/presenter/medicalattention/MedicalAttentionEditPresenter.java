package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.mvp.presenter.Presenter;

public interface MedicalAttentionEditPresenter extends Presenter {

    void loadMedicalAttention(Long id);

    void addMedicalAttention(MedicalAttention medicalAttention);

    void editMedicalAttention(Long id, MedicalAttention medicalAttention);
}
