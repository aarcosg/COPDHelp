package com.aarcosg.copdhelp.mvp.presenter.exercise;

import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.mvp.view.View;
import com.aarcosg.copdhelp.mvp.view.exercise.ExerciseDetailsView;

import javax.inject.Inject;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.subjects.PublishSubject;
import rx.subscriptions.Subscriptions;

public class ExerciseDetailsPresenterImpl implements ExerciseDetailsPresenter {

    private ExerciseDetailsView mExerciseDetailsView;
    private final ExerciseInteractor mExerciseInteractor;
    private Subscription mSubscription = Subscriptions.empty();
    private PublishSubject<Long> onEditButtonClickSubject = PublishSubject.create();

    @Inject
    public ExerciseDetailsPresenterImpl(ExerciseInteractor exerciseInteractor){
        this.mExerciseInteractor = exerciseInteractor;
    }

    @Override
    public void setView(View v) {
        mExerciseDetailsView = (ExerciseDetailsView) v;
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
                                mExerciseDetailsView.bindRealmObject(realmExercise)
                        ,throwable ->
                                mExerciseDetailsView.showRealmObjectNotFoundError()
                );
    }

    @Override
    public void onEditButtonClick(Long id){
        onEditButtonClickSubject.onNext(id);
    }

    @Override
    public PublishSubject<Long> getOnEditButtonClickSubject() {
        return onEditButtonClickSubject;
    }
}
