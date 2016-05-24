package com.aarcosg.copdhelp.ui.fragment.copdcat;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class COPDCATQuestionFragment extends Fragment {

    private static final String ARG_QUESTION_ID = "question_id";
    private static final String ARG_QUESTION_TEXT_ID = "question_text_id";
    private static final String ARG_LEFT_ANSWER_TEXT_ID = "left_answer_id";
    private static final String ARG_RIGHT_ANSWER_TEXT_ID = "right_answer_id";

    private OnAnswerSelectedListener mCallback;
    private String mQuestion;
    private String mLeftAnswer;
    private String mRightAnswer;
    private int mQuestionId;

    @Bind(R.id.question_tv)
    TextView mQuestionTv;
    @Bind(R.id.answer0_tv)
    TextView mAnswer0Tv;
    @Bind(R.id.answer5_tv)
    TextView mAnswer5Tv;
    @Bind(R.id.answer_radio_group)
    RadioGroup mAnswerRadioGroup;

    public static COPDCATQuestionFragment newInstance(int questionId, @StringRes int questionText
            , @StringRes int leftAnswer, @StringRes int rightAnswer) {
        COPDCATQuestionFragment fragment = new COPDCATQuestionFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_ID, questionId);
        args.putInt(ARG_QUESTION_TEXT_ID, questionText);
        args.putInt(ARG_LEFT_ANSWER_TEXT_ID, leftAnswer);
        args.putInt(ARG_RIGHT_ANSWER_TEXT_ID, rightAnswer);
        fragment.setArguments(args);
        return fragment;
    }

    public COPDCATQuestionFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null
                && getArguments().containsKey(ARG_QUESTION_ID)
                && getArguments().containsKey(ARG_QUESTION_TEXT_ID)
                && getArguments().containsKey(ARG_LEFT_ANSWER_TEXT_ID)
                && getArguments().containsKey(ARG_RIGHT_ANSWER_TEXT_ID)) {
            mQuestionId = getArguments().getInt(ARG_QUESTION_ID);
            mQuestion = getString(getArguments().getInt(ARG_QUESTION_TEXT_ID));
            mLeftAnswer = getString(getArguments().getInt(ARG_LEFT_ANSWER_TEXT_ID));
            mRightAnswer = getString(getArguments().getInt(ARG_RIGHT_ANSWER_TEXT_ID));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_copdcat_question, container, false);
        ButterKnife.bind(this, fragmentView);
        mQuestionTv.setText(mQuestion);
        mAnswer0Tv.setText(mLeftAnswer);
        mAnswer5Tv.setText(mRightAnswer);
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

    @OnCheckedChanged({R.id.radio0,R.id.radio1,R.id.radio2,R.id.radio3,R.id.radio4,R.id.radio5})
    public void onRadioButtonClicked(RadioButton radioButton){
        mCallback.onAnswerSelected(mQuestionId,Integer.valueOf(radioButton.getText().toString()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int questionId, int answerPoints);
    }

}