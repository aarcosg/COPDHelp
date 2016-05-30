package com.aarcosg.copdhelp.ui.fragment.mycopd;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.MedicalAttention;
import com.aarcosg.copdhelp.data.realm.entity.User;
import com.aarcosg.copdhelp.di.components.ApplicationComponent;
import com.aarcosg.copdhelp.interactor.BMIInteractor;
import com.aarcosg.copdhelp.interactor.ExerciseInteractor;
import com.aarcosg.copdhelp.interactor.MedicalAttentionInteractor;
import com.aarcosg.copdhelp.interactor.MedicineInteractor;
import com.aarcosg.copdhelp.interactor.SmokeInteractor;
import com.aarcosg.copdhelp.interactor.UserInteractor;
import com.aarcosg.copdhelp.ui.activity.MainActivity;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Sort;
import rx.subscriptions.CompositeSubscription;

public class MyCOPDMainFragment extends BaseFragment {

    private static final String TAG = MyCOPDMainFragment.class.getCanonicalName();

    @Bind(R.id.exacerbations_tv)
    TextView mExacerbationsTv;
    @Bind(R.id.exacerbations_cardview)
    CardView mExacerbationsCardview;
    @Bind(R.id.checkups_tv)
    TextView mCheckupsTv;
    @Bind(R.id.checkups_cardview)
    CardView mCheckupsCardview;
    @Bind(R.id.bmi_tv)
    TextView mBmiTv;
    @Bind(R.id.bmi_cardview)
    CardView mBmiCardview;
    @Bind(R.id.doses_tv)
    TextView mDosesTv;
    @Bind(R.id.doses_cardview)
    CardView mDosesCardview;
    @Bind(R.id.exercise_tv)
    TextView mExerciseTv;
    @Bind(R.id.exercise_cardview)
    CardView mExerciseCardview;
    @Bind(R.id.smoking_tv)
    TextView mSmokingTv;
    @Bind(R.id.smoking_cardview)
    CardView mSmokingCardview;
    @Bind(R.id.copdps_tv)
    TextView mCopdpsTv;
    @Bind(R.id.copdps_cardview)
    CardView mCopdpsCardview;
    @Bind(R.id.cat_tv)
    TextView mCatTv;
    @Bind(R.id.cat_cardview)
    CardView mCatCardview;
    @Bind(R.id.bode_tv)
    TextView mBodeTv;
    @Bind(R.id.bode_cardview)
    CardView mBodeCardview;
    @Bind(R.id.bodex_tv)
    TextView mBodexTv;
    @Bind(R.id.bodex_cardview)
    CardView mBodexCardview;
    @Bind(R.id.mmrc_tv)
    TextView mMmrcTv;
    @Bind(R.id.mmrc_cardview)
    CardView mMmrcCardview;
    @Bind(R.id.borg_tv)
    TextView mBorgTv;
    @Bind(R.id.borg_cardview)
    CardView mBorgCardview;

    private CompositeSubscription mSubscriptions = new CompositeSubscription();
    private OnMyCOPDCardSelectedListener mCallback;
    private User mUser;

    public static MyCOPDMainFragment newInstance() {
        MyCOPDMainFragment fragment = new MyCOPDMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MyCOPDMainFragment() {}

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnMyCOPDCardSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnCardSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_my_copd_main, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubscriptions = new CompositeSubscription();
        loadUser();
        loadUserRelatedData();
        loadData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if (!mSubscriptions.isUnsubscribed()) {
            mSubscriptions.unsubscribe();
        }
        if(mUser != null){
            mUser.removeChangeListeners();
        }
    }

    @OnClick({R.id.exacerbations_cardview,R.id.checkups_cardview})
    void goToMedicalAttentions(){
        mCallback.openFragment(MainActivity.MEDICAL_ATTENTION_MAIN_ID);
    }

    @OnClick({R.id.bmi_cardview})
    void goToBMI(){
        mCallback.openFragment(MainActivity.BMI_MAIN_ID);
    }

    @OnClick({R.id.exercise_cardview})
    void goToExercises(){
        mCallback.openFragment(MainActivity.EXERCISE_MAIN_ID);
    }

    @OnClick({R.id.smoking_cardview})
    void goToSmoking(){
        mCallback.openFragment(MainActivity.SMOKE_MAIN_ID);
    }

    @OnClick({R.id.copdps_cardview})
    void goToCOPDPS(){
        mCallback.openFragment(MainActivity.COPDPS_MAIN_ID);
    }

    @OnClick({R.id.cat_cardview})
    void goToCAT(){
        mCallback.openFragment(MainActivity.COPDCAT_MAIN_ID);
    }

    @OnClick({R.id.bode_cardview})
    void goToBODE(){
        mCallback.openFragment(MainActivity.COPDBODE_MAIN_ID);
    }

    @OnClick({R.id.bodex_cardview})
    void goToBODEx(){
        mCallback.openFragment(MainActivity.COPDBODEX_MAIN_ID);
    }

    @OnClick({R.id.mmrc_cardview})
    void goToMMRC(){
        mCallback.openFragment(MainActivity.SCALE_MMRC_DYSPNEA_MAIN_ID);
    }

    @OnClick({R.id.borg_cardview})
    void goToBORG(){
        mCallback.openFragment(MainActivity.SCALE_BORG_MAIN_ID);
    }

    private void loadUser(){
        UserInteractor userInteractor = COPDHelpApplication.get(getContext()).getApplicationComponent().getUserInteractor();
        mSubscriptions.add(userInteractor.realmFindById(1L).subscribe(user -> mUser = user));
        mUser.addChangeListener(element -> loadUserRelatedData());
    }

    private void loadUserRelatedData(){
        //COPD-PS index
        if(mUser != null && mUser.getIndexCOPDPS() != null){
            mCopdpsTv.setText(mUser.getIndexCOPDPS().toString());
        }
        //CAT index
        if(mUser != null && mUser.getIndexCAT() != null){
            mCatTv.setText(mUser.getIndexCAT().toString());
        }
        //BODE index
        if(mUser != null && mUser.getIndexBODE() != null){
            mBodeTv.setText(mUser.getIndexBODE().toString());
        }
        //BODEx index
        if(mUser != null && mUser.getIndexBODEx() != null){
            mBodexTv.setText(mUser.getIndexBODEx().toString());
        }
        //mMRC scale
        if(mUser != null && mUser.getScaleMMRC() != null){
            mMmrcTv.setText(mUser.getScaleMMRC().toString());
        }
        //Borg scale
        if(mUser != null && mUser.getScaleBORG() != null){
            mBorgTv.setText(mUser.getScaleBORG().toString());
        }
    }

    private void loadData(){
        ApplicationComponent appComponent = COPDHelpApplication.get(getContext()).getApplicationComponent();
        MedicalAttentionInteractor medicalAttentionInteractor = appComponent.getMedicalAttentionInteractor();
        BMIInteractor bmiInteractor = appComponent.getBMIInteractor();
        MedicineInteractor medicineInteractor = appComponent.getMedicineInteractor();
        ExerciseInteractor exerciseInteractor = appComponent.getExerciseInteractor();
        SmokeInteractor smokeInteractor = appComponent.getSmokeInteractor();

        Calendar calendar = Calendar.getInstance();
        // Exacerbations this year
        mSubscriptions.add(medicalAttentionInteractor.realmFindAll(query ->
                query
                    .equalTo(RealmTable.MedicalAttention.YEAR,calendar.get(Calendar.YEAR))
                    .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_EMERGENCY)
                ,null,null)
                .subscribe(medicalAttentions -> mExacerbationsTv.setText(String.valueOf(medicalAttentions.size())))
        );
        // Checkups this year
        mSubscriptions.add(medicalAttentionInteractor.realmFindAll(query ->
                        query
                            .equalTo(RealmTable.MedicalAttention.YEAR,calendar.get(Calendar.YEAR))
                            .equalTo(RealmTable.MedicalAttention.TYPE, MedicalAttention.TYPE_CHECKUP)
                ,null,null)
                .subscribe(medicalAttentions -> mCheckupsTv.setText(String.valueOf(medicalAttentions.size())))
        );
        // Last BMI
        mSubscriptions.add(bmiInteractor.realmFindAll(null,RealmTable.BMI.TIMESTAMP,Sort.DESCENDING)
                .subscribe(bmis -> {
                    if(!bmis.isEmpty()){
                        mBmiTv.setText(String.format("%.2f",bmis.get(0).getBmi()));
                    }
                })
        );
        // Medication taken this week
        mSubscriptions.add(medicineInteractor.realmFindAll(query ->
                        query.equalTo(RealmTable.Medicine.WEEK_OF_YEAR,calendar.get(Calendar.WEEK_OF_YEAR))
                ,null,null)
                .subscribe(medicines -> mDosesTv.setText(String.valueOf(medicines.size())))
        );
        //Exercise this week
        mSubscriptions.add(exerciseInteractor.realmFindAll(query ->
                        query.equalTo(RealmTable.Exercise.WEEK_OF_YEAR,calendar.get(Calendar.WEEK_OF_YEAR))
                ,null,null)
                .subscribe(exercises -> mExerciseTv.setText(String.valueOf(exercises.where().sum(RealmTable.Exercise.DURATION).intValue())))
        );
        //Smoking this week
        mSubscriptions.add(smokeInteractor.realmFindAll(query ->
                        query.equalTo(RealmTable.Smoke.WEEK_OF_YEAR,calendar.get(Calendar.WEEK_OF_YEAR))
                ,null,null)
                .subscribe(smokes -> mSmokingTv.setText(String.valueOf(smokes.where().sum(RealmTable.Smoke.QUANTITY).intValue())))
        );

    }

    public interface OnMyCOPDCardSelectedListener {
        void openFragment(int fragmentId);
    }

}
