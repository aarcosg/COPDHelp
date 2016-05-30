package com.aarcosg.copdhelp.receiver;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.data.realm.entity.Medicine;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.ui.activity.MainActivity;

import io.realm.Realm;

public class MedicineTakenBroadcastReceiver extends WakefulBroadcastReceiver {

    //TODO use an interactor to find and update reminders

    private static final String TAG = MedicineTakenBroadcastReceiver.class.getCanonicalName();

    @Override
    public void onReceive(Context context, Intent intent) {
        MedicineReminder reminder = findMedicineReminder(intent.getLongExtra(RemindersHelper.EXTRA_ID,-1L));
        if(reminder != null){
            saveMedicineTaken(context, reminder);
            openMainActivity(context,reminder.getId());
        }
    }

    private void openMainActivity(Context context, Long reminderId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(RemindersHelper.EXTRA_ID, reminderId);
        intent.putExtra(RemindersHelper.EXTRA_NOTIFICATION_ID, reminderId);
        intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                        | Intent.FLAG_ACTIVITY_CLEAR_TOP
                        | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        context.startActivity(intent);
    }

    private MedicineReminder findMedicineReminder(Long id){
        Realm realm = Realm.getDefaultInstance();
        MedicineReminder reminder = realm.where(MedicineReminder.class).equalTo("id",id).findFirst();
        realm.close();
        return reminder;
    }

    private void saveMedicineTaken(Context context, MedicineReminder medicineReminder){
        Medicine medicine = new Medicine(
                medicineReminder.getMedicine()
                ,medicineReminder.getDose()
                ,medicineReminder.getTimestamp());
        COPDHelpApplication.get(context)
                .getApplicationComponent()
                .getMedicineInteractor()
                .realmCreate(medicine);
    }
}
