package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionDetailsPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionDetailsPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionEditPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionEditPresenterImpl;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MedicalAttentionModule {

    @Provides
    @PerActivity
    public MedicalAttentionListPresenter provideMedicalAttentionListPresenter(MedicalAttentionInteractor medicalAttentionInteractor) {
        return new MedicalAttentionListPresenterImpl(medicalAttentionInteractor);
    }

    @Provides
    @PerActivity
    public MedicalAttentionChartPresenter provideMedicalAttentionChartPresenter(MedicalAttentionInteractor medicalAttentionInteractor) {
        return new MedicalAttentionChartPresenterImpl(medicalAttentionInteractor);
    }

    @Provides
    @PerActivity
    public MedicalAttentionDetailsPresenter provideMedicalAttentionDetailsPresenter(MedicalAttentionInteractor medicalAttentionInteractor) {
        return new MedicalAttentionDetailsPresenterImpl(medicalAttentionInteractor);
    }

    @Provides
    @PerActivity
    public MedicalAttentionEditPresenter provideMedicalAttentionEditPresenter(MedicalAttentionInteractor medicalAttentionInteractor) {
        return new MedicalAttentionEditPresenterImpl(medicalAttentionInteractor);
    }
}