package com.aarcosg.copdhelp.ui.fragment.guides;


import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.activity.GuideBreathingTherapyActivity;
import com.aarcosg.copdhelp.ui.activity.GuideWhatIsCOPDActivity;
import com.aarcosg.copdhelp.ui.adapteritem.MainGuideItem;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainGuidesListFragment extends BaseFragment{

    private static final String TAG = MainGuidesListFragment.class.getCanonicalName();
    private static final int GUIDE_WHAT_IS_COPD = 1;
    private static final int GUIDE_BREATHING_THERAPY = 2;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private FastItemAdapter<MainGuideItem> mFastItemAdapter;

    public static MainGuidesListFragment newInstance() {
        MainGuidesListFragment fragment = new MainGuidesListFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MainGuidesListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_main_guides_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        mFastItemAdapter.add(getGuideItems());
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
        mFastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
            switch ((int)item.getIdentifier()){
                case GUIDE_WHAT_IS_COPD:
                    GuideWhatIsCOPDActivity.launch(getActivity());
                    break;
                case GUIDE_BREATHING_THERAPY:
                    GuideBreathingTherapyActivity.launch(getActivity());
                    break;
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mFastItemAdapter);
    }

    private List<MainGuideItem> getGuideItems() {
        return toList(
                new MainGuideItem().withIdentifier(GUIDE_WHAT_IS_COPD)
                        .withName(getString(R.string.what_is_copd))
                        .withImage(ResourcesCompat.getDrawable(getResources(),R.drawable.guide_what_is_copd_header,null)),
                new MainGuideItem().withIdentifier(GUIDE_BREATHING_THERAPY)
                        .withName(getString(R.string.breathing_therapy))
                        .withImage(ResourcesCompat.getDrawable(getResources(),R.drawable.guide_breathing_therapy_header,null))
        );
    }

    private List<MainGuideItem> toList(MainGuideItem... items) {
        return Arrays.asList(items);
    }

}
