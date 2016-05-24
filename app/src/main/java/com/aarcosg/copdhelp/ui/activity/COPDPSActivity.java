package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.copdps.COPDPSIntroFragment;
import com.aarcosg.copdhelp.ui.fragment.copdps.COPDPSQuestionFragment;
import com.aarcosg.copdhelp.ui.fragment.copdps.COPDPSResultFragment;
import com.aarcosg.copdhelp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class COPDPSActivity extends BaseAppIntroActivity implements COPDPSQuestionFragment.OnAnswerSelectedListener {

    private Map<Integer,Integer> mPointsMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(COPDPSIntroFragment.newInstance());
        addSlide(COPDPSQuestionFragment.newInstance(R.layout.fragment_copdps_question1));
        addSlide(COPDPSQuestionFragment.newInstance(R.layout.fragment_copdps_question2));
        addSlide(COPDPSQuestionFragment.newInstance(R.layout.fragment_copdps_question3));
        addSlide(COPDPSQuestionFragment.newInstance(R.layout.fragment_copdps_question4));
        addSlide(COPDPSQuestionFragment.newInstance(R.layout.fragment_copdps_question5));
        addSlide(COPDPSResultFragment.newInstance(0));
        showSkipButton(false);
        mPointsMap.put(0,0);
    }

    @Override
    public void onAnswerSelected(int questionId, int answerPoints) {
        mPointsMap.put(questionId, answerPoints);
        setNextPageSwipeLock(false);
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if(newFragment != null){
            int newFragmentIndex = getSlides().indexOf(newFragment);
            if(newFragmentIndex == getSlides().size() - 1){
                COPDPSResultFragment resultFragment = (COPDPSResultFragment) newFragment;
                resultFragment.bindPoints(Utils.sum(new ArrayList<>(mPointsMap.values())));
            }else{
                if(mPointsMap.containsKey(newFragmentIndex)){
                    setNextPageSwipeLock(false);
                }else{
                    setNextPageSwipeLock(true);
                }
            }
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, COPDPSActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
