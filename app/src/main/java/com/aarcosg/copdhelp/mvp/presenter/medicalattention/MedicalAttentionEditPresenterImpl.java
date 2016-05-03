package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionEditView;
import com.aarcosg.copdhelp.mvp.view.View;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionEditPresenterImpl implements MedicalAttentionEditPresenter {

    private MedicalAttentionEditView mMedicalAttentionEditView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MedicalAttentionEditPresenterImpl(MedicalAttentionInteractor medicalAttentionInteractor){
        this.mMedicalAttentionInteractor = medicalAttentionInteractor;
    }

    @Override
    public void setView(View v) {
        mMedicalAttentionEditView = (MedicalAttentionEditView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

}
