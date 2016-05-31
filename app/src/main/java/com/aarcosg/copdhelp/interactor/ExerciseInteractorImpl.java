package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;

import java.util.Calendar;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public class ExerciseInteractorImpl implements ExerciseInteractor {

    private static final String TAG = ExerciseInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public ExerciseInteractorImpl(){}


    @Override
    public Observable<RealmResults<Exercise>> realmFindAll(
            @Nullable Func1<RealmQuery<Exercise>, RealmQuery<Exercise>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<Exercise> query = getRealm().where(Exercise.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<Exercise> results;
        if(sortFieldName != null && sortOrder != null){
            results = query.findAllSorted(sortFieldName,sortOrder);
        }else{
            results = query.findAll();
        }
        return Observable.just(results);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Observable<Exercise> realmFindById(Long id) {
        return Observable.just(getRealm().where(Exercise.class)
                .equalTo("id",id).findFirst());
    }


    @Override
    public Observable<Exercise> realmCreate(Exercise exercise) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(exercise.getTimestamp());
        getRealm().beginTransaction();
        Exercise realmExercise = getRealm().createObject(Exercise.class,
                PrimaryKeyFactory.getInstance().nextKey(Exercise.class));
        realmExercise.setType(exercise.getType());
        realmExercise.setDuration(exercise.getDuration());
        realmExercise.setNote(exercise.getNote());
        realmExercise.setTimestamp(exercise.getTimestamp());
        realmExercise.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmExercise.setMonth(calendar.get(Calendar.MONTH));
        realmExercise.setYear(calendar.get(Calendar.YEAR));
        realmExercise.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmExercise.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmExercise);
    }


    @Override
    public Observable<Exercise> realmUpdate(Long id, Exercise exercise) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(exercise.getTimestamp());
        getRealm().beginTransaction();
        Exercise realmExercise = getRealm().where(Exercise.class)
                .equalTo("id",id).findFirst();
        realmExercise.setType(exercise.getType());
        realmExercise.setDuration(exercise.getDuration());
        realmExercise.setNote(exercise.getNote());
        realmExercise.setTimestamp(exercise.getTimestamp());
        realmExercise.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        realmExercise.setMonth(calendar.get(Calendar.MONTH));
        realmExercise.setYear(calendar.get(Calendar.YEAR));
        realmExercise.setDayOfWeek(calendar.get(Calendar.DAY_OF_WEEK));
        realmExercise.setWeekOfYear(calendar.get(Calendar.WEEK_OF_YEAR));
        getRealm().commitTransaction();
        return Observable.just(realmExercise);
    }


    @Override
    public Observable<Exercise> realmRemove(Long id) {
        getRealm().beginTransaction();
        Exercise realmExercise = getRealm().where(Exercise.class)
                .equalTo("id",id).findFirst();
        realmExercise.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmExercise);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}