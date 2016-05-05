package com.aarcosg.copdhelp.di.modules;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.aarcosg.copdhelp.BuildConfig;
import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.realm.RealmConfiguration;


@Module
public class ApplicationModule {

    private final COPDHelpApplication mApplication;

    public ApplicationModule(COPDHelpApplication application) {
        this.mApplication = application;
        if(BuildConfig.DEBUG){
            LeakCanary.install(mApplication);
            Stetho.initialize(
                    Stetho.newInitializerBuilder(mApplication)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(mApplication))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(mApplication).build())
                            .build());
        }else{

        }
        setupRealm();

    }

    @Provides
    @PerApp
    public Context provideApplicationContext() {
        return mApplication;
    }

    @Provides
    @PerApp
    public SharedPreferences provideDefaultSharedPreferences() {
        return PreferenceManager
                .getDefaultSharedPreferences(mApplication);
    }

    private void setupRealm(){
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(mApplication).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        Realm realm = Realm.getDefaultInstance();
        PrimaryKeyFactory.getInstance().initialize(realm);
        realm.close();
    }
}
