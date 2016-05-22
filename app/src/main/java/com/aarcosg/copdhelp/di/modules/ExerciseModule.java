package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseChartPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseDetailsPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseEditPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class ExerciseModule {

    @Provides
    @PerActivity
    public ExerciseListPresenter provideExerciseListPresenter(ExerciseInteractor exerciseInteractor) {
        return new ExerciseListPresenterImpl(exerciseInteractor);
    }

    @Provides
    @PerActivity
    public ExerciseChartPresenter provideExerciseChartPresenter(ExerciseInteractor exerciseInteractor) {
        return new ExerciseChartPresenterImpl(exerciseInteractor);
    }

    @Provides
    @PerActivity
    public ExerciseDetailsPresenter provideExerciseDetailsPresenter(ExerciseInteractor exerciseInteractor) {
        return new ExerciseDetailsPresenterImpl(exerciseInteractor);
    }

    @Provides
    @PerActivity
    public ExerciseEditPresenter provideExerciseEditPresenter(ExerciseInteractor exerciseInteractor) {
        return new ExerciseEditPresenterImpl(exerciseInteractor);
    }
}