package com.aarcosg.copdhelp.ui.fragment.copdps;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;


public class COPDPSIntroFragment extends Fragment{

    private static final String TAG = COPDPSIntroFragment.class.getCanonicalName();

    public static COPDPSIntroFragment newInstance() {
        COPDPSIntroFragment fragment = new COPDPSIntroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public COPDPSIntroFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_copdps_intro, container, false);
    }

}
