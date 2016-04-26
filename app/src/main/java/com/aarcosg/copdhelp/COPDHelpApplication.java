package com.aarcosg.copdhelp;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.aarcosg.copdhelp.di.components.ApplicationComponent;
import com.aarcosg.copdhelp.di.components.DaggerApplicationComponent;
import com.aarcosg.copdhelp.di.modules.ApplicationModule;
import com.aarcosg.copdhelp.di.modules.NetworkModule;

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
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .networkModule(new NetworkModule())
                .build();
    }

    public ApplicationComponent getApplicationComponent() {
        return this.mApplicationComponent;
    }
}
