package com.aarcosg.copdhelp.mvp.presenter;

import io.realm.RealmObject;
import rx.subjects.PublishSubject;

public interface DetailsPresenter<T extends RealmObject> extends Presenter {

    void loadRealmObject(Long id);

    void onEditButtonClick(Long id);

    PublishSubject<Long> getOnEditButtonClickSubject();
}
