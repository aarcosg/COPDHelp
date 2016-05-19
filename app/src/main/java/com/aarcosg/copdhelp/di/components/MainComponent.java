package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.BMIModule;
import com.aarcosg.copdhelp.di.modules.MainModule;
import com.aarcosg.copdhelp.di.modules.MedicalAttentionModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionListPresenter;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIChartFragment;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIListFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionChartFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionListFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MainModule.class,
                MedicalAttentionModule.class,
                BMIModule.class
        })
public interface MainComponent extends ActivityComponent{

    void inject(MainActivity mainActivity);

    void inject(MedicalAttentionListFragment medicalAttentionListFragment);
    void inject(MedicalAttentionChartFragment medicalAttentionChartFragment);

    void inject(BMIListFragment bMIListFragment);
    void inject(BMIChartFragment bMIChartFragment);

    MainPresenter getMainPresenter();

    MedicalAttentionListPresenter getMedicalAttentionListPresenter();
    MedicalAttentionChartPresenter getMedicalAttentionChartPresenter();

    BMIListPresenter getBMIListPresenter();
    BMIChartPresenter getBMIChartPresenter();

}