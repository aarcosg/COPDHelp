package com.aarcosg.copdhelp.ui.fragment.copdbode;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class COPDBODEResultFragment extends Fragment {

    private static final String TAG = COPDBODEResultFragment.class.getCanonicalName();
    private static final String ARG_POINTS = "arg_points";

    @Bind(R.id.result_tv)
    TextView mResultTv;
    @Bind(R.id.result_description_tv)
    TextView mResultDescriptionTv;

    public static COPDBODEResultFragment newInstance(int answerPoints) {
        COPDBODEResultFragment fragment = new COPDBODEResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, answerPoints);
        fragment.setArguments(args);
        return fragment;
    }

    public COPDBODEResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_copdbode_result, container, false);
        ButterKnife.bind(this, fragmentView);
        int answerPoints = getArguments().getInt(ARG_POINTS);
        bindPoints(answerPoints,false);
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void bindPoints(int answerPoints, boolean extraAnswer){
        mResultTv.setText(String.valueOf(answerPoints));
        int mortality = 20;
        int gravityTextId = R.string.mild;
        String grade = "I";
        if(answerPoints >= 3  && answerPoints <= 4){
            mortality = 30;
            gravityTextId = R.string.moderate;
            grade = "II";
        }else if(answerPoints >= 5  && answerPoints <= 6){
            mortality = 40;
            gravityTextId = R.string.grave;
            grade = "III";
        }else if(answerPoints >= 7){
            mortality = 80;
            if(extraAnswer){
                gravityTextId = R.string.terminal;
                grade = "V";
            }else{
                gravityTextId = R.string.very_grave;
                grade = "IV";
            }
        }
        String gravity = getString(gravityTextId).toLowerCase();

        mResultDescriptionTv.setText(getString(R.string.copdbode_result_description,String.valueOf(mortality),gravity,grade));
    }
}
