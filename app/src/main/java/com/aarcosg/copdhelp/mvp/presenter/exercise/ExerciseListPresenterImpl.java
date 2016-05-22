package com.aarcosg.copdhelp.mvp.presenter.exercise;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseListView;

import javax.inject.Inject;

import io.realm.Sort;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class ExerciseListPresenterImpl implements ExerciseListPresenter {

    private ExerciseListView mExerciseListView;
    private final ExerciseInteractor mExerciseInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public ExerciseListPresenterImpl(ExerciseInteractor exerciseInteractor){
        this.mExerciseInteractor = exerciseInteractor;
    }

    @Override
    public void setView(View v) {
        mExerciseListView = (ExerciseListView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadAllFromRealm() {
        mSubscription = mExerciseInteractor.realmFindAll(
                    null
                    ,RealmTable.Exercise.TIMESTAMP
                    ,Sort.DESCENDING)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    exercises ->
                            mExerciseListView.bindAll(exercises)
                    ,throwable ->
                            mExerciseListView.showLoadAllRealmErrorMessage()
                );
    }

    @Override
    public void removeFromRealm(Long id) {
        mSubscription = mExerciseInteractor.realmRemove(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmExercise ->
                                mExerciseListView.showRemoveRealmSuccessMessage()
                        ,throwable ->
                                mExerciseListView.showRemoveRealmErrorMessage()
                );
    }
}