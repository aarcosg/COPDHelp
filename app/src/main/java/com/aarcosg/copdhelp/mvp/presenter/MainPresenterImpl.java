package com.aarcosg.copdhelp.mvp.presenter;

import com.aarcosg.copdhelp.interactor.MainInteractor;
import com.aarcosg.copdhelp.mvp.view.MainView;
import com.aarcosg.copdhelp.mvp.view.View;

import javax.inject.Inject;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MainPresenterImpl implements MainPresenter{

    private MainView mMainView;
    private final MainInteractor mMainInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MainPresenterImpl(MainInteractor mainInteractor){
        this.mMainInteractor = mainInteractor;
    }

    @Override
    public void setView(View v) {
        mMainView = (MainView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void onCreate() {
    }
}