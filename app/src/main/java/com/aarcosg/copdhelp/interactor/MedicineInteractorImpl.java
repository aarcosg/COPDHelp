package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.Medicine;
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

public class MedicineInteractorImpl implements MedicineInteractor {

    private static final String TAG = MedicineInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public MedicineInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<RealmResults<Medicine>> realmFindAll(
            @Nullable Func1<RealmQuery<Medicine>, RealmQuery<Medicine>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<Medicine> query = getRealm().where(Medicine.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<Medicine> results;
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
    public Observable<Medicine> realmFindById(Long id) {
        return Observable.just(getRealm().where(Medicine.class)
                .equalTo("id",id).findFirst());
    }

    @RxLogObservable
    @Override
    public Observable<Medicine> realmCreate(Medicine medicine) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicine.getTimestamp());
        getRealm().beginTransaction();
        Medicine realmMedicine = getRealm().createObject(Medicine.class,
                PrimaryKeyFactory.getInstance().nextKey(Medicine.class));
        realmMedicine.setMedicine(medicine.getMedicine());
        realmMedicine.setDose(medicine.getDose());
        realmMedicine.setTimestamp(calendar.getTime());
        realmMedicine.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmMedicine.setMonth(calendar.get(Calendar.MONTH));
        realmMedicine.setYear(calendar.get(Calendar.YEAR));
        realmMedicine.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmMedicine.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmMedicine);
    }

    @RxLogObservable
    @Override
    public Observable<Medicine> realmUpdate(Long id, Medicine medicine) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(medicine.getTimestamp());
        getRealm().beginTransaction();
        Medicine realmMedicine = getRealm().where(Medicine.class)
                .equalTo("id",id).findFirst();
        realmMedicine.setMedicine(medicine.getMedicine());
        realmMedicine.setDose(medicine.getDose());
        realmMedicine.setTimestamp(calendar.getTime());
        realmMedicine.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmMedicine.setMonth(calendar.get(Calendar.MONTH));
        realmMedicine.setYear(calendar.get(Calendar.YEAR));
        realmMedicine.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmMedicine.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmMedicine);
    }

    @RxLogObservable
    @Override
    public Observable<Medicine> realmRemove(Long id) {
        getRealm().beginTransaction();
        Medicine realmMedicine = getRealm().where(Medicine.class)
                .equalTo("id",id).findFirst();
        realmMedicine.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmMedicine);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}