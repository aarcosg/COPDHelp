package com.aarcosg.copdhelp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.di.components.DaggerSmokeComponent;
import com.aarcosg.copdhelp.di.components.SmokeComponent;
import com.aarcosg.copdhelp.di.modules.SmokeModule;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeEditFragment;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class SmokeActivity extends BaseActivity implements HasComponent<SmokeComponent> {

    private static final String TAG = SmokeActivity.class.getCanonicalName();
    private static final String EXTRA_SMOKE = "extra_smoke_id";

    private SmokeComponent mSmokeComponent;
    private Subscription mSubscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeInjector();
        setContentView(R.layout.activity_smoke_content);
        if(getIntent().hasExtra(EXTRA_SMOKE)){
            addDetailsFragment(getIntent().getExtras().getLong(EXTRA_SMOKE));
        }else{
            addCreateFragment();
        }
        mSubscription = mSmokeComponent
                .getSmokeDetailsPresenter()
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
    public SmokeComponent getComponent() {
        return mSmokeComponent;
    }

    private void initializeInjector() {
        mSmokeComponent = DaggerSmokeComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .smokeModule(new SmokeModule())
                .build();
    }

    private void addDetailsFragment(Long id){
        addFragment(R.id.fragment_container,
                SmokeDetailsFragment.newInstance(id));
    }

    private void addCreateFragment(){
        addFragment(R.id.fragment_container,
                SmokeEditFragment.newInstance());
    }

    private void addEditFragment(Long id){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        SmokeEditFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, SmokeActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void launch(Activity activity, Long SmokeId) {
        Intent intent = new Intent(activity, SmokeActivity.class);
        intent.putExtra(EXTRA_SMOKE, SmokeId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
