package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.ExerciseModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.exercise.ExerciseListPresenter;
import com.aarcosg.copdhelp.ui.fragment.exercise.ExerciseDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.exercise.ExerciseEditFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                ExerciseModule.class
        })
public interface ExerciseComponent extends ActivityComponent {

    void inject(ExerciseEditFragment exerciseEditFragment);
    void inject(ExerciseDetailsFragment exerciseDetailsFragment);

    ExerciseListPresenter getExerciseListPresenter();
    ExerciseChartPresenter getExerciseChartPresenter();
    ExerciseDetailsPresenter getExerciseDetailsPresenter();
    ExerciseEditPresenter getExerciseEditPresenter();

}