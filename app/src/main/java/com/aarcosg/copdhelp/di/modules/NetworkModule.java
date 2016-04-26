package com.aarcosg.copdhelp.di.modules;

import android.content.Context;

import com.aarcosg.copdhelp.BuildConfig;
import com.aarcosg.copdhelp.data.api.COPDHelpApi;
import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.utils.RxNetwork;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    public final static String NAME_RETROFIT_COPDHELP = "NAME_RETROFIT_COPDHELP";
    private final static long SECONDS_TIMEOUT = 20;

    @Provides
    @PerApp
    RxNetwork provideRxNetwork(Context context){
        return new RxNetwork(context);
    }

    @Provides
    @PerApp
    Cache provideOkHttpCache(Context context) {
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        return new Cache(context.getCacheDir(), cacheSize);
    }

    @Provides
    @PerApp
    OkHttpClient provideOkHttpClient(Cache cache) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(SECONDS_TIMEOUT, TimeUnit.SECONDS)
                .cache(cache);
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(logging);
        }
        return builder.build();
    }

    @Provides
    @PerApp
    Gson provideGson() {
        return new Gson();
    }

    @Provides
    @PerApp
    Retrofit.Builder provideRetrofit(Gson gson, OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient);
    }

    @Provides
    @PerApp
    @Named(NAME_RETROFIT_COPDHELP)
    Retrofit provideCOPDHelpRetrofit(Retrofit.Builder builder) {
        return builder
                .baseUrl(COPDHelpApi.SERVICE_ENDPOINT)
                .build();
    }

    @Provides
    @PerApp
    COPDHelpApi provideCOPDHelpApi(
            @Named(NAME_RETROFIT_COPDHELP) Retrofit retrofit) {
        return retrofit.create(COPDHelpApi.class);
    }

}
