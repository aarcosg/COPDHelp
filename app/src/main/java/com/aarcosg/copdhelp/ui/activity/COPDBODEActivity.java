package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.ui.fragment.copdbode.COPDBODEIntroFragment;
import com.aarcosg.copdhelp.ui.fragment.copdbode.COPDBODEQuestionFragment;
import com.aarcosg.copdhelp.ui.fragment.copdbode.COPDBODEResultFragment;
import com.aarcosg.copdhelp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class COPDBODEActivity extends BaseCOPDIndexActivity implements COPDBODEQuestionFragment.OnAnswerSelectedListener {

    private Map<Integer,Integer> mPointsMap = new HashMap<>();
    private boolean mExtraAnswer = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(COPDBODEIntroFragment.newInstance());
        addSlide(COPDBODEQuestionFragment.newInstance(R.layout.fragment_copdbode_question1));
        addSlide(COPDBODEQuestionFragment.newInstance(R.layout.fragment_copdbode_question2));
        addSlide(COPDBODEQuestionFragment.newInstance(R.layout.fragment_copdbode_question3));
        addSlide(COPDBODEQuestionFragment.newInstance(R.layout.fragment_copdbode_question4));
        addSlide(COPDBODEQuestionFragment.newInstance(R.layout.fragment_copdbode_question5));
        addSlide(COPDBODEResultFragment.newInstance(0));
        showSkipButton(false);
        mPointsMap.put(0,0);
    }

    @Override
    public void onAnswerSelected(int questionId, int answerPoints) {
        mPointsMap.put(questionId, answerPoints);
        setNextPageSwipeLock(false);
    }

    @Override
    public void onExtraAnswerSelected(int questionId, int answerPoints){
        mExtraAnswer = answerPoints == 1;
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        int fragmentIndex = getSlides().indexOf(currentFragment);
        if(fragmentIndex == getSlides().size() - 2){
            mPointsMap.put(5,0);
            getPager().setCurrentItem(getSlides().size()-1);
        }
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);
        if(newFragment != null){
            int newFragmentIndex = getSlides().indexOf(newFragment);
            if(newFragmentIndex == getSlides().size() - 1){
                COPDBODEResultFragment resultFragment = (COPDBODEResultFragment) newFragment;
                int totalPoints = Utils.sum(new ArrayList<>(mPointsMap.values()));
                resultFragment.bindPoints(totalPoints,mExtraAnswer);
                updateCOPDIndex(RealmTable.User.INDEX_BODE,totalPoints);
            }else{
                if(mPointsMap.containsKey(newFragmentIndex)){
                    setNextPageSwipeLock(false);
                }else{
                    setNextPageSwipeLock(true);
                }
            }
            if(newFragmentIndex == getSlides().size() - 2){
                showSkipButton(true);
                setNextPageSwipeLock(false);
            }else{
                showSkipButton(false);
            }
        }
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, COPDBODEActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
