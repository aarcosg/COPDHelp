package com.aarcosg.copdhelp.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.mvp.presenter.COPDHelpServicePresenter;
import com.aarcosg.copdhelp.mvp.view.COPDHelpServiceView;

import java.util.Calendar;

import javax.inject.Inject;

public class COPDHelpService extends Service implements COPDHelpServiceView {

    private static final String TAG = COPDHelpService.class.getCanonicalName();

    @Inject
    Context mContext;
    @Inject
    COPDHelpServicePresenter mCOPDHelpServicePresenter;
    private PendingIntent mCOPDHelpPendingIntent;

    public COPDHelpService(){}

    @Inject
    public COPDHelpService(Context context, COPDHelpServicePresenter copdHelpServicePresenter){
        this.mContext = context;
        this.mCOPDHelpServicePresenter = copdHelpServicePresenter;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.initializeInjector();
        //GoogleFitHelper.initFitApi(this.getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.mCOPDHelpServicePresenter.onDestroy();
    }

    private void initializeInjector() {
        COPDHelpApplication.get(this.getApplicationContext()).getApplicationComponent().inject(this);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG,"@onStartCommand");
        if(intent != null){
            /*this.mCOPDHelpServicePresenter.queryLocations();
            this.mCOPDHelpServicePresenter.queryActivities();*/
            AchievementsHelper.getInstance(mContext).checkAchievements();
        }
        return START_STICKY;
    }

    public void start(){
        Log.i(TAG,"@start");
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.HOUR,19);
        startTime.set(Calendar.MINUTE,0);
        startTime.set(Calendar.SECOND,0);
        startTime.add(Calendar.DAY_OF_YEAR, 1); // First time
        long frequency = 24 * 60 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, startTime.getTimeInMillis(), frequency, getCOPDHelpPendingIntent());
    }

    public void stop(){
        Log.i(TAG,"@stop");
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getCOPDHelpPendingIntent());
    }

    public boolean isRunning() {
        Log.i(TAG,"@isRunning");
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (COPDHelpService.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private PendingIntent getCOPDHelpPendingIntent(){
        if(mCOPDHelpPendingIntent == null){
            Intent intent = new Intent(mContext, COPDHelpService.class);
            mCOPDHelpPendingIntent = PendingIntent.getService(
                    mContext, 0,
                    intent, 0);
        }
        return mCOPDHelpPendingIntent;
    }
    
}
