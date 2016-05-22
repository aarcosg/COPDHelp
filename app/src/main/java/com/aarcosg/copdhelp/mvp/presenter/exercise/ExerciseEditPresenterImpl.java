package com.aarcosg.copdhelp.mvp.presenter.exercise;

import com.aarcosg.copdhelp.data.realm.entity.Exercise;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseEditView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subscriptions.Subscriptions;

public class ExerciseEditPresenterImpl implements ExerciseEditPresenter {

    private ExerciseEditView mExerciseEditView;
    private final ExerciseInteractor mExerciseInteractor;
    private Subscription mSubscription = Subscriptions.empty();

    @Inject
    public ExerciseEditPresenterImpl(ExerciseInteractor exerciseInteractor){
        this.mExerciseInteractor = exerciseInteractor;
    }

    @Override
    public void setView(View v) {
        mExerciseEditView = (ExerciseEditView) v;
    }

    @Override
    public void onPause() {
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public void loadRealmObject(Long id) {
        mSubscription = mExerciseInteractor.realmFindById(id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmExercise ->
                                mExerciseEditView.bindRealmObject(realmExercise)
                        ,throwable ->
                                mExerciseEditView.showRealmObjectNotFoundError()
                );
    }

    @Override
    public void addRealmObject(Exercise Exercise) {
        mSubscription = mExerciseInteractor.realmCreate(Exercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmExercise ->
                                mExerciseEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mExerciseEditView.showSaveErrorMessage()
                );
    }

    @Override
    public void editRealmObject(Long id, Exercise Exercise) {
        mSubscription = mExerciseInteractor.realmUpdate(id,Exercise)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        realmExercise ->
                                mExerciseEditView.showSaveSuccessMessage()
                        ,throwable ->
                                mExerciseEditView.showSaveErrorMessage()
                );
    }
}
