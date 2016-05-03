package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.MainModule;
import com.aarcosg.copdhelp.di.modules.MedicalAttentionModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionMainPresenter;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionMainFragment;

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
    void inject(MedicalAttentionMainFragment medicalAttentionMainFragment);

    MainPresenter getMainPresenter();
    MedicalAttentionMainPresenter getMedicalAttentionPresenter();

}