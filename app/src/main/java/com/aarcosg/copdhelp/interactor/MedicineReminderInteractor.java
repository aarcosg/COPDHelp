package com.aarcosg.copdhelp.interactor;

import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;

import rx.Observable;

public interface MedicineReminderInteractor extends RealmInteractor<MedicineReminder> {

    Observable<MedicineReminder> realmUpdateMedicineReminderState(Long id, boolean enable);
}
