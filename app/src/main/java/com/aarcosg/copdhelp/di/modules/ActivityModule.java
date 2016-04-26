package com.aarcosg.copdhelp.di.modules;

import android.app.Activity;

import com.aarcosg.copdhelp.di.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity mActivity;

    public ActivityModule(Activity activity) {
        this.mActivity = activity;
    }

    @Provides
    @PerActivity
    Activity provideActivity() {
        return this.mActivity;
    }
}