package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
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
    public void loadRealmObject(Long id) {
        mSubscription = mMedicalAttentionInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        medicalAttention ->
                                mMedicalAttentionEditView.bindRealmObject(medicalAttention)
                        ,throwable ->
                                mMedicalAttentionEditView.showRealmObjectNotFoundError()
                );
    }

    @Override
    public void addRealmObject(MedicalAttention medicalAttention) {
        mSubscription = mMedicalAttentionInteractor.realmCreate(medicalAttention)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmMedicalAttention ->
                                mMedicalAttentionEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mMedicalAttentionEditView.showSaveErrorMessage()
                );
    }

    @Override
    public void editRealmObject(Long id, MedicalAttention medicalAttention) {
        mSubscription = mMedicalAttentionInteractor.realmUpdate(id,medicalAttention)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmMedicalAttention ->
                                mMedicalAttentionEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mMedicalAttentionEditView.showSaveErrorMessage()
                );
    }
}
