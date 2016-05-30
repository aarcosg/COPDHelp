package com.aarcosg.copdhelp.ui.fragment.achievement;


import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.data.realm.entity.Achievement;
import com.aarcosg.copdhelp.ui.adapteritem.AchievementItem;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class AchievementMainFragment extends BaseFragment {

    //TODO add mvp pattern

    private static final String TAG = AchievementMainFragment.class.getCanonicalName();

    @Bind(R.id.tabs)
    TabLayout mTabLayout;
    @Bind(R.id.viewpager)
    ViewPager mViewPager;

    private PagerAdapter mPagerAdapter;
    private Subscription mSubscription = Subscriptions.empty();
    private RealmResults<Achievement> mAchievements;

    public static AchievementMainFragment newInstance() {
        AchievementMainFragment fragment = new AchievementMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AchievementMainFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_achievement_main, container, false);
        ButterKnife.bind(this, fragmentView);
        mSubscription = COPDHelpApplication.get(getContext())
                .getApplicationComponent()
                .getAchievementInteractor()
                .realmFindAll(null
                        , RealmTable.Achievement.TIMESTAMP
                        , Sort.DESCENDING)
                .subscribe(achievements -> mAchievements = achievements);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPagerAdapter = new PagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    private class PagerAdapter extends FragmentPagerAdapter {

        private final String[] tabTitles = getResources().getStringArray(R.array.achievement_tab_titles);

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
                    fragment = AchievementListFragment.newInstance(getExerciseMedals());
                    break;
                case 1:
                    fragment = AchievementListFragment.newInstance(getNoSmokingMedals());
                    break;
            }
            return fragment;
        }

    }

    private ArrayList<AchievementItem> getExerciseMedals(){
        ArrayList<AchievementItem> medals = new ArrayList<>();
        int[] ids = getResources().getIntArray(R.array.medals_ids_exercise);
        TypedArray ta = getContext().getResources().obtainTypedArray(R.array.medals_drawables_exercise);
        for(int i = 0; i < ids.length; i++){
            medals.add(new AchievementItem(ids[i],ta.getResourceId(i, 0),false,null));
        }
        ta.recycle();
        for(Achievement achievement : mAchievements){
            for(AchievementItem medal: medals){
                if(achievement.getId() == medal.mAchievementId){
                    medal.mCompleted = true;
                    medal.mState = DateUtils.formatDateTime(getContext()
                            ,achievement.getTimestamp().getTime()
                            ,DateUtils.FORMAT_SHOW_DATE);
                }
            }
        }
        return medals;
    }

    private ArrayList<AchievementItem> getNoSmokingMedals(){
        ArrayList<AchievementItem> medals = new ArrayList<>();
        int[] ids = getResources().getIntArray(R.array.medals_ids_no_smooking);
        TypedArray ta = getContext().getResources().obtainTypedArray(R.array.medals_drawables_no_smooking);
        for(int i = 0; i < ids.length; i++){
            medals.add(new AchievementItem(ids[i],ta.getResourceId(i, 0),false,null));
        }
        ta.recycle();
        for(Achievement achievement : mAchievements){
            for(AchievementItem medal: medals){
                if(achievement.getId() == medal.mAchievementId){
                    medal.mCompleted = true;
                    medal.mState = DateUtils.formatDateTime(getContext()
                            ,achievement.getTimestamp().getTime()
                            ,DateUtils.FORMAT_SHOW_DATE
                                    | DateUtils.FORMAT_SHOW_YEAR
                                    | DateUtils.FORMAT_ABBREV_MONTH
                    );
                }
            }
        }
        return medals;
    }

}
