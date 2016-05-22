package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeChartPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeDetailsPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeEditPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class SmokeModule {

    @Provides
    @PerActivity
    public SmokeListPresenter provideSmokeListPresenter(SmokeInteractor smokeInteractor) {
        return new SmokeListPresenterImpl(smokeInteractor);
    }

    @Provides
    @PerActivity
    public SmokeChartPresenter provideSmokeChartPresenter(SmokeInteractor smokeInteractor) {
        return new SmokeChartPresenterImpl(smokeInteractor);
    }

    @Provides
    @PerActivity
    public SmokeDetailsPresenter provideSmokeDetailsPresenter(SmokeInteractor smokeInteractor) {
        return new SmokeDetailsPresenterImpl(smokeInteractor);
    }

    @Provides
    @PerActivity
    public SmokeEditPresenter provideSmokeEditPresenter(SmokeInteractor smokeInteractor) {
        return new SmokeEditPresenterImpl(smokeInteractor);
    }
}