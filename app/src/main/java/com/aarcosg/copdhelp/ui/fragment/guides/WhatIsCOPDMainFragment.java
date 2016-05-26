package com.aarcosg.copdhelp.ui.fragment.guides;


import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardExpand;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.recyclerview.internal.CardArrayRecyclerViewAdapter;
import it.gmariotti.cardslib.library.recyclerview.view.CardRecyclerView;

public class WhatIsCOPDMainFragment extends BaseFragment {

    private static final String TAG = WhatIsCOPDMainFragment.class.getCanonicalName();

    @Bind(R.id.recycler_view)
    CardRecyclerView mRecyclerView;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private CardArrayRecyclerViewAdapter mCardArrayAdapter;

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
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mCardArrayAdapter.addAll(getCards());
        mCardArrayAdapter.notifyDataSetChanged();
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
        getSupportActionBar().setTitle(R.string.what_is_copd);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupAdapter() {
        mCardArrayAdapter = new CardArrayRecyclerViewAdapter(getContext(),new ArrayList<Card>());
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mCardArrayAdapter);
    }

    private List<Card> getCards() {
        List<Card> cards = Arrays.asList(
                createCard(R.string.what_is_copd_title1,R.layout.card_expand_what_is_copd_desc1),
                createCard(R.string.what_is_copd_title2,R.layout.card_expand_what_is_copd_desc2),
                createCard(R.string.what_is_copd_title3,R.layout.card_expand_what_is_copd_desc3),
                createCard(R.string.what_is_copd_title4,R.layout.card_expand_what_is_copd_desc4),
                createCard(R.string.what_is_copd_title5,R.layout.card_expand_what_is_copd_desc5),
                createCard(R.string.what_is_copd_title6,R.layout.card_expand_what_is_copd_desc6)
        );
        return cards;
    }

    private Card createCard(@StringRes int titleTextId,@LayoutRes int descriptionLayoutId){
        Card card = new Card(getActivity());
        CardHeader header = new CardHeader(getActivity());
        header.setTitle(getString(titleTextId));
        header.setButtonExpandVisible(true);
        card.addCardHeader(header);
        CardExpand expand = new CardExpand(getActivity(),descriptionLayoutId);
        card.addCardExpand(expand);
        return card;
    }

}
