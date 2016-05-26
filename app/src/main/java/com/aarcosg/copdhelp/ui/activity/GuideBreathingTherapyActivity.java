package com.aarcosg.copdhelp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.guides.BreathingTherapyMainFragment;

public class GuideBreathingTherapyActivity extends BaseActivity{

    private static final String TAG = GuideBreathingTherapyActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_breathing_therapy_content);
        addFragment(R.id.fragment_container, BreathingTherapyMainFragment.newInstance());
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GuideBreathingTherapyActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
