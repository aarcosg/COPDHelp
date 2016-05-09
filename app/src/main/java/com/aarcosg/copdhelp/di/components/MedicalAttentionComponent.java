package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.MedicalAttentionModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionMainPresenter;
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionEditFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MedicalAttentionModule.class
        })
public interface MedicalAttentionComponent extends ActivityComponent {

    void inject(MedicalAttentionEditFragment medicalAttentionEditFragment);
    void inject(MedicalAttentionDetailsFragment medicalAttentionDetailsFragment);

    MedicalAttentionMainPresenter getMedicalAttentionMainPresenter();
    MedicalAttentionDetailsPresenter getMedicalAttentionDetailsPresenter();
    MedicalAttentionEditPresenter getMedicalAttentionEditPresenter();

}