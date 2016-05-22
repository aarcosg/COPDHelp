package com.aarcosg.copdhelp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.di.components.DaggerExerciseComponent;
import com.aarcosg.copdhelp.di.components.ExerciseComponent;
import com.aarcosg.copdhelp.di.modules.ExerciseModule;
import com.aarcosg.copdhelp.ui.fragment.exercise.ExerciseDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.exercise.ExerciseEditFragment;

import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class ExerciseActivity extends BaseActivity implements HasComponent<ExerciseComponent> {

    private static final String TAG = ExerciseActivity.class.getCanonicalName();
    private static final String EXTRA_EXERCISE = "extra_exercise_id";

    private ExerciseComponent mExerciseComponent;
    private Subscription mSubscription = Subscriptions.empty();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeInjector();
        setContentView(R.layout.activity_exercise_content);
        if(getIntent().hasExtra(EXTRA_EXERCISE)){
            addDetailsFragment(getIntent().getExtras().getLong(EXTRA_EXERCISE));
        }else{
            addCreateFragment();
        }
        mSubscription = mExerciseComponent
                .getExerciseDetailsPresenter()
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
    public ExerciseComponent getComponent() {
        return mExerciseComponent;
    }

    private void initializeInjector() {
        mExerciseComponent = DaggerExerciseComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .exerciseModule(new ExerciseModule())
                .build();
    }

    private void addDetailsFragment(Long id){
        addFragment(R.id.fragment_container,
                ExerciseDetailsFragment.newInstance(id));
    }

    private void addCreateFragment(){
        addFragment(R.id.fragment_container,
                ExerciseEditFragment.newInstance());
    }

    private void addEditFragment(Long id){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,
                        ExerciseEditFragment.newInstance(id))
                .addToBackStack(null)
                .commit();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, ExerciseActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void launch(Activity activity, Long ExerciseId) {
        Intent intent = new Intent(activity, ExerciseActivity.class);
        intent.putExtra(EXTRA_EXERCISE, ExerciseId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
