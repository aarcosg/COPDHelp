package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.ui.fragment.bodex.COPDBODEXIntroFragment;
import com.aarcosg.copdhelp.ui.fragment.bodex.COPDBODEXQuestionFragment;
import com.aarcosg.copdhelp.ui.fragment.bodex.COPDBODEXResultFragment;
import com.aarcosg.copdhelp.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class COPDBODEXActivity extends BaseCOPDIndexActivity implements COPDBODEXQuestionFragment.OnAnswerSelectedListener {

    private Map<Integer,Integer> mPointsMap = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addSlide(COPDBODEXIntroFragment.newInstance());
        addSlide(COPDBODEXQuestionFragment.newInstance(R.layout.fragment_copdbode_question1));
        addSlide(COPDBODEXQuestionFragment.newInstance(R.layout.fragment_copdbode_question2));
        addSlide(COPDBODEXQuestionFragment.newInstance(R.layout.fragment_copdbode_question3));
        addSlide(COPDBODEXQuestionFragment.newInstance(R.layout.fragment_copdbodex_question4));
        addSlide(COPDBODEXResultFragment.newInstance(0));
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
                COPDBODEXResultFragment resultFragment = (COPDBODEXResultFragment) newFragment;
                int totalPoints = Utils.sum(new ArrayList<>(mPointsMap.values()));
                resultFragment.bindPoints(totalPoints);
                updateCOPDIndex(RealmTable.User.INDEX_BODEX,totalPoints);
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
        Intent intent = new Intent(activity, COPDBODEXActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
