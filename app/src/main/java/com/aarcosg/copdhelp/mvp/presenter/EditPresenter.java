package com.aarcosg.copdhelp.mvp.presenter;

import io.realm.RealmObject;

public interface EditPresenter<T extends RealmObject> extends Presenter {

    void loadRealmObject(Long id);

    void addRealmObject(T t);

    void editRealmObject(Long id, T t);
}
