package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionDetailsView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionDetailsPresenterImpl implements MedicalAttentionDetailsPresenter {

    private MedicalAttentionDetailsView mMedicalAttentionDetailsView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private Subscription mSubscription = Subscriptions.empty();
    private PublishSubject<Long> onEditButtonClickSubject = PublishSubject.create();

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

    @Override
    public void loadMedicalAttention(Long id) {
        mSubscription = mMedicalAttentionInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        medicalAttention ->
                                mMedicalAttentionDetailsView.bindMedicalAttention(medicalAttention)
                        ,throwable ->
                                mMedicalAttentionDetailsView.showMedicalAttentionNotFoundError()
                );
    }

    @Override
    public void onEditButtonClick(Long id){
        onEditButtonClickSubject.onNext(id);
    }

    @Override
    public PublishSubject<Long> getOnEditButtonClickSubject() {
        return onEditButtonClickSubject;
    }
}
