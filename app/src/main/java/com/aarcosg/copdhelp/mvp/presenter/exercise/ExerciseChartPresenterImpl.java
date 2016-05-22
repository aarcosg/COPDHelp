package com.aarcosg.copdhelp.mvp.presenter.exercise;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseChartView;

import java.util.Calendar;

import javax.inject.Inject;

import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.CompositeSubscription;

public class ExerciseChartPresenterImpl implements ExerciseChartPresenter {

    private ExerciseChartView mExerciseChartView;
    private final ExerciseInteractor mExerciseInteractor;
    private CompositeSubscription mSubscriptions = new CompositeSubscription();

    @Inject
    public ExerciseChartPresenterImpl(ExerciseInteractor ExerciseInteractor){
        this.mExerciseInteractor = ExerciseInteractor;
    }

    @Override
    public void setView(View v) {
        mExerciseChartView = (ExerciseChartView) v;
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
        mSubscriptions.add(mExerciseInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.Exercise.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.Exercise.WEEK_OF_YEAR
                                ,calendar.get(Calendar.WEEK_OF_YEAR) - 1
                                ,calendar.get(Calendar.WEEK_OF_YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        exercises ->
                                mExerciseChartView.bindWeekData(exercises)
                        ,throwable ->
                                mExerciseChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadMonthData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mExerciseInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .equalTo(RealmTable.Exercise.YEAR,calendar.get(Calendar.YEAR))
                        .between(RealmTable.Exercise.MONTH
                                ,calendar.get(Calendar.MONTH) - 1
                                ,calendar.get(Calendar.MONTH)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        exercises ->
                                mExerciseChartView.bindMonthData(exercises)
                        ,throwable ->
                                mExerciseChartView.showLoadRealmErrorMessage()
                )
        );
    }

    @Override
    public void loadYearData() {
        Calendar calendar = Calendar.getInstance();
        mSubscriptions.add(mExerciseInteractor.realmFindAll(
                realmQuery -> realmQuery
                        .between(RealmTable.Exercise.YEAR
                                ,calendar.get(Calendar.YEAR) - 1
                                ,calendar.get(Calendar.YEAR)
                        )
                ,null
                ,null)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        exercises ->
                                mExerciseChartView.bindYearData(exercises)
                        ,throwable ->
                                mExerciseChartView.showLoadRealmErrorMessage()
                )
        );
    }
}