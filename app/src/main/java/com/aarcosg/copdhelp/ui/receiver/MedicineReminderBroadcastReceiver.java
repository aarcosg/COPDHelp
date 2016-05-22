package com.aarcosg.copdhelp.ui.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.ui.activity.MainActivity;

import java.util.Calendar;

import io.realm.Realm;

public class MedicineReminderBroadcastReceiver extends WakefulBroadcastReceiver {

    //TODO use an interactor to find and update reminders

    private static final String TAG = MedicineReminderBroadcastReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        MedicineReminder reminder = findMedicineReminder(intent.getLongExtra(Reminders.EXTRA_ID,-1L));
        if(reminder != null){
            showNotification(context,reminder);
            updateMedicineReminderTime(reminder);
        }
    }

    private void showNotification(Context context, MedicineReminder reminder) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(Reminders.EXTRA_ID, reminder.getId());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.medicine_reminder_notification,reminder.getMedicine()))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, builder.build());

    }

    private MedicineReminder findMedicineReminder(Long id){
        Realm realm = Realm.getDefaultInstance();
        MedicineReminder reminder = realm.where(MedicineReminder.class).equalTo("id",id).findFirst();
        realm.close();
        return reminder;
    }

    private void updateMedicineReminderTime(MedicineReminder medicineReminder){
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
