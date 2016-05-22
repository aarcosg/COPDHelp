package com.aarcosg.copdhelp.ui.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;

import io.realm.Realm;
import io.realm.RealmResults;

public class OnBootReceiver extends WakefulBroadcastReceiver {

    //TODO use an interactor to find reminders

    private static final String TAG = OnBootReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
            setupReminderAlarms(context);
        }
    }

    private void setupReminderAlarms(Context context){
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MedicineReminder> results = realm.where(MedicineReminder.class)
                .equalTo(RealmTable.MedicineReminder.ENABLED,true)
                .findAll();
        for (MedicineReminder reminder : results) {
            Reminders.getInstance(context).setupReminder(reminder);
        }
        realm.close();
    }
}