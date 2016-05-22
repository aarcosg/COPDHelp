package com.aarcosg.copdhelp.mvp.presenter.medicinereminder;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.medicinereminder.MedicineReminderListView;

import javax.inject.Inject;

import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class MedicineReminderListPresenterImpl implements MedicineReminderListPresenter {

    private MedicineReminderListView mMedicineReminderListView;
    private final MedicineReminderInteractor mMedicineReminderInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public MedicineReminderListPresenterImpl(MedicineReminderInteractor medicineReminderInteractor){
        this.mMedicineReminderInteractor = medicineReminderInteractor;
    }

    @Override
    public void setView(View v) {
        mMedicineReminderListView = (MedicineReminderListView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadAllFromRealm() {
        mSubscription = mMedicineReminderInteractor.realmFindAll(
                    null
                    ,RealmTable.MedicineReminder.TIMESTAMP
                    ,Sort.DESCENDING)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    MedicineReminders ->
                            mMedicineReminderListView.bindAll(MedicineReminders)
                    ,throwable ->
                            mMedicineReminderListView.showLoadAllRealmErrorMessage()
                );
    }

    @Override
    public void addRealmObject(MedicineReminder medicineReminder) {
        mSubscription = mMedicineReminderInteractor.realmCreate(medicineReminder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmBMI ->
                                mMedicineReminderListView.showSaveSuccessMessage()
                        ,throwable ->
                                mMedicineReminderListView.showSaveErrorMessage()
                );
    }

    @Override
    public void editRealmObject(Long id, MedicineReminder medicineReminder) {
        mSubscription = mMedicineReminderInteractor.realmUpdate(id,medicineReminder)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmBMI ->
                                mMedicineReminderListView.showSaveSuccessMessage()
                        ,throwable ->
                                mMedicineReminderListView.showSaveErrorMessage()
                );
    }

    @Override
    public void removeFromRealm(Long id) {
        mSubscription = mMedicineReminderInteractor.realmRemove(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmMedicineReminder ->
                                mMedicineReminderListView.showRemoveRealmSuccessMessage()
                        ,throwable ->
                                mMedicineReminderListView.showRemoveRealmErrorMessage()
                );
    }

    @Override
    public void enableMedicineReminder(Long id, boolean enable) {
        mSubscription = mMedicineReminderInteractor.realmUpdateMedicineReminderState(id,enable)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmMedicineReminder -> {
                            if(enable){
                                mMedicineReminderListView.disableReminderAlarm(realmMedicineReminder);
                                mMedicineReminderListView.enableReminderAlarm(realmMedicineReminder);
                            }else{
                                mMedicineReminderListView.disableReminderAlarm(realmMedicineReminder);
                            }
                            mMedicineReminderListView.showReminderStateChangedSuccessMessage(enable);
                        }
                        ,throwable ->
                                mMedicineReminderListView.showSaveErrorMessage()
                );
    }
}