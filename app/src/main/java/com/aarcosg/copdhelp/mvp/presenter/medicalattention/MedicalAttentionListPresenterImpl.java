package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionListView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionListPresenterImpl implements MedicalAttentionListPresenter {

    private MedicalAttentionListView mMedicalAttentionListView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MedicalAttentionListPresenterImpl(MedicalAttentionInteractor medicalAttentionInteractor){
        this.mMedicalAttentionInteractor = medicalAttentionInteractor;
    }

    @Override
    public void setView(View v) {
        mMedicalAttentionListView = (MedicalAttentionListView) v;
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
                            mMedicalAttentionListView.bindAllMedicalAttentions(medicalAttentions)

                    ,throwable ->
                            mMedicalAttentionListView.showLoadAllRealmErrorMessage()
                );
    }

    @Override
    public void removeMedicalAttention(Long id) {
        mSubscription = mMedicalAttentionInteractor.realmRemove(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmMedicalAttention ->
                                mMedicalAttentionListView.showRemoveRealmSuccessMessage()
                        ,throwable ->
                                mMedicalAttentionListView.showRemoveRealmErrorMessage()
                );
    }
}