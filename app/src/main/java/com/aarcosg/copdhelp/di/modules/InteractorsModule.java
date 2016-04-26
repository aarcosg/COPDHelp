package com.aarcosg.copdhelp.di.modules;

import android.content.SharedPreferences;

import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.interactor.MainInteractor;
import com.aarcosg.copdhelp.interactor.MainInteractorImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class InteractorsModule {

    @Provides
    @PerApp
    public MainInteractor provideMainInteractor(SharedPreferences preferences) {
        return new MainInteractorImpl(preferences);
    }
}
