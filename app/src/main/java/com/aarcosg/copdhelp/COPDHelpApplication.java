package com.aarcosg.copdhelp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.aarcosg.copdhelp.di.components.ApplicationComponent;
import com.aarcosg.copdhelp.di.components.DaggerApplicationComponent;
import com.aarcosg.copdhelp.di.modules.ApplicationModule;
import com.aarcosg.copdhelp.interactor.UserInteractor;

public class COPDHelpApplication extends MultiDexApplication {

    private static final String TAG = COPDHelpApplication.class.getCanonicalName();

    private ApplicationComponent mApplicationComponent;

    public COPDHelpApplication(){
        super();
    }

    public static COPDHelpApplication get(Context context){
        return (COPDHelpApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        createUser();
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.mApplicationComponent;
    }

    private void createUser(){
        UserInteractor userInteractor = getApplicationComponent().getUserInteractor();
        userInteractor.realmCreateIfNotExists(1L);
    }
}
