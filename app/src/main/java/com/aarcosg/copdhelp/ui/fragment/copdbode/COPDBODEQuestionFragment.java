package com.aarcosg.copdhelp.ui.fragment.copdbode;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.aarcosg.copdhelp.R;

import butterknife.ButterKnife;

public class COPDBODEQuestionFragment extends Fragment {

    private static final String ARG_LAYOUT_RES_ID = "layoutResId";

    private OnAnswerSelectedListener mCallback;
    private int layoutResId;

    public static COPDBODEQuestionFragment newInstance(int layoutResId) {
        COPDBODEQuestionFragment fragment = new COPDBODEQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT_RES_ID, layoutResId);
        fragment.setArguments(args);
        return fragment;
    }

    public COPDBODEQuestionFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(ARG_LAYOUT_RES_ID))
            layoutResId = getArguments().getInt(ARG_LAYOUT_RES_ID);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView =  inflater.inflate(layoutResId, container, false);
        RadioGroup radioGroup = ButterKnife.findById(fragmentView, R.id.answer_radio_group);
        radioGroup.setOnCheckedChangeListener(this::onRadioGroupClicked);
        return fragmentView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnAnswerSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnAnswerSelectedListener");
        }
    }

    private void onRadioGroupClicked(RadioGroup radioGroup, int checkedId) {
        int questionIndex = 0;
        int points = 0;
        if(layoutResId == R.layout.fragment_copdbode_question5){
            questionIndex = 5;
            switch (checkedId){
                case R.id.answer1_radio:
                case R.id.answer2_radio:
                case R.id.answer3_radio:
                case R.id.answer4_radio: points = 1; break;
            }
            mCallback.onExtraAnswerSelected(questionIndex,points);
        }else{
            switch (layoutResId){
                case R.layout.fragment_copdbode_question1:
                    questionIndex = 1;
                    switch (checkedId){
                        case R.id.answer2_radio: points = 1; break;
                    }
                    break;
                case R.layout.fragment_copdbode_question2:
                    questionIndex = 2;
                    switch (checkedId){
                        case R.id.answer2_radio: points = 1; break;
                        case R.id.answer3_radio: points = 2; break;
                        case R.id.answer4_radio: points = 3; break;
                    }
                    break;
                case R.layout.fragment_copdbode_question3:
                    questionIndex = 3;
                    switch (checkedId){
                        case R.id.answer2_radio: points = 1; break;
                        case R.id.answer3_radio: points = 2; break;
                        case R.id.answer4_radio: points = 3; break;
                    }
                    break;
                case R.layout.fragment_copdbode_question4:
                    questionIndex = 4;
                    switch (checkedId){
                        case R.id.answer2_radio: points = 1; break;
                        case R.id.answer3_radio: points = 2; break;
                        case R.id.answer4_radio: points = 3; break;
                    }
                    break;
            }
            mCallback.onAnswerSelected(questionIndex, points);
        }

    }

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionId, int answerPoints);
        void onExtraAnswerSelected(int questionId, int answerPoints);
    }

}