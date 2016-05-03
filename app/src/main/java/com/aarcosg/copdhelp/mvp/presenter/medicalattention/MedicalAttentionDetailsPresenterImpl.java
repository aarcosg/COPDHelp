package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionDetailsView;
import com.aarcosg.copdhelp.mvp.view.View;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionDetailsPresenterImpl implements MedicalAttentionDetailsPresenter {

    private MedicalAttentionDetailsView mMedicalAttentionDetailsView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MedicalAttentionDetailsPresenterImpl(MedicalAttentionInteractor medicalAttentionInteractor){
        this.mMedicalAttentionInteractor = medicalAttentionInteractor;
    }

    @Override
    public void setView(View v) {
        mMedicalAttentionDetailsView = (MedicalAttentionDetailsView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

}
