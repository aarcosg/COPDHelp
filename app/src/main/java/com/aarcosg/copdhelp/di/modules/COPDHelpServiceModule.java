package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.interactor.COPDHelpServiceInteractor;
import com.aarcosg.copdhelp.mvp.presenter.COPDHelpServicePresenter;
import com.aarcosg.copdhelp.mvp.presenter.COPDHelpServicePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class COPDHelpServiceModule {

    @Provides
    @PerApp
    public COPDHelpServicePresenter provideCOPDHelpServicePresenter(COPDHelpServiceInteractor syncServiceInteractor) {
        return new COPDHelpServicePresenterImpl(syncServiceInteractor);
    }
}