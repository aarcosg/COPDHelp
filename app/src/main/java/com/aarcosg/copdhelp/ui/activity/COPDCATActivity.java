package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.ui.fragment.copdcat.COPDCATIntroFragment;
import com.aarcosg.copdhelp.ui.fragment.copdcat.COPDCATQuestionFragment;
import com.aarcosg.copdhelp.ui.fragment.copdcat.COPDCATResultFragment;
import com.aarcosg.copdhelp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class COPDCATActivity extends BaseCOPDIndexActivity implements COPDCATQuestionFragment.OnAnswerSelectedListener {

    private Map<Integer,Integer> mPointsMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(COPDCATIntroFragment.newInstance());
        addSlide(COPDCATQuestionFragment.newInstance(1, R.string.copdcat_question1, R.string.copdcat_question1_answer0, R.string.copdcat_question1_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(2, R.string.copdcat_question2, R.string.copdcat_question2_answer0, R.string.copdcat_question2_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(3, R.string.copdcat_question3, R.string.copdcat_question3_answer0, R.string.copdcat_question3_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(4, R.string.copdcat_question4, R.string.copdcat_question4_answer0, R.string.copdcat_question4_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(5, R.string.copdcat_question5, R.string.copdcat_question5_answer0, R.string.copdcat_question5_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(6, R.string.copdcat_question6, R.string.copdcat_question6_answer0, R.string.copdcat_question6_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(7, R.string.copdcat_question7, R.string.copdcat_question7_answer0, R.string.copdcat_question7_answer5));
        addSlide(COPDCATQuestionFragment.newInstance(8, R.string.copdcat_question8, R.string.copdcat_question8_answer0, R.string.copdcat_question8_answer5));
        addSlide(COPDCATResultFragment.newInstance(0));
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
                COPDCATResultFragment resultFragment = (COPDCATResultFragment) newFragment;
                int totalPoints = Utils.sum(new ArrayList<>(mPointsMap.values()));
                resultFragment.bindPoints(totalPoints);
                updateCOPDIndex(RealmTable.User.INDEX_CAT,totalPoints);
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
        Intent intent = new Intent(activity, COPDCATActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
