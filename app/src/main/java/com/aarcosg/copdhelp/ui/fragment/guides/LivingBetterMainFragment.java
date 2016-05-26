package com.aarcosg.copdhelp.ui.fragment.guides;


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

import butterknife.Bind;
import butterknife.ButterKnife;

public class LivingBetterMainFragment extends BaseFragment {

    private static final String TAG = LivingBetterMainFragment.class.getCanonicalName();

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private PagerAdapter mPagerAdapter;

    public static LivingBetterMainFragment newInstance() {
        LivingBetterMainFragment fragment = new LivingBetterMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public LivingBetterMainFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_living_better_main, container, false);
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

        private final String[] tabTitles = getResources().getStringArray(R.array.living_better_tab_titles);

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
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_living_better_smoking);
                    break;
                case 1:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_living_better_nutrition);
                    break;
                case 2:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_living_better_exercise);
                    break;
                case 3:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_living_better_vaccine);
                    break;
                case 4:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_living_better_medication);
                    break;
                case 5:
                    fragment = GuideContentFragment.newInstance(R.layout.fragment_living_better_getting_worse);
                    break;
            }
            return fragment;
        }
    }

}
