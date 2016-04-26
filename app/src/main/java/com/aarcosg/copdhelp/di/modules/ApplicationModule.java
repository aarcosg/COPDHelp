package com.aarcosg.copdhelp.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aarcosg.copdhelp.BuildConfig;
import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.squareup.leakcanary.LeakCanary;

import dagger.Module;
import dagger.Provides;


@Module
public class ApplicationModule {

    private final COPDHelpApplication mApplication;

    public ApplicationModule(COPDHelpApplication application) {
        this.mApplication = application;

        if(BuildConfig.DEBUG){
            LeakCanary.install(this.mApplication);
        }else{

        }

    }

    @Provides
    @PerApp
    public Context provideApplicationContext() {
        return this.mApplication;
    }

    @Provides
    @PerApp
    public SharedPreferences provideDefaultSharedPreferences() {
        return PreferenceManager
                .getDefaultSharedPreferences(this.mApplication);
    }
}
