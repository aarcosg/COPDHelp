package com.aarcosg.copdhelp.receiver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.ui.activity.MainActivity;

import java.util.Calendar;
import java.util.Random;

import io.realm.Realm;

public class RemindersHelper {

    private static final String TAG = RemindersHelper.class.getCanonicalName();
    public static final String EXTRA_ID = "extra_reminder_id";
    public static final String EXTRA_NOTIFICATION_ID = "extra_reminder_notification_id";
    public static final String ACTION_TAKE_MEDICATION = "com.aarcosg.copdhelp.ACTION_TAKE_MEDICATION";

    private static RemindersHelper sInstance;
    private Context mContext;

    private RemindersHelper(){}

    public static RemindersHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new RemindersHelper();
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

    public void showNotification(Context context, MedicineReminder reminder) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(RemindersHelper.EXTRA_ID, reminder.getId());
        intent.putExtra(RemindersHelper.EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1000, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.notification_small_copd_icon)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.medicine_reminder_notification,reminder.getMedicine()))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false);

        Intent takeMedicationIntent = new Intent(RemindersHelper.ACTION_TAKE_MEDICATION);
        takeMedicationIntent.putExtra(RemindersHelper.EXTRA_ID, reminder.getId());
        takeMedicationIntent.putExtra(RemindersHelper.EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent takeMedicationPI = PendingIntent.getBroadcast(context,1001,takeMedicationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        builder.addAction(R.drawable.ic_done,context.getString(R.string.taken_medication),takeMedicationPI);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,builder.build());
    }

    public MedicineReminder findMedicineReminder(Long id){
        Realm realm = Realm.getDefaultInstance();
        MedicineReminder reminder = realm.where(MedicineReminder.class).equalTo("id",id).findFirst();
        realm.close();
        return reminder;
    }

    public void updateMedicineReminderTime(MedicineReminder medicineReminder){
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicineReminder.getTimestamp());
        calendar.add(Calendar.HOUR_OF_DAY,medicineReminder.getFrequency());
        medicineReminder.setTimestamp(calendar.getTime());
        medicineReminder.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        medicineReminder.setMonth(calendar.get(Calendar.MONTH));
        medicineReminder.setYear(calendar.get(Calendar.YEAR));
        medicineReminder.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        medicineReminder.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        realm.commitTransaction();
        realm.close();

    }

}
