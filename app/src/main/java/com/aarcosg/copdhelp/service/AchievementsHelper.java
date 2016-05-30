package com.aarcosg.copdhelp.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.Achievement;
import com.aarcosg.copdhelp.interactor.AchievementInteractor;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class AchievementsHelper {

    //TODO use dagger and interactors

    private static final String TAG = AchievementsHelper.class.getCanonicalName();
    public static final String EXTRA_ID = "extra_achievement_id";
    public static final String EXTRA_NOTIFICATION_ID = "extra_achievement_notification_id";

    private static AchievementsHelper sInstance;
    private Context mContext;

    private AchievementsHelper(){}

    public static AchievementsHelper getInstance(Context context){
        if(sInstance == null){
            sInstance = new AchievementsHelper();
            sInstance.mContext = context;
        }
        return sInstance;
    }

    public void checkAchievements(){
        checkSmokingAchievements();
        checkExerciseAchievements();
    }

    private void checkSmokingAchievements(){
        SmokeInteractor smokeInteractor = COPDHelpApplication.get(mContext).getApplicationComponent().getSmokeInteractor();
        AchievementInteractor achievementInteractor = COPDHelpApplication.get(mContext).getApplicationComponent().getAchievementInteractor();

        Calendar nowCalendar = Calendar.getInstance();
        Date firstInstallDate = new Date(Utils.getAppFirstInstallTime(mContext));

        // Check no smoking 1 week achievements
        Calendar auxCalendar = Calendar.getInstance();
        auxCalendar.add(Calendar.WEEK_OF_YEAR, -1);
        if(firstInstallDate.before(auxCalendar.getTime())){
            smokeInteractor.realmFindAll(
                    query -> query.between(RealmTable.Smoke.WEEK_OF_YEAR
                            ,nowCalendar.get(Calendar.WEEK_OF_YEAR) - 1
                            ,nowCalendar.get(Calendar.WEEK_OF_YEAR)
                    )
                    ,null
                    ,null
            ).subscribe(smokes -> {
                if(smokes.isEmpty()){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_no_smoking_1_week)
                            ,true
                            ,nowCalendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
            });
        }

        // Check no smoking 1 month achievements
        auxCalendar = Calendar.getInstance();
        auxCalendar.add(Calendar.MONTH, -1);
        if(firstInstallDate.before(auxCalendar.getTime())){
            smokeInteractor.realmFindAll(
                    query -> query.between(RealmTable.Smoke.MONTH
                            ,nowCalendar.get(Calendar.MONTH) - 1
                            ,nowCalendar.get(Calendar.MONTH)
                    )
                    ,null
                    ,null
            ).subscribe(smokes -> {
                if(smokes.isEmpty()){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_no_smoking_1_month)
                            ,true
                            ,nowCalendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
            });
        }

        // Check no smoking 3 months achievements
        auxCalendar = Calendar.getInstance();
        auxCalendar.add(Calendar.MONTH, -3);
        if(firstInstallDate.before(auxCalendar.getTime())){
            smokeInteractor.realmFindAll(
                    query -> query.between(RealmTable.Smoke.MONTH
                            ,nowCalendar.get(Calendar.MONTH) - 3
                            ,nowCalendar.get(Calendar.MONTH)
                    )
                    ,null
                    ,null
            ).subscribe(smokes -> {
                if(smokes.isEmpty()){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_no_smoking_3_month)
                            ,true
                            ,nowCalendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
            });
        }

        // Check no smoking 1 year achievements
        auxCalendar = Calendar.getInstance();
        auxCalendar.add(Calendar.YEAR, -1);
        if(firstInstallDate.before(auxCalendar.getTime())){
            smokeInteractor.realmFindAll(
                    query -> query.between(RealmTable.Smoke.YEAR
                            ,nowCalendar.get(Calendar.YEAR) - 1
                            ,nowCalendar.get(Calendar.YEAR)
                    )
                    ,null
                    ,null
            ).subscribe(smokes -> {
                if(smokes.isEmpty()){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_no_smoking_1_year)
                            ,true
                            ,nowCalendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
            });
        }
    }

    private void checkExerciseAchievements(){
        ExerciseInteractor exerciseInteractor = COPDHelpApplication.get(mContext).getApplicationComponent().getExerciseInteractor();
        AchievementInteractor achievementInteractor = COPDHelpApplication.get(mContext).getApplicationComponent().getAchievementInteractor();

        Calendar calendar = Calendar.getInstance();
        exerciseInteractor.realmFindAll(
                null
                ,null
                ,null
        ).subscribe(exercises -> {
            if(!exercises.isEmpty()){
                double totalHours = exercises.where().sum(RealmTable.Exercise.DURATION).doubleValue() / 60;
                // Check exercise 5 hours achievement
                if(totalHours >= 5){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_exercise_5_hours)
                            ,true
                            ,calendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
                // Check exercise 24 hours achievement
                if(totalHours >= 24){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_exercise_24_hours)
                            ,true
                            ,calendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
                // Check exercise 200 hours achievement
                if(totalHours >= 200){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_exercise_200_hours)
                            ,true
                            ,calendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
                // Check exercise 1000 hours achievement
                if(totalHours >= 1000){
                    Achievement achievement = new Achievement(
                            (long)mContext.getResources().getInteger(R.integer.medal_id_exercise_1000_hours)
                            ,true
                            ,calendar.getTime()
                    );
                    achievementInteractor.realmCreateIfNotExists(achievement)
                            .subscribe(realmAchievement -> {
                                if(realmAchievement != null){
                                    showNotification(mContext, realmAchievement);
                                }
                            });
                }
            }
        });
    }

    private void showNotification(Context context, Achievement achievement) {
        int notificationId = new Random().nextInt();
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_ID, achievement.getId());
        intent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.app_name))
                .setContentText(context.getString(R.string.gratz_achievement_unlocked))
                .setContentIntent(pendingIntent)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(false);
        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }

}
