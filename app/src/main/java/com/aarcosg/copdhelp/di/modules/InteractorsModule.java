package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.interactor.BMIInteractorImpl;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.interactor.ExerciseInteractorImpl;
import com.aarcosg.copdhelp.interactor.MainInteractor;
import com.aarcosg.copdhelp.interactor.MainInteractorImpl;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractorImpl;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractor;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractorImpl;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.interactor.SmokeInteractorImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class InteractorsModule {

    @Provides
    @PerApp
    public MainInteractor provideMainInteractor() {
        return new MainInteractorImpl();
    }

    @Provides
    @PerApp
    public MedicalAttentionInteractor provideMedicalAttentionInteractor() {
        return new MedicalAttentionInteractorImpl();
    }

    @Provides
    @PerApp
    public BMIInteractor provideBMIInteractor() {
        return new BMIInteractorImpl();
    }

    @Provides
    @PerApp
    public MedicineReminderInteractor provideMedicineReminderInteractor() {
        return new MedicineReminderInteractorImpl();
    }

    @Provides
    @PerApp
    public SmokeInteractor provideSmokeInteractor() {
        return new SmokeInteractorImpl();
    }

    @Provides
    @PerApp
    public ExerciseInteractor provideExerciseInteractor() {
        return new ExerciseInteractorImpl();
    }
}
