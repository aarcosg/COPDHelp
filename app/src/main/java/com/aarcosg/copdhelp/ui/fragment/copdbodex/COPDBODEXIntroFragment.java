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


public class COPDBODEXIntroFragment extends Fragment {

    private static final String TAG = COPDBODEXIntroFragment.class.getCanonicalName();
    @Bind(R.id.quiz_title_tv)
    TextView mQuizTitleTv;
    @Bind(R.id.quiz_description_tv)
    TextView mQuizDescriptionTv;

    public static COPDBODEXIntroFragment newInstance() {
        COPDBODEXIntroFragment fragment = new COPDBODEXIntroFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public COPDBODEXIntroFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_copdbode_intro, container, false);
        ButterKnife.bind(this, view);
        mQuizTitleTv.setText(getString(R.string.copdbodex_intro_title));
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
