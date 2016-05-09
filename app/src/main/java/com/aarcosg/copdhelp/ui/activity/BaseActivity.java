package com.aarcosg.copdhelp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.di.components.ApplicationComponent;
import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.mikepenz.iconics.context.IconicsContextWrapper;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getApplicationComponent().inject(this);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    protected void addFragment(int containerViewId, Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(containerViewId,fragment).commit();
    }

    protected void replaceFragment(int containerViewId, Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(containerViewId,fragment).commit();
    }

    protected ApplicationComponent getApplicationComponent(){
        return COPDHelpApplication.get(this).getApplicationComponent();
    }

    protected ActivityModule getActivityModule(){
        return new ActivityModule(this);
    }

    @Override
    public void onBackPressed() {
        int count = getSupportFragmentManager().getBackStackEntryCount();
        if (count == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStackImmediate();
        }
    }
}
