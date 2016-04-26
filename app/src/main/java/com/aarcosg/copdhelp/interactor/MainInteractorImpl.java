package com.aarcosg.copdhelp.interactor;

import android.content.SharedPreferences;

import javax.inject.Inject;

public class MainInteractorImpl implements MainInteractor {

    private static final String TAG = MainInteractorImpl.class.getCanonicalName();

    private final SharedPreferences mPrefs;

    @Inject
    public MainInteractorImpl(SharedPreferences prefs){
        this.mPrefs = prefs;
    }

}