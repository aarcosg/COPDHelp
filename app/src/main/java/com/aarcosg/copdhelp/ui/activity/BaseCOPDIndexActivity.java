package com.aarcosg.copdhelp.ui.activity;

import com.aarcosg.copdhelp.COPDHelpApplication;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class BaseCOPDIndexActivity extends BaseAppIntroActivity {

    private Subscription mSubscription = Subscriptions.empty();

    protected void updateCOPDIndex(String realmCOPDIndexTable, int totalPoints){
        COPDHelpApplication.get(this)
                .getApplicationComponent()
                .getUserInteractor()
                .updateCOPDIndexResult(1L, realmCOPDIndexTable, totalPoints)
                .subscribe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }
}
