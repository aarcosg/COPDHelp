package com.aarcosg.copdhelp.mvp.presenter.smoke;

import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeDetailsView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

public class SmokeDetailsPresenterImpl implements SmokeDetailsPresenter {

    private SmokeDetailsView mSmokeDetailsView;
    private final SmokeInteractor mSmokeInteractor;
    private Subscription mSubscription = Subscriptions.empty();
    private PublishSubject<Long> onEditButtonClickSubject = PublishSubject.create();

    @Inject
    public SmokeDetailsPresenterImpl(SmokeInteractor smokeInteractor){
        this.mSmokeInteractor = smokeInteractor;
    }

    @Override
    public void setView(View v) {
        mSmokeDetailsView = (SmokeDetailsView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadRealmObject(Long id) {
        mSubscription = mSmokeInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmSmoke ->
                                mSmokeDetailsView.bindRealmObject(realmSmoke)
                        ,throwable ->
                                mSmokeDetailsView.showRealmObjectNotFoundError()
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
