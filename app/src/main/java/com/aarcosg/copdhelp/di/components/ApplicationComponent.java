package com.aarcosg.copdhelp.di.components;

import android.content.Context;
import android.content.SharedPreferences;

import com.aarcosg.copdhelp.data.api.COPDHelpApi;
import com.aarcosg.copdhelp.di.modules.ApplicationModule;
import com.aarcosg.copdhelp.di.modules.InteractorsModule;
import com.aarcosg.copdhelp.di.modules.NetworkModule;
import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.interactor.MainInteractor;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractor;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.ui.activity.BaseActivity;
import com.aarcosg.copdhelp.utils.RxNetwork;
import com.google.gson.Gson;

import dagger.Component;
import okhttp3.OkHttpClient;

@PerApp
@Component(
        modules = {
                ApplicationModule.class,
                NetworkModule.class,
                InteractorsModule.class
        }
)
public interface ApplicationComponent {

    void inject(BaseActivity baseActivity);

    Context getContext();
    SharedPreferences getSharedPreferences();

    RxNetwork getRxNetwork();
    OkHttpClient getOkHttpClient();
    Gson getGson();
    COPDHelpApi getCOPDHelpApi();

    MainInteractor getMainInteractor();
    MedicalAttentionInteractor getMedicalAttentionInteractor();
    BMIInteractor getBMIInteractor();
    MedicineReminderInteractor getMedicineReminderInteractor();
    SmokeInteractor getSmokeInteractor();

}