package com.aarcosg.copdhelp.mvp.presenter.medicalattention;


import com.aarcosg.copdhelp.mvp.presenter.Presenter;

public interface MedicalAttentionDetailsPresenter extends Presenter {

    void loadMedicalAttention(Long id);

}
