package com.aarcosg.copdhelp.ui.fragment.bmi;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class BMIMainFragment extends BaseFragment {

    private static final String TAG = BMIMainFragment.class.getCanonicalName();

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;

    public static BMIMainFragment newInstance() {
        BMIMainFragment fragment = new BMIMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public BMIMainFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_bmi_main, container, false);
        ButterKnife.bind(this, fragmentView);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout){
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupTabLayoutIcons(position);
            }
        });
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setupTabLayoutIcons(0);
        return fragmentView;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setupTabLayoutIcons(int position){
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            IconicsDrawable iconDrawable = new IconicsDrawable(getContext())
                    .icon(mPagerAdapter.getTabIconAt(i))
                    .sizeDp(24);
            if(i == position){
                iconDrawable.color(ContextCompat.getColor(getContext(),R.color.icon_tab_selected));
            }else{
                iconDrawable.color(ContextCompat.getColor(getContext(),R.color.icon_tab_unselected));
            }
            mTabLayout.getTabAt(i).setIcon(iconDrawable);
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final String[] tabTitles = getResources().getStringArray(R.array.bmi_tab_titles);
        private final String[] tabIcons = getResources().getStringArray(R.array.bmi_tab_icons);

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
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
                    fragment = BMIListFragment.newInstance();
                    break;
                case 1:
                    fragment = BMIChartFragment.newInstance();
                    break;
            }
            return fragment;
        }

        public String getTabIconAt(int position) {
            return tabIcons[position];
        }
    }

}
