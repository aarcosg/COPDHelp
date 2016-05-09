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
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionDetailsFragment;
import com.aarcosg.copdhelp.ui.fragment.MedicalAttentionEditFragment;

public class MedicalAttentionDetailsActivity extends BaseActivity implements HasComponent<MedicalAttentionComponent> {

    private static final String TAG = MedicalAttentionDetailsActivity.class.getCanonicalName();
    private static final String EXTRA_MEDICAL_ATTENTION = "extra_medical_attention_id";

    private MedicalAttentionComponent mMedicalAttentionComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initializeInjector();
        setContentView(R.layout.activity_medical_attention_content);
        if(getIntent().hasExtra(EXTRA_MEDICAL_ATTENTION)){
            addFragment(R.id.fragment_container,
                    MedicalAttentionDetailsFragment.newInstance(
                            getIntent().getExtras().getLong(EXTRA_MEDICAL_ATTENTION)));
        }else{
            addFragment(R.id.fragment_container, MedicalAttentionEditFragment.newInstance());
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

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MedicalAttentionDetailsActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }

    public static void launch(Activity activity, Long medicalAttentionId) {
        Intent intent = new Intent(activity, MedicalAttentionDetailsActivity.class);
        intent.putExtra(EXTRA_MEDICAL_ATTENTION, medicalAttentionId);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
