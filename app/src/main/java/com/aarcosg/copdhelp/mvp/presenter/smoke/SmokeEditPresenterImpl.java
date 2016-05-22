package com.aarcosg.copdhelp.mvp.presenter.smoke;

import com.aarcosg.copdhelp.data.realm.entity.Smoke;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeEditView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class SmokeEditPresenterImpl implements SmokeEditPresenter {

    private SmokeEditView mSmokeEditView;
    private final SmokeInteractor mSmokeInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public SmokeEditPresenterImpl(SmokeInteractor smokeInteractor){
        this.mSmokeInteractor = smokeInteractor;
    }

    @Override
    public void setView(View v) {
        mSmokeEditView = (SmokeEditView) v;
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
                                mSmokeEditView.bindRealmObject(realmSmoke)
                        ,throwable ->
                                mSmokeEditView.showRealmObjectNotFoundError()
                );
    }

    @Override
    public void addRealmObject(Smoke smoke) {
        mSubscription = mSmokeInteractor.realmCreate(smoke)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmSmoke ->
                                mSmokeEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mSmokeEditView.showSaveErrorMessage()
                );
    }

    @Override
    public void editRealmObject(Long id, Smoke smoke) {
        mSubscription = mSmokeInteractor.realmUpdate(id,smoke)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmSmoke ->
                                mSmokeEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mSmokeEditView.showSaveErrorMessage()
                );
    }
}
