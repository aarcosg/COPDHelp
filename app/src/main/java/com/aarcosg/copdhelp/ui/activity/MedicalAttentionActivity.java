package com.aarcosg.copdhelp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.di.components.DaggerMedicalAttentionComponent;
import com.aarcosg.copdhelp.di.components.MedicalAttentionComponent;
import com.aarcosg.copdhelp.di.modules.MedicalAttentionModule;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionEditFragment;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class MedicalAttentionActivity extends BaseActivity implements HasComponent<MedicalAttentionComponent> {

    private static final String TAG = MedicalAttentionActivity.class.getCanonicalName();
    private static final String EXTRA_MEDICAL_ATTENTION = "extra_medical_attention_id";

    private MedicalAttentionComponent mMedicalAttentionComponent;
    private Subscription mSubscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeInjector();
        setContentView(R.layout.activity_medical_attention_content);
        if(getIntent().hasExtra(EXTRA_MEDICAL_ATTENTION)){
            addDetailsFragment(getIntent().getExtras().getLong(EXTRA_MEDICAL_ATTENTION));
        }else{
            addCreateFragment();
        }
        mSubscription = mMedicalAttentionComponent
                .getMedicalAttentionDetailsPresenter()
                .getOnEditButtonClickSubject()
                .subscribe(this::addEditFragment);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public MedicalAttentionComponent getComponent() {
        return mMedicalAttentionComponent;
    }

    private void initializeInjector() {
        mMedicalAttentionComponent = DaggerMedicalAttentionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .medicalAttentionModule(new MedicalAttentionModule())
                .build();
    }

    private void addDetailsFragment(Long id){
        addFragment(R.id.fragment_container,
                MedicalAttentionDetailsFragment.newInstance(id));
    }

    private void addCreateFragment(){
        addFragment(R.id.fragment_container,
                MedicalAttentionEditFragment.newInstance());
    }

    private void addEditFragment(Long id){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        MedicalAttentionEditFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MedicalAttentionActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void launch(Activity activity, Long medicalAttentionId) {
        Intent intent = new Intent(activity, MedicalAttentionActivity.class);
        intent.putExtra(EXTRA_MEDICAL_ATTENTION, medicalAttentionId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
