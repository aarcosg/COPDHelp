package com.aarcosg.copdhelp.ui.fragment.achievement;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.activity.AchievementActivity;
import com.aarcosg.copdhelp.ui.adapteritem.AchievementItem;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AchievementListFragment extends BaseFragment {

    private static final String TAG = AchievementListFragment.class.getCanonicalName();
    private static final String ARG_MEDALS = "arg_medals";

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private List<AchievementItem> mAchievementItems;
    private FastItemAdapter<AchievementItem> mFastItemAdapter;

    public static AchievementListFragment newInstance(ArrayList<AchievementItem> medals) {
        AchievementListFragment fragment = new AchievementListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_MEDALS, medals);
        fragment.setArguments(args);
        return fragment;
    }

    public AchievementListFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null && getArguments().containsKey(ARG_MEDALS))
            mAchievementItems = getArguments().getParcelableArrayList(ARG_MEDALS);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_achievement_list, container, false);
        ButterKnife.bind(this, fragmentView);
        setupAdapter();
        setupRecyclerView();
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFastItemAdapter.add(mAchievementItems);
        mFastItemAdapter.notifyAdapterDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
        mFastItemAdapter.withOnClickListener((v, adapter, item, position) -> {
            if(item.mCompleted){
                AchievementActivity.launch(getActivity(),item.mAchievementId);
            }
            return false;
        });
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),2));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mFastItemAdapter);
    }

}
