package com.aarcosg.copdhelp.di.modules;

import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.interactor.MedicineReminderInteractor;
import com.aarcosg.copdhelp.mvp.presenter.medicinereminder.MedicineReminderListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicinereminder.MedicineReminderListPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public class MedicineReminderModule {

    @Provides
    @PerActivity
    public MedicineReminderListPresenter provideMedicineReminderListPresenter(MedicineReminderInteractor medicineReminderInteractor) {
        return new MedicineReminderListPresenterImpl(medicineReminderInteractor);
    }
    
}