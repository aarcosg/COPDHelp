package com.aarcosg.copdhelp.mvp.presenter.smoke;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.smoke.SmokeChartView;

import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class SmokeChartPresenterImpl implements SmokeChartPresenter {

    private SmokeChartView mSmokeChartView;
    private final SmokeInteractor mSmokeInteractor;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Inject
    public SmokeChartPresenterImpl(SmokeInteractor smokeInteractor){
        this.mSmokeInteractor = smokeInteractor;
    }

    @Override
    public void setView(View v) {
        mSmokeChartView = (SmokeChartView) v;
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
        mSubscriptions.add(mSmokeInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.Smoke.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.Smoke.WEEK_OF_YEAR
                                ,calendar.get(Calendar.WEEK_OF_YEAR) - 1
                                ,calendar.get(Calendar.WEEK_OF_YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        smokeList ->
                                mSmokeChartView.bindWeekData(smokeList)
                        ,throwable ->
                                mSmokeChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadMonthData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mSmokeInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.Smoke.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.Smoke.MONTH
                                ,calendar.get(Calendar.MONTH) - 1
                                ,calendar.get(Calendar.MONTH)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        smokeList ->
                                mSmokeChartView.bindMonthData(smokeList)
                        ,throwable ->
                                mSmokeChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadYearData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mSmokeInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .between(RealmTable.Smoke.YEAR
                                ,calendar.get(Calendar.YEAR) - 1
                                ,calendar.get(Calendar.YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        smokeList ->
                                mSmokeChartView.bindYearData(smokeList)
                        ,throwable ->
                                mSmokeChartView.showLoadRealmErrorMessage()
                )
        );
    }
}