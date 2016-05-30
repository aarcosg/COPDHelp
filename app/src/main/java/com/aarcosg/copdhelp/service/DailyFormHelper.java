package com.aarcosg.copdhelp.service;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.aarcosg.copdhelp.Constants;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.utils.Utils;

import java.util.Random;

public class DailyFormHelper {

    public static final String EXTRA_ID = "extra_daily_form_id";
    public static final String EXTRA_NOTIFICATION_ID = "extra_daily_form_notification_id";

    private static DailyFormHelper sInstance;
    private Context mContext;

    private DailyFormHelper(){}

    public static DailyFormHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new DailyFormHelper();
            sInstance.mContext = context;
        }
        return sInstance;
    }

    public void sendNotification(){
        if(Utils.getSharedPreferences(mContext).getBoolean(Constants.PROPERTY_GET_DAILY_NOTIFICATIONS,false)){
            showNotification();
        }
    }

    private void showNotification() {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(mContext, MainActivity.class);
        intent.putExtra(EXTRA_ID, notificationId);
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext)
                .setSmallIcon(R.drawable.notification_small_copd_icon)
                .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher))
                .setContentTitle(mContext.getString(R.string.app_name))
                .setContentText(mContext.getString(R.string.reminder_daily_form_notification))
                .setSubText(mContext.getString(R.string.reminder_daily_form_notification_subtext))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        NotificationManager notificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

}
