package com.aarcosg.copdhelp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.di.components.BMIComponent;
import com.aarcosg.copdhelp.di.components.DaggerBMIComponent;
import com.aarcosg.copdhelp.di.modules.BMIModule;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIEditFragment;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class BMIActivity extends BaseActivity implements HasComponent<BMIComponent> {

    private static final String TAG = BMIActivity.class.getCanonicalName();
    private static final String EXTRA_BMI = "extra_bmi_id";

    private BMIComponent mBMIComponent;
    private Subscription mSubscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeInjector();
        setContentView(R.layout.activity_bmi_content);
        if(getIntent().hasExtra(EXTRA_BMI)){
            addDetailsFragment(getIntent().getExtras().getLong(EXTRA_BMI));
        }else{
            addCreateFragment();
        }
       /* mSubscription = mBMIComponent
                .getBMIDetailsPresenter()
                .getOnEditButtonClickSubject()
                .subscribe(this::addEditFragment);*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @Override
    public BMIComponent getComponent() {
        return mBMIComponent;
    }

    private void initializeInjector() {
        mBMIComponent = DaggerBMIComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .bMIModule(new BMIModule())
                .build();
    }

    private void addDetailsFragment(Long id){
        addFragment(R.id.fragment_container,
                BMIDetailsFragment.newInstance(id));
    }

    private void addCreateFragment(){
        addFragment(R.id.fragment_container,
                BMIEditFragment.newInstance());
    }

    private void addEditFragment(Long id){
        /*getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        BMIEditFragment.newInstance(id))
                .addToBackStack(null)
                .commit();*/
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, BMIActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void launch(Activity activity, Long BMIId) {
        Intent intent = new Intent(activity, BMIActivity.class);
        intent.putExtra(EXTRA_BMI, BMIId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
