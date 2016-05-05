package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.data.entity.MedicalAttention;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionEditView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
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

    @Override
    public void loadMedicalAttention(int id) {
        mSubscription = mMedicalAttentionInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(medicalAttention ->
                        mMedicalAttentionEditView.bindMedicalAttention(medicalAttention)
                );
    }

    @Override
    public void createMedicalAttention(MedicalAttention medicalAttention) {
        mSubscription = mMedicalAttentionInteractor.realmCreate(medicalAttention)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if(success){
                        mMedicalAttentionEditView.onRealmSuccess();
                    }else{
                        mMedicalAttentionEditView.onRealmError();
                    }
                });
    }

    @Override
    public void editMedicalAttention(MedicalAttention medicalAttention) {
        mSubscription = mMedicalAttentionInteractor.realmUpdate(medicalAttention)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(success -> {
                    if(success){
                        mMedicalAttentionEditView.onRealmSuccess();
                    }else{
                        mMedicalAttentionEditView.onRealmError();
                    }
                });
    }
}
