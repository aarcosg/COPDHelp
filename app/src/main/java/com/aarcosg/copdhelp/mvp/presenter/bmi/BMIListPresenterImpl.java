package com.aarcosg.copdhelp.mvp.presenter.bmi;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIListView;

import javax.inject.Inject;

import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class BMIListPresenterImpl implements BMIListPresenter {

    private BMIListView mBMIListView;
    private final BMIInteractor mBMIInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public BMIListPresenterImpl(BMIInteractor BMIInteractor){
        this.mBMIInteractor = BMIInteractor;
    }

    @Override
    public void setView(View v) {
        mBMIListView = (BMIListView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadAllFromRealm() {
        mSubscription = mBMIInteractor.realmFindAll(
                    null
                    ,RealmTable.BMI.TIMESTAMP
                    ,Sort.DESCENDING)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    BMIs ->
                            mBMIListView.bindAll(BMIs)

                    ,throwable ->
                            mBMIListView.showLoadAllRealmErrorMessage()
                );
    }

    @Override
    public void removeFromRealm(Long id) {
        mSubscription = mBMIInteractor.realmRemove(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmBMI ->
                                mBMIListView.showRemoveRealmSuccessMessage()
                        ,throwable ->
                                mBMIListView.showRemoveRealmErrorMessage()
                );
    }
}