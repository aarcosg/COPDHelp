package com.aarcosg.copdhelp.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;

public class MedicineReminderBroadcastReceiver extends WakefulBroadcastReceiver {

    //TODO use an interactor to find and update reminders

    private static final String TAG = MedicineReminderBroadcastReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
       RemindersHelper remindersHelper =  RemindersHelper.getInstance(context);
        MedicineReminder reminder = remindersHelper.findMedicineReminder(intent.getLongExtra(RemindersHelper.EXTRA_ID,-1L));
        if(reminder != null){
            remindersHelper.showNotification(context,reminder);
            remindersHelper.updateMedicineReminderTime(reminder);
        }
    }
}
