package com.aarcosg.copdhelp.ui.fragment.copdcat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class COPDCATResultFragment extends Fragment {

    private static final String TAG = COPDCATResultFragment.class.getCanonicalName();
    private static final String ARG_POINTS = "arg_points";

    @Bind(R.id.result_tv)
    TextView mResultTv;
    @Bind(R.id.result_description_tv)
    TextView mResultDescriptionTv;

    public static COPDCATResultFragment newInstance(int answerPoints) {
        COPDCATResultFragment fragment = new COPDCATResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, answerPoints);
        fragment.setArguments(args);
        return fragment;
    }

    public COPDCATResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_copdcat_result, container, false);
        ButterKnife.bind(this, fragmentView);
        int answerPoints = getArguments().getInt(ARG_POINTS);
        bindPoints(answerPoints);
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void bindPoints(int answerPoints){
        mResultTv.setText(String.valueOf(answerPoints));
        int textId = R.string.copd_cat_low_impact;
        if(answerPoints >= 11 && answerPoints <= 20){
            textId = R.string.copd_cat_moderate_impact;
        }else if(answerPoints >= 21 && answerPoints <= 30){
            textId = R.string.copd_cat_high_impact;
        }else if(answerPoints > 30){
            textId = R.string.copd_cat_very_high_impact;
        }
        mResultDescriptionTv.setText(getString(textId));
    }
}
