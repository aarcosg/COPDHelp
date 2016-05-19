package com.aarcosg.copdhelp.mvp.presenter;

import io.realm.RealmObject;

public interface ChartPresenter<T extends RealmObject> extends Presenter {

    void loadWeekData();

    void loadMonthData();

    void loadYearData();

    void onCreateView();

}
