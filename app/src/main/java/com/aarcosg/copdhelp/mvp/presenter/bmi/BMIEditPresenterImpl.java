package com.aarcosg.copdhelp.mvp.presenter.bmi;

import android.text.TextUtils;

import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.bmi.BMIEditView;
import com.aarcosg.copdhelp.utils.Utils;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class BMIEditPresenterImpl implements BMIEditPresenter {

    private BMIEditView mBMIEditView;
    private final BMIInteractor mBMIInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public BMIEditPresenterImpl(BMIInteractor BMIInteractor){
        this.mBMIInteractor = BMIInteractor;
    }

    @Override
    public void setView(View v) {
        mBMIEditView = (BMIEditView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadRealmObject(Long id) {
        mSubscription = mBMIInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        bmi ->
                                mBMIEditView.bindRealmObject(bmi)
                        ,throwable ->
                                mBMIEditView.showRealmObjectNotFoundError()
                );
    }

    @Override
    public void addRealmObject(BMI BMI) {
        mSubscription = mBMIInteractor.realmCreate(BMI)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmBMI ->
                                mBMIEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mBMIEditView.showSaveErrorMessage()
                );
    }

    @Override
    public void editRealmObject(Long id, BMI BMI) {
        mSubscription = mBMIInteractor.realmUpdate(id,BMI)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmBMI ->
                                mBMIEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mBMIEditView.showSaveErrorMessage()
                );
    }

    @Override
    public void onHeightOrWeightChanged(String height, String weight) {
        if(!TextUtils.isEmpty(height) && TextUtils.isDigitsOnly(height)
                && !TextUtils.isEmpty(weight)){
            double bmiResult = Utils.calculateBMI(Integer.valueOf(height), Double.valueOf(weight));
            int bmiStateArrayIndex = Utils.getBMIStateArrayIndex(bmiResult);
            mBMIEditView.bindBMICalcResult(bmiResult,bmiStateArrayIndex);

        }
    }
}
