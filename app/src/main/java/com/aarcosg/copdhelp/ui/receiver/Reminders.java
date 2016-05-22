package com.aarcosg.copdhelp.ui.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;

import java.util.Calendar;

public class Reminders {

    private static final String TAG = Reminders.class.getCanonicalName();
    public static final String EXTRA_ID = "extra_reminder_id";

    private static Reminders sInstance;
    private Context mContext;

    private Reminders(){}

    public static Reminders getInstance(Context context){
        if(sInstance == null){
            sInstance = new Reminders();
            sInstance.mContext = context;
        }
        return sInstance;
    }

    public void setupReminder(MedicineReminder reminder){
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(reminder.getTimestamp().getTime());
        long frequency = reminder.getFrequency() * 60 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), frequency, getPendingIntent(reminder));
    }

    public void cancelReminder(MedicineReminder reminder){
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(reminder));
    }

    private PendingIntent getPendingIntent(MedicineReminder reminder){
        Intent intent = new Intent(mContext, MedicineReminderBroadcastReceiver.class);
        intent.putExtra(EXTRA_ID,reminder.getId());
        return PendingIntent.getBroadcast(mContext,reminder.getId().intValue(),intent,0);
    }

}
