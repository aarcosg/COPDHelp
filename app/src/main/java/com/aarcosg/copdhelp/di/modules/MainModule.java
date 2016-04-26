package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.interactor.MainInteractor;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    @Provides
    @PerActivity
    public MainPresenter provideMainPresenter(MainInteractor mainInteractor) {
        return new MainPresenterImpl(mainInteractor);
    }
}