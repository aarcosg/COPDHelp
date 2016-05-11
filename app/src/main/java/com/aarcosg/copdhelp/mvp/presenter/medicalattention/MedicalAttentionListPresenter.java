package com.aarcosg.copdhelp.mvp.presenter.medicalattention;


import com.aarcosg.copdhelp.mvp.presenter.Presenter;

public interface MedicalAttentionListPresenter extends Presenter {

    void loadAllMedicalAttentions();

    void removeMedicalAttention(Long id);
}
