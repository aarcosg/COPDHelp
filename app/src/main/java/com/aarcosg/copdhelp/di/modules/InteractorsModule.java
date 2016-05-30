package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerApp;
import com.aarcosg.copdhelp.interactor.AchievementInteractor;
import com.aarcosg.copdhelp.interactor.AchievementInteractorImpl;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.interactor.BMIInteractorImpl;
import com.aarcosg.copdhelp.interactor.COPDHelpServiceInteractor;
import com.aarcosg.copdhelp.interactor.COPDHelpServiceInteractorImpl;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.interactor.ExerciseInteractorImpl;
import com.aarcosg.copdhelp.interactor.MainInteractor;
import com.aarcosg.copdhelp.interactor.MainInteractorImpl;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractorImpl;
import com.aarcosg.copdhelp.interactor.MedicineInteractor;
import com.aarcosg.copdhelp.interactor.MedicineInteractorImpl;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractor;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractorImpl;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.interactor.SmokeInteractorImpl;
import com.aarcosg.copdhelp.interactor.UserInteractor;
import com.aarcosg.copdhelp.interactor.UserInteractorImpl;

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

    @Provides
    @PerApp
    public UserInteractor provideUserInteractor() {
        return new UserInteractorImpl();
    }

    @Provides
    @PerApp
    public AchievementInteractor provideAchievementInteractor() {
        return new AchievementInteractorImpl();
    }

    @Provides
    @PerApp
    public COPDHelpServiceInteractor provideCOPDHelpServiceInteractor(){
        return new COPDHelpServiceInteractorImpl();
    }

    @Provides
    @PerApp
    public MedicineInteractor provideMedicineInteractor(){
        return new MedicineInteractorImpl();
    }
}
