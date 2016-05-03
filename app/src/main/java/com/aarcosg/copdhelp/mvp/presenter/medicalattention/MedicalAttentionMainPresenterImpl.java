package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionMainView;
import com.aarcosg.copdhelp.mvp.view.View;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionMainPresenterImpl implements MedicalAttentionMainPresenter {

    private MedicalAttentionMainView mMedicalAttentionMainView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MedicalAttentionMainPresenterImpl(MedicalAttentionInteractor medicalAttentionInteractor){
        this.mMedicalAttentionInteractor = medicalAttentionInteractor;
    }

    @Override
    public void setView(View v) {
        mMedicalAttentionMainView = (MedicalAttentionMainView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

}