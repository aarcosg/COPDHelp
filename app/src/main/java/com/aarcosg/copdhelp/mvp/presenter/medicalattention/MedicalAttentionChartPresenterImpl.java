package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionChartView;

import java.util.Calendar;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionChartPresenterImpl implements MedicalAttentionChartPresenter {

    private MedicalAttentionChartView mMedicalAttentionChartView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MedicalAttentionChartPresenterImpl(MedicalAttentionInteractor medicalAttentionInteractor){
        this.mMedicalAttentionInteractor = medicalAttentionInteractor;
    }

    @Override
    public void setView(View v) {
        mMedicalAttentionChartView = (MedicalAttentionChartView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadWeekMedicalAttentions() {
        mSubscription = mMedicalAttentionInteractor.realmFindAll(
                realmQuery ->
                        realmQuery.equalTo(
                                RealmTable.MedicalAttention.WEEK_OF_YEAR
                                ,Calendar.getInstance().get(Calendar.WEEK_OF_YEAR))
                                ,null
                                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        medicalAttentions ->
                                mMedicalAttentionChartView.bindWeekMedicalAttentions(medicalAttentions)
                        ,throwable ->
                                mMedicalAttentionChartView.showLoadWeekRealmErrorMessage()
                );
    }
}