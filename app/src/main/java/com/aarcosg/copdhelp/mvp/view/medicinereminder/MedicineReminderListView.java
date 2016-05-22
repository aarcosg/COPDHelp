package com.aarcosg.copdhelp.mvp.view.medicinereminder;

import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.mvp.view.ListView;

public interface MedicineReminderListView extends ListView<MedicineReminder> {

    void showSaveSuccessMessage();

    void showSaveErrorMessage();

    void showReminderStateChangedSuccessMessage(boolean enable);

    void enableReminderAlarm(MedicineReminder medicineReminder);

    void disableReminderAlarm(MedicineReminder medicineReminder);
}
