package com.aarcosg.copdhelp.interactor;

import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.User;
import com.aarcosg.copdhelp.utils.PrimaryKeyFactory;

import javax.inject.Inject;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import rx.functions.Func1;

public class UserInteractorImpl implements UserInteractor {

    private static final String TAG = UserInteractorImpl.class.getCanonicalName();

    private Realm mRealm;

    @Inject
    public UserInteractorImpl(){}


    @Override
    public Observable<RealmResults<User>> realmFindAll(
            @Nullable Func1<RealmQuery<User>, RealmQuery<User>> predicate
            , @Nullable String sortFieldName
            , @Nullable Sort sortOrder) {
        RealmQuery<User> query = getRealm().where(User.class);
        if(predicate != null){
            query = predicate.call(query);
        }
        RealmResults<User> results;
        if(sortFieldName != null && sortOrder != null){
            results = query.findAllSorted(sortFieldName,sortOrder);
        }else{
            results = query.findAll();
        }
        return Observable.just(results);
    }


    @SuppressWarnings("unchecked")
    @Override
    public Observable<User> realmFindById(Long id) {
        return Observable.just(getRealm().where(User.class)
                .equalTo("id",id).findFirst());
    }


    @Override
    public Observable<User> realmCreate(User user) {
        getRealm().beginTransaction();
        User realmUser = getRealm().createObject(User.class,
                PrimaryKeyFactory.getInstance().nextKey(User.class));
        realmUser.setAge(user.getAge());
        realmUser.setGenre(user.getGenre());
        realmUser.setSmoker(user.isSmoker());
        realmUser.setSmokingYears(user.getSmokingYears());
        realmUser.setIndexCOPDPS(user.getIndexCOPDPS());
        realmUser.setIndexCAT(user.getIndexCAT());
        realmUser.setIndexBODE(user.getIndexBODE());
        realmUser.setIndexBODEx(user.getIndexBODEx());
        getRealm().commitTransaction();
        return Observable.just(realmUser);
    }


    @Override
    public Observable<User> realmUpdate(Long id, User user) {
        getRealm().beginTransaction();
        User realmUser = getRealm().where(User.class)
                .equalTo("id",id).findFirst();
        if(realmUser == null){
            realmUser = getRealm().createObject(User.class,
                    PrimaryKeyFactory.getInstance().nextKey(User.class));
        }
        realmUser.setAge(user.getAge());
        realmUser.setGenre(user.getGenre());
        realmUser.setSmoker(user.isSmoker());
        realmUser.setSmokingYears(user.getSmokingYears());
        realmUser.setIndexCOPDPS(user.getIndexCOPDPS());
        realmUser.setIndexCAT(user.getIndexCAT());
        realmUser.setIndexBODE(user.getIndexBODE());
        realmUser.setIndexBODEx(user.getIndexBODEx());
        getRealm().commitTransaction();
        return Observable.just(realmUser);
    }


    @Override
    public Observable<User> realmRemove(Long id) {
        getRealm().beginTransaction();
        User realmUser = getRealm().where(User.class)
                .equalTo("id",id).findFirst();
        realmUser.deleteFromRealm();
        getRealm().commitTransaction();
        return Observable.just(realmUser);
    }


    @Override
    public Observable<User> updateCOPDIndexResult(Long id, String realmCOPDIndexTable, int result) {
        getRealm().beginTransaction();
        User realmUser = realmCreateIfNotExists(id);
        if(realmCOPDIndexTable.equals(RealmTable.User.INDEX_COPDPS)){
            realmUser.setIndexCOPDPS(result);
        }else if(realmCOPDIndexTable.equals(RealmTable.User.INDEX_CAT)){
            realmUser.setIndexCAT(result);
        }else if(realmCOPDIndexTable.equals(RealmTable.User.INDEX_BODE)){
            realmUser.setIndexBODE(result);
        }else if(realmCOPDIndexTable.equals(RealmTable.User.INDEX_BODEX)){
            realmUser.setIndexBODEx(result);
        }
        getRealm().commitTransaction();
        return Observable.just(realmUser);
    }


    @Override
    public Observable<User> updateCOPDScaleGrade(Long id, String realmCOPDScaleTable, double grade) {
        getRealm().beginTransaction();
        User realmUser = realmCreateIfNotExists(id);
        if(realmCOPDScaleTable.equals(RealmTable.User.SCALE_BORG)){
            realmUser.setScaleBORG(grade);
        }else if(realmCOPDScaleTable.equals(RealmTable.User.SCALE_MMRC)){
            realmUser.setScaleMMRC((int)grade);
        }
        getRealm().commitTransaction();
        return Observable.just(realmUser);
    }

    @Override
    public User realmCreateIfNotExists(Long id) {
        User realmUser = getRealm().where(User.class)
                .equalTo("id",id).findFirst();
        if(realmUser == null){
            getRealm().beginTransaction();
            realmUser = getRealm().createObject(User.class,1L);
            getRealm().commitTransaction();
        }
        return realmUser;
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }
}