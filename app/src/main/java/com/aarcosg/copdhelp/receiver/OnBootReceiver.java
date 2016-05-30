package com.aarcosg.copdhelp.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.service.COPDHelpService;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmResults;

public class OnBootReceiver extends WakefulBroadcastReceiver {

    //TODO use an interactor to find reminders

    private static final String TAG = OnBootReceiver.class.getCanonicalName();

    @Inject
    COPDHelpService mCOPDHelpService;


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            this.initializeInjector(context);
            mCOPDHelpService.start();
            setupReminderAlarms(context);
        }
    }

    private void setupReminderAlarms(Context context){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MedicineReminder> results = realm.where(MedicineReminder.class)
                .equalTo(RealmTable.MedicineReminder.ENABLED,true)
                .findAll();
        for (MedicineReminder reminder : results) {
            RemindersHelper.getInstance(context).setupReminder(reminder);
        }
        realm.close();
    }

    private void initializeInjector(Context context) {
        COPDHelpApplication.get(context).getApplicationComponent().inject(this);
    }
}