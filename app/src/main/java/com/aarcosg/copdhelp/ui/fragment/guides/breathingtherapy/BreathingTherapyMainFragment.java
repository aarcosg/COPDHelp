package com.aarcosg.copdhelp.ui.fragment.guides.breathingtherapy;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.ui.fragment.guides.GuideContentFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BreathingTherapyMainFragment extends BaseFragment {

    private static final String TAG = BreathingTherapyMainFragment.class.getCanonicalName();

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private PagerAdapter mPagerAdapter;

    public static BreathingTherapyMainFragment newInstance() {
        BreathingTherapyMainFragment fragment = new BreathingTherapyMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BreathingTherapyMainFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_breathing_therapy_main, container, false);
        ButterKnife.bind(this, fragmentView);
        setupToolbar();
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setHasOptionsMenu(true);
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

    private class PagerAdapter extends FragmentPagerAdapter {

        private final String[] tabTitles = getResources().getStringArray(R.array.breathing_therapy_tab_titles);

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_breathing_therapy_intro);
                    break;
                case 1:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_breathing_therapy_recommendations);
                    break;
                case 2:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_breathing_therapy_walk);
                    break;
                case 3:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_breathing_therapy_bicycle);
                    break;
                case 4:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_breathing_therapy_breathing);
                    break;
                case 5:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_breathing_therapy_arms);
                    break;
            }
            return fragment;
        }
    }

}
