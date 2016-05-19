package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.BMIModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIListPresenter;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIEditFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                BMIModule.class
        })
public interface BMIComponent extends ActivityComponent {

    void inject(BMIEditFragment bMIEditFragment);
    void inject(BMIDetailsFragment bMIDetailsFragment);

    BMIListPresenter getBMIListPresenter();
    BMIChartPresenter getBMIChartPresenter();
    BMIDetailsPresenter getBMIDetailsPresenter();
    BMIEditPresenter getBMIEditPresenter();

}