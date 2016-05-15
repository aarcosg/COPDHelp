package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class BMIModule {

    @Provides
    @PerActivity
    public BMIListPresenter provideBMIListPresenter(BMIInteractor BMIInteractor) {
        return new BMIListPresenterImpl(BMIInteractor);
    }

    /*@Provides
    @PerActivity
    public BMIChartPresenter provideBMIChartPresenter(BMIInteractor BMIInteractor) {
        return new BMIChartPresenterImpl(BMIInteractor);
    }

    @Provides
    @PerActivity
    public BMIDetailsPresenter provideBMIDetailsPresenter(BMIInteractor BMIInteractor) {
        return new BMIDetailsPresenterImpl(BMIInteractor);
    }

    @Provides
    @PerActivity
    public BMIEditPresenter provideBMIEditPresenter(BMIInteractor BMIInteractor) {
        return new BMIEditPresenterImpl(BMIInteractor);
    }*/
}