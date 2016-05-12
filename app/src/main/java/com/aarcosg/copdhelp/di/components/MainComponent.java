package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.MainModule;
import com.aarcosg.copdhelp.di.modules.MedicalAttentionModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionListPresenter;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionChartFragment;
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionListFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MainModule.class,
                MedicalAttentionModule.class
        })
public interface MainComponent extends ActivityComponent{

    void inject(MainActivity mainActivity);

    void inject(MedicalAttentionListFragment medicalAttentionListFragment);
    void inject(MedicalAttentionChartFragment medicalAttentionChartFragment);

    MainPresenter getMainPresenter();
    MedicalAttentionListPresenter getMedicalAttentionListPresenter();
    MedicalAttentionChartPresenter getMedicalAttentionChartPresenter();

}