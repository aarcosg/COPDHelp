package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.data.entity.MedicalAttention;
import com.aarcosg.copdhelp.mvp.presenter.Presenter;

public interface MedicalAttentionEditPresenter extends Presenter {

    void loadMedicalAttention(int id);

    void createMedicalAttention(MedicalAttention medicalAttention);

    void editMedicalAttention(MedicalAttention medicalAttention);
}
