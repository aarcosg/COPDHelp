package com.aarcosg.copdhelp.mvp.presenter.bmi;

import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIDetailsView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

public class BMIDetailsPresenterImpl implements BMIDetailsPresenter {

    private BMIDetailsView mBMIDetailsView;
    private final BMIInteractor mBMIInteractor;
    private Subscription mSubscription = Subscriptions.empty();
    private PublishSubject<Long> onEditButtonClickSubject = PublishSubject.create();

    @Inject
    public BMIDetailsPresenterImpl(BMIInteractor BMIInteractor){
        this.mBMIInteractor = BMIInteractor;
    }

    @Override
    public void setView(View v) {
        mBMIDetailsView = (BMIDetailsView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadRealmObject(Long id) {
        mSubscription = mBMIInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bmi ->
                                mBMIDetailsView.bindRealmObject(bmi)
                        ,throwable ->
                                mBMIDetailsView.showRealmObjectNotFoundError()
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
