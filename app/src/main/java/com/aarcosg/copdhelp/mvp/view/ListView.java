package com.aarcosg.copdhelp.mvp.view;


import io.realm.RealmObject;
import io.realm.RealmResults;

public interface ListView<T extends RealmObject> extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindAll(RealmResults<T> realmResults);

    void showEmptyView();

    void hideEmptyView();

    void showLoadAllRealmErrorMessage();

    void showRemoveRealmSuccessMessage();

    void showRemoveRealmErrorMessage();
}
