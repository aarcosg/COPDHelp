package com.aarcosg.copdhelp.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.aarcosg.copdhelp.R;
import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroViewPager;
import com.mikepenz.iconics.context.IconicsContextWrapper;

public abstract class BaseAppIntroActivity extends AppIntro {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showStatusBar(false);
        setFadeAnimation();
        setSkipText(getString(R.string.skip));
        setDoneText(getString(R.string.done));
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(IconicsContextWrapper.wrap(newBase));
    }

    protected void toggleNextPageSwipeLock() {
        AppIntroViewPager pager = getPager();
        boolean pagingState = pager.isNextPagingEnabled();
        setNextPageSwipeLock(pagingState);
    }

    protected void toggleSwipeLock() {
        AppIntroViewPager pager = getPager();
        boolean pagingState = pager.isPagingEnabled();
        setSwipeLock(pagingState);
    }

    protected void toggleProgressButton() {
        boolean progressButtonState = isProgressButtonEnabled();
        progressButtonState = !progressButtonState;
        setProgressButtonEnabled(progressButtonState);
    }

    protected void toggleSkipButton() {
        boolean skipButtonState = isSkipButtonEnabled();
        skipButtonState = !skipButtonState;
        showSkipButton(skipButtonState);
    }

}
