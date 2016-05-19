package com.aarcosg.copdhelp.mvp.view;


import io.realm.RealmObject;
import io.realm.RealmResults;

public interface ChartView<T extends RealmObject> extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindWeekData(RealmResults<T> tRealmResults);

    void bindMonthData(RealmResults<T> tRealmResults);

    void bindYearData(RealmResults<T> tRealmResults);

    void showLoadRealmErrorMessage();
}
