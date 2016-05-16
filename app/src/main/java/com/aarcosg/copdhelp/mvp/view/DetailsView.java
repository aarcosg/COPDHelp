package com.aarcosg.copdhelp.mvp.view;

import io.realm.RealmObject;

public interface DetailsView<T extends RealmObject> extends View {

    void showProgressBar();

    void hideProgressBar();

    void bindRealmObject(T t);

    void showRealmObjectNotFoundError();
}
