package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.SmokeModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeListPresenter;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeEditFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                SmokeModule.class
        })
public interface SmokeComponent extends ActivityComponent {

    void inject(SmokeEditFragment smokeEditFragment);
    void inject(SmokeDetailsFragment smokeDetailsFragment);

    SmokeListPresenter getSmokeListPresenter();
    SmokeChartPresenter getSmokeChartPresenter();
    SmokeDetailsPresenter getSmokeDetailsPresenter();
    SmokeEditPresenter getSmokeEditPresenter();

}