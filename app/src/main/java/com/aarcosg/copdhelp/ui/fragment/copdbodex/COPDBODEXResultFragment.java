package com.aarcosg.copdhelp.ui.fragment.copdbodex;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class COPDBODEXResultFragment extends Fragment {

    private static final String TAG = COPDBODEXResultFragment.class.getCanonicalName();
    private static final String ARG_POINTS = "arg_points";

    @Bind(R.id.result_tv)
    TextView mResultTv;
    @Bind(R.id.result_description_tv)
    TextView mResultDescriptionTv;
    @Bind(R.id.result_bode_recommended_tv)
    TextView mResultBodeRecommendedTv;

    public static COPDBODEXResultFragment newInstance(int answerPoints) {
        COPDBODEXResultFragment fragment = new COPDBODEXResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, answerPoints);
        fragment.setArguments(args);
        return fragment;
    }

    public COPDBODEXResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_copdbodex_result, container, false);
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

    public void bindPoints(int answerPoints) {
        mResultTv.setText(String.valueOf(answerPoints));
        int mortality = 20;
        int gravityTextId = R.string.mild;
        String grade = "I";
        boolean bodeIndexRecommended = false;
        if (answerPoints >= 3 && answerPoints <= 4) {
            mortality = 30;
            gravityTextId = R.string.moderate;
            grade = "II";
        } else if (answerPoints >= 5) {
            mortality = 40;
            gravityTextId = R.string.grave;
            grade = "III";
            bodeIndexRecommended = true;
        }
        String gravity = getString(gravityTextId).toLowerCase();

        mResultDescriptionTv.setText(getString(R.string.copdbode_result_description, String.valueOf(mortality), gravity, grade));
        mResultBodeRecommendedTv.setVisibility(bodeIndexRecommended ? View.VISIBLE : View.GONE);
    }
}
