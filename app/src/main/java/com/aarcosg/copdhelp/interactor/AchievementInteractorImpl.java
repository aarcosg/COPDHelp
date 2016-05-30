package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.Achievement;
import com.fernandocejas.frodo.annotation.RxLogObservable;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public class AchievementInteractorImpl implements AchievementInteractor {

    private static final String TAG = AchievementInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public AchievementInteractorImpl(){}

    @RxLogObservable
    @Override
    public Observable<RealmResults<Achievement>> realmFindAll(
            @Nullable Func1<RealmQuery<Achievement>, RealmQuery<Achievement>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<Achievement> query = getRealm().where(Achievement.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<Achievement> results;
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
    public Observable<Achievement> realmFindById(Long id) {
        return Observable.just(getRealm().where(Achievement.class)
                .equalTo("id",id).findFirst());
    }

    @RxLogObservable
    @Override
    public Observable<Achievement> realmCreate(Achievement achievement) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(achievement.getTimestamp());
        getRealm().beginTransaction();
        Achievement realmAchievement = getRealm().createObject(Achievement.class, achievement.getId());
        realmAchievement.setCompleted(achievement.isCompleted());
        realmAchievement.setTimestamp(achievement.getTimestamp());
        realmAchievement.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmAchievement.setMonth(calendar.get(Calendar.MONTH));
        realmAchievement.setYear(calendar.get(Calendar.YEAR));
        realmAchievement.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmAchievement.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmAchievement);
    }

    @RxLogObservable
    @Override
    public Observable<Achievement> realmUpdate(Long id, Achievement achievement) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(achievement.getTimestamp());
        getRealm().beginTransaction();
        Achievement realmAchievement = getRealm().where(Achievement.class)
                .equalTo("id",id).findFirst();
        realmAchievement.setCompleted(achievement.isCompleted());
        realmAchievement.setTimestamp(achievement.getTimestamp());
        realmAchievement.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmAchievement.setMonth(calendar.get(Calendar.MONTH));
        realmAchievement.setYear(calendar.get(Calendar.YEAR));
        realmAchievement.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmAchievement.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmAchievement);
    }

    @RxLogObservable
    @Override
    public Observable<Achievement> realmRemove(Long id) {
        getRealm().beginTransaction();
        Achievement realmAchievement = getRealm().where(Achievement.class)
                .equalTo("id",id).findFirst();
        realmAchievement.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmAchievement);
    }

    @RxLogObservable
    @Override
    public Observable<Achievement> realmCreateIfNotExists(Achievement achievement) {
        if(getRealm().where(Achievement.class).equalTo("id",achievement.getId()).findFirst() == null){
            return realmCreate(achievement);
        }else{
            return Observable.just(null);
        }
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}