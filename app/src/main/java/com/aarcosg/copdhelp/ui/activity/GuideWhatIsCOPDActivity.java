package com.aarcosg.copdhelp.ui.activity;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.guides.WhatIsCOPDMainFragment;

public class GuideWhatIsCOPDActivity extends BaseActivity{

    private static final String TAG = GuideWhatIsCOPDActivity.class.getCanonicalName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide_what_is_copd_content);
        addFragment(R.id.fragment_container, WhatIsCOPDMainFragment.newInstance());
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, GuideWhatIsCOPDActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
