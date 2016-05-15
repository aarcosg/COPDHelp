package com.aarcosg.copdhelp.mvp.presenter;


import io.realm.RealmObject;

public interface ListPresenter<T extends RealmObject> extends Presenter {

    void loadAllFromRealm();

    void removeFromRealm(Long id);
}
