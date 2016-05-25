package com.aarcosg.copdhelp.ui.fragment.copdbode;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;


public class COPDBODEIntroFragment extends Fragment{

    private static final String TAG = COPDBODEIntroFragment.class.getCanonicalName();

    public static COPDBODEIntroFragment newInstance() {
        COPDBODEIntroFragment fragment = new COPDBODEIntroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public COPDBODEIntroFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_copdbode_intro, container, false);
    }

}
