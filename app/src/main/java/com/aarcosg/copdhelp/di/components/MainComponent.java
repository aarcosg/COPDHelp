package com.aarcosg.copdhelp.di.components;

import com.aarcosg.copdhelp.di.modules.ActivityModule;
import com.aarcosg.copdhelp.di.modules.BMIModule;
import com.aarcosg.copdhelp.di.modules.MainModule;
import com.aarcosg.copdhelp.di.modules.MedicalAttentionModule;
import com.aarcosg.copdhelp.di.modules.MedicineReminderModule;
import com.aarcosg.copdhelp.di.modules.SmokeModule;
import com.aarcosg.copdhelp.di.scopes.PerActivity;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.bmi.BMIListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicalattention.MedicalAttentionListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.medicinereminder.MedicineReminderListPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeChartPresenter;
import com.aarcosg.copdhelp.mvp.presenter.smoke.SmokeListPresenter;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIChartFragment;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIListFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionChartFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionListFragment;
import com.aarcosg.copdhelp.ui.fragment.medicinereminder.MedicineReminderListFragment;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeChartFragment;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeListFragment;

import dagger.Component;

@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {
                ActivityModule.class,
                MainModule.class,
                MedicalAttentionModule.class,
                BMIModule.class,
                MedicineReminderModule.class,
                SmokeModule.class
        })
public interface MainComponent extends ActivityComponent{

    void inject(MainActivity mainActivity);

    void inject(MedicalAttentionListFragment medicalAttentionListFragment);
    void inject(MedicalAttentionChartFragment medicalAttentionChartFragment);

    void inject(BMIListFragment bMIListFragment);
    void inject(BMIChartFragment bMIChartFragment);

    void inject(MedicineReminderListFragment medicineReminderListFragment);

    void inject(SmokeListFragment smokeListFragment);
    void inject(SmokeChartFragment smokeChartFragment);

    MainPresenter getMainPresenter();

    MedicalAttentionListPresenter getMedicalAttentionListPresenter();
    MedicalAttentionChartPresenter getMedicalAttentionChartPresenter();

    BMIListPresenter getBMIListPresenter();
    BMIChartPresenter getBMIChartPresenter();

    MedicineReminderListPresenter getMedicineReminderListPresenter();

    SmokeListPresenter getSmokeListPresenter();
    SmokeChartPresenter getSmokeChartPresenter();

}