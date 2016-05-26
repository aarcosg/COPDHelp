package com.aarcosg.copdhelp.ui.fragment.guides;


import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.adapteritem.WhatIsCOPDGuideItem;
import com.aarcosg.copdhelp.ui.decorator.VerticalSpaceItemDecoration;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.fastadapter.adapters.FastItemAdapter;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class WhatIsCOPDMainFragment extends BaseFragment {

    private static final String TAG = WhatIsCOPDMainFragment.class.getCanonicalName();

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private FastItemAdapter<WhatIsCOPDGuideItem> mFastItemAdapter;

    public static WhatIsCOPDMainFragment newInstance() {
        WhatIsCOPDMainFragment fragment = new WhatIsCOPDMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public WhatIsCOPDMainFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_what_is_copd_main, container, false);
        ButterKnife.bind(this, fragmentView);
        setupToolbar();
        setupAdapter();
        setupRecyclerView();
        setHasOptionsMenu(true);
        mFastItemAdapter.add(getGuideItems());
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupAdapter() {
        mFastItemAdapter = new FastItemAdapter<>();
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new VerticalSpaceItemDecoration(
                getResources().getDimension(R.dimen.activity_vertical_margin)));
        mRecyclerView.setAdapter(mFastItemAdapter);
    }

    private List<WhatIsCOPDGuideItem> getGuideItems() {
        return toList(
                new WhatIsCOPDGuideItem().withIdentifier(1L)
                        .withTitle(getString(R.string.what_is_copd_title1))
                        .withLayoutRes(R.layout.fragment_what_is_copd_desc1)
                        .withIsExpanded(false)
        );
    }

    private List<WhatIsCOPDGuideItem> toList(WhatIsCOPDGuideItem... items) {
        return Arrays.asList(items);
    }

}
