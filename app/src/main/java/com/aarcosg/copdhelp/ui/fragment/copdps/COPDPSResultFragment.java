package com.aarcosg.copdhelp.ui.fragment.copdps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aarcosg.copdhelp.R;

import butterknife.Bind;
import butterknife.ButterKnife;


public class COPDPSResultFragment extends Fragment {

    private static final String TAG = COPDPSResultFragment.class.getCanonicalName();
    private static final String ARG_POINTS = "arg_points";

    @Bind(R.id.result_tv)
    TextView mResultTv;
    @Bind(R.id.result_description_tv)
    TextView mResultDescriptionTv;

    public static COPDPSResultFragment newInstance(int answerPoints) {
        COPDPSResultFragment fragment = new COPDPSResultFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_POINTS, answerPoints);
        fragment.setArguments(args);
        return fragment;
    }

    public COPDPSResultFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_copdps_result, container, false);
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
        mResultDescriptionTv.setText(answerPoints >= 4 ?
                R.string.copdps_result_copd : R.string.copdps_result_no_copd);
    }
}
