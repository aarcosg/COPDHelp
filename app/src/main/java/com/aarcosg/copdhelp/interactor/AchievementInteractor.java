package com.aarcosg.copdhelp.interactor;


import com.aarcosg.copdhelp.data.realm.entity.Achievement;

import rx.Observable;

public interface AchievementInteractor extends RealmInteractor<Achievement> {

    Observable<Achievement> realmCreateIfNotExists(Achievement achievement);
}
