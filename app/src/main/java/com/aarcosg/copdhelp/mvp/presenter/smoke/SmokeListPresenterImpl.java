package com.aarcosg.copdhelp.mvp.presenter.smoke;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeListView;

import javax.inject.Inject;

import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class SmokeListPresenterImpl implements SmokeListPresenter {

    private SmokeListView mSmokeListView;
    private final SmokeInteractor mSmokeInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public SmokeListPresenterImpl(SmokeInteractor smokeInteractor){
        this.mSmokeInteractor = smokeInteractor;
    }

    @Override
    public void setView(View v) {
        mSmokeListView = (SmokeListView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadAllFromRealm() {
        mSubscription = mSmokeInteractor.realmFindAll(
                    null
                    ,RealmTable.Smoke.TIMESTAMP
                    ,Sort.DESCENDING)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    smokeList ->
                            mSmokeListView.bindAll(smokeList)
                    ,throwable ->
                            mSmokeListView.showLoadAllRealmErrorMessage()
                );
    }

    @Override
    public void removeFromRealm(Long id) {
        mSubscription = mSmokeInteractor.realmRemove(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmSmoke ->
                                mSmokeListView.showRemoveRealmSuccessMessage()
                        ,throwable ->
                                mSmokeListView.showRemoveRealmErrorMessage()
                );
    }
}