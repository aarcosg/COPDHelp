package com.aarcosg.copdhelp.ui.fragment.copdcat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;


public class COPDCATIntroFragment extends Fragment{

    private static final String TAG = COPDCATIntroFragment.class.getCanonicalName();

    public static COPDCATIntroFragment newInstance() {
        COPDCATIntroFragment fragment = new COPDCATIntroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public COPDCATIntroFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_copdcat_intro, container, false);
    }

}
