package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.appintro.AppIntroFragment;

public class AppIntroActivity extends BaseAppIntroActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(AppIntroFragment.newInstance(R.layout.fragment_appintro_slide1));
        addSlide(AppIntroFragment.newInstance(R.layout.fragment_appintro_slide2));
        addSlide(AppIntroFragment.newInstance(R.layout.fragment_appintro_slide3));
        addSlide(AppIntroFragment.newInstance(R.layout.fragment_appintro_slide4));
        addSlide(AppIntroFragment.newInstance(R.layout.fragment_appintro_slide5));
        addSlide(AppIntroFragment.newInstance(R.layout.fragment_appintro_slide6));
        showSkipButton(false);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        MainActivity.launch(this);
        finish();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, AppIntroActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
