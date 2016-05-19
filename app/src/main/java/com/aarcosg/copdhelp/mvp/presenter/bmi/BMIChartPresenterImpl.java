package com.aarcosg.copdhelp.mvp.presenter.bmi;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIChartView;

import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class BMIChartPresenterImpl implements BMIChartPresenter {

    private BMIChartView mBMIChartView;
    private final BMIInteractor mBMIInteractor;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Inject
    public BMIChartPresenterImpl(BMIInteractor BMIInteractor){
        this.mBMIInteractor = BMIInteractor;
    }

    @Override
    public void setView(View v) {
        mBMIChartView = (BMIChartView) v;
    }

    @Override
    public void onCreateView() {
        this.mSubscriptions = new CompositeSubscription();
    }

    @Override
    public void onPause() {
        if(!mSubscriptions.isUnsubscribed()){
            mSubscriptions.unsubscribe();
        }
    }

    @Override
    public void loadWeekData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mBMIInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.BMI.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.BMI.WEEK_OF_YEAR
                                ,calendar.get(Calendar.WEEK_OF_YEAR) - 1
                                ,calendar.get(Calendar.WEEK_OF_YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        BMIs ->
                                mBMIChartView.bindWeekData(BMIs)
                        ,throwable ->
                                mBMIChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadMonthData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mBMIInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.BMI.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.BMI.MONTH
                                ,calendar.get(Calendar.MONTH) - 1
                                ,calendar.get(Calendar.MONTH)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        BMIs ->
                                mBMIChartView.bindMonthData(BMIs)
                        ,throwable ->
                                mBMIChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadYearData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mBMIInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .between(RealmTable.BMI.YEAR
                                ,calendar.get(Calendar.YEAR) - 1
                                ,calendar.get(Calendar.YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        BMIs ->
                                mBMIChartView.bindYearData(BMIs)
                        ,throwable ->
                                mBMIChartView.showLoadRealmErrorMessage()
                )
        );
    }
}