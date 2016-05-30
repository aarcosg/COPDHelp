package com.aarcosg.copdhelp.mvp.presenter;

import com.aarcosg.copdhelp.interactor.COPDHelpServiceInteractor;
import com.aarcosg.copdhelp.mvp.view.COPDHelpServiceView;
import com.aarcosg.copdhelp.mvp.view.View;

import javax.inject.Inject;

import rx.subscriptions.CompositeSubscription;

public class COPDHelpServicePresenterImpl implements COPDHelpServicePresenter {

    private COPDHelpServiceView mCOPDHelpServiceView;
    private final COPDHelpServiceInteractor mCOPDHelpServiceInteractor;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Inject
    public COPDHelpServicePresenterImpl(COPDHelpServiceInteractor copdHelpServiceInteractor){
        this.mCOPDHelpServiceInteractor = copdHelpServiceInteractor;
    }

    @Override
    public void setView(View v) {
        mCOPDHelpServiceView = (COPDHelpServiceView) v;
    }

    @Override
    public void onPause() {
        if(!this.mSubscriptions.isUnsubscribed()){
            this.mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void onDestroy() {
        onPause();
    }

    /*@Override
    public void queryLocations() {
        mSubscriptions.add(this.COPDHelpServiceInteractor.queryLocationsToGoogleFit().subscribe(
            locations ->  {
                if(!locations.isEmpty()){
                    this.COPDHelpServiceInteractor.uploadLocations(locations);
                }
            }
        ));
    }

    @Override
    public void queryActivities() {
        mSubscriptions.add(this.COPDHelpServiceInteractor.queryActivitiesToGoogleFit().subscribe(
            activities -> {
                if(!activities.isEmpty()){
                    this.COPDHelpServiceInteractor.uploadActivities(activities);
                }
            }
        ));
    }*/
}