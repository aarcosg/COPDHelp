package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionMainView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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

    @Override
    public void loadAllMedicalAttentions() {
        mSubscription = mMedicalAttentionInteractor.realmFindAll()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    medicalAttentions ->
                            mMedicalAttentionMainView.bindAllMedicalAttentions(medicalAttentions)

                    ,throwable ->
                            mMedicalAttentionMainView.showLoadAllRealmErrorMessage()
                );
    }

    @Override
    public void removeMedicalAttention(Long id) {
        mSubscription = mMedicalAttentionInteractor.realmRemove(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmMedicalAttention ->
                                mMedicalAttentionMainView.showRemoveRealmSuccessMessage()
                        ,throwable ->
                                mMedicalAttentionMainView.showRemoveRealmErrorMessage()
                );
    }
}