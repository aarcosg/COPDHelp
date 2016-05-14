package com.aarcosg.copdhelp.mvp.presenter.medicalattention;


import com.aarcosg.copdhelp.mvp.presenter.Presenter;

public interface MedicalAttentionChartPresenter extends Presenter {

    void loadWeekMedicalAttentions();

    void loadMonthMedicalAttentions();

    void loadYearMedicalAttentions();

}
