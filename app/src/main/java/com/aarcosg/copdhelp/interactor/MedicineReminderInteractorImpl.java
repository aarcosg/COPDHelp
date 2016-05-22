package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.MedicineReminder;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public class MedicineReminderInteractorImpl implements MedicineReminderInteractor {

    private static final String TAG = MedicineReminderInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public MedicineReminderInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<RealmResults<MedicineReminder>> realmFindAll(
            @Nullable Func1<RealmQuery<MedicineReminder>, RealmQuery<MedicineReminder>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<MedicineReminder> query = getRealm().where(MedicineReminder.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<MedicineReminder> results;
        if(sortFieldName != null && sortOrder != null){
            results = query.findAllSorted(sortFieldName,sortOrder);
        }else{
            results = query.findAll();
        }
        return Observable.just(results);
    }

    @RxLogObservable
    @SuppressWarnings("unchecked")
    @Override
    public Observable<MedicineReminder> realmFindById(Long id) {
        return Observable.just(getRealm().where(MedicineReminder.class)
                .equalTo("id",id).findFirst());
    }

    @RxLogObservable
    @Override
    public Observable<MedicineReminder> realmCreate(MedicineReminder medicineReminder) {
        Calendar now = Calendar.getInstance();
        Calendar reminderTime = Calendar.getInstance();
        reminderTime.setTime(medicineReminder.getTimestamp());
        if(reminderTime.before(now)){
            reminderTime.add(Calendar.HOUR_OF_DAY,medicineReminder.getFrequency());
        }
        getRealm().beginTransaction();
        MedicineReminder realmMedicineReminder = getRealm().createObject(MedicineReminder.class,
                PrimaryKeyFactory.getInstance().nextKey(MedicineReminder.class));
        realmMedicineReminder.setEnabled(medicineReminder.isEnabled());
        realmMedicineReminder.setMedicine(medicineReminder.getMedicine());
        realmMedicineReminder.setDose(medicineReminder.getDose());
        realmMedicineReminder.setFrequency(medicineReminder.getFrequency());
        realmMedicineReminder.setTimestamp(reminderTime.getTime());
        realmMedicineReminder.setDay(reminderTime.get(Calendar.DAY_OF_MONTH));
        realmMedicineReminder.setMonth(reminderTime.get(Calendar.MONTH));
        realmMedicineReminder.setYear(reminderTime.get(Calendar.YEAR));
        realmMedicineReminder.setDayOfWeek(reminderTime.get(Calendar.DAY_OF_WEEK));
        realmMedicineReminder.setWeekOfYear(reminderTime.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmMedicineReminder);
    }

    @RxLogObservable
    @Override
    public Observable<MedicineReminder> realmUpdate(Long id, MedicineReminder medicineReminder) {
        Calendar now = Calendar.getInstance();
        Calendar reminderTime = Calendar.getInstance();
        reminderTime.setTime(medicineReminder.getTimestamp());
        if(reminderTime.before(now)){
            reminderTime.add(Calendar.HOUR_OF_DAY,medicineReminder.getFrequency());
        }
        getRealm().beginTransaction();
        MedicineReminder realmMedicineReminder = getRealm().where(MedicineReminder.class)
                .equalTo("id",id).findFirst();
        realmMedicineReminder.setMedicine(medicineReminder.getMedicine());
        realmMedicineReminder.setDose(medicineReminder.getDose());
        realmMedicineReminder.setFrequency(medicineReminder.getFrequency());
        realmMedicineReminder.setTimestamp(reminderTime.getTime());
        realmMedicineReminder.setDay(reminderTime.get(Calendar.DAY_OF_MONTH));
        realmMedicineReminder.setMonth(reminderTime.get(Calendar.MONTH));
        realmMedicineReminder.setYear(reminderTime.get(Calendar.YEAR));
        realmMedicineReminder.setDayOfWeek(reminderTime.get(Calendar.DAY_OF_WEEK));
        realmMedicineReminder.setWeekOfYear(reminderTime.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmMedicineReminder);
    }

    @RxLogObservable
    @Override
    public Observable<MedicineReminder> realmRemove(Long id) {
        getRealm().beginTransaction();
        MedicineReminder realmMedicineReminder = getRealm().where(MedicineReminder.class)
                .equalTo("id",id).findFirst();
        realmMedicineReminder.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmMedicineReminder);
    }

    @RxLogObservable
    @Override
    public Observable<MedicineReminder> realmUpdateMedicineReminderState(Long id, boolean enable) {
        getRealm().beginTransaction();
        MedicineReminder realmMedicineReminder = getRealm().where(MedicineReminder.class)
                .equalTo("id",id).findFirst();
        realmMedicineReminder.setEnabled(enable);
        getRealm().commitTransaction();
        return Observable.just(realmMedicineReminder);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}