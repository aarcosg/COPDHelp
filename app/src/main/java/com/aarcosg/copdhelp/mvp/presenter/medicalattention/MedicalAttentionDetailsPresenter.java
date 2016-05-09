package com.aarcosg.copdhelp.mvp.presenter.medicalattention;


import com.aarcosg.copdhelp.mvp.presenter.Presenter;

import rx.subjects.PublishSubject;

public interface MedicalAttentionDetailsPresenter extends Presenter {

    void loadMedicalAttention(Long id);

    void onEditButtonClick(Long id);

    PublishSubject<Long> getOnEditButtonClickSubject();
}
