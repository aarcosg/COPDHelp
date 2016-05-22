package com.aarcosg.copdhelp.mvp.presenter.medicinereminder;


import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.mvp.presenter.ListPresenter;

public interface MedicineReminderListPresenter extends ListPresenter<MedicineReminder> {

    void addRealmObject(MedicineReminder medicineReminder);

    void editRealmObject(Long id, MedicineReminder medicineReminder);

    void enableMedicineReminder(Long id, boolean enable);
}
