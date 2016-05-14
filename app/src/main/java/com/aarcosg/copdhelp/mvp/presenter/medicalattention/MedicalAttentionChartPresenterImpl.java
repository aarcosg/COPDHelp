package com.aarcosg.copdhelp.mvp.presenter.medicalattention;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicalattention.MedicalAttentionChartView;

import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class MedicalAttentionChartPresenterImpl implements MedicalAttentionChartPresenter {

    private MedicalAttentionChartView mMedicalAttentionChartView;
    private final MedicalAttentionInteractor mMedicalAttentionInteractor;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

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
        if(!mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void loadWeekMedicalAttentions() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mMedicalAttentionInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.MedicalAttention.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.MedicalAttention.WEEK_OF_YEAR
                                ,calendar.get(Calendar.WEEK_OF_YEAR) - 1
                                ,calendar.get(Calendar.WEEK_OF_YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        medicalAttentions ->
                                mMedicalAttentionChartView.bindWeekMedicalAttentions(medicalAttentions)
                        ,throwable ->
                                mMedicalAttentionChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadMonthMedicalAttentions() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mMedicalAttentionInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.MedicalAttention.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.MedicalAttention.MONTH
                                ,calendar.get(Calendar.MONTH) - 1
                                ,calendar.get(Calendar.MONTH)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        medicalAttentions ->
                                mMedicalAttentionChartView.bindMonthMedicalAttentions(medicalAttentions)
                        ,throwable ->
                                mMedicalAttentionChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadYearMedicalAttentions() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mMedicalAttentionInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .between(RealmTable.MedicalAttention.YEAR
                                ,calendar.get(Calendar.YEAR) - 1
                                ,calendar.get(Calendar.YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        medicalAttentions ->
                                mMedicalAttentionChartView.bindYearMedicalAttentions(medicalAttentions)
                        ,throwable ->
                                mMedicalAttentionChartView.showLoadRealmErrorMessage()
                )
        );
    }
}