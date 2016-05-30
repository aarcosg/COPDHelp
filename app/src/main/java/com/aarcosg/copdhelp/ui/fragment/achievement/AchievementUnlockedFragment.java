package com.aarcosg.copdhelp.ui.fragment.achievement;


import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.Achievement;
import com.aarcosg.copdhelp.service.AchievementsHelper;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.Utils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class AchievementUnlockedFragment extends BaseFragment {

    private static final String TAG = AchievementUnlockedFragment.class.getCanonicalName();

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.achievement_image)
    ImageView mAchievementImage;
    @Bind(R.id.share_btn)
    Button mShareBtn;
    @Bind(R.id.achievement_container)
    LinearLayout mAchievementContainer;

    private long mAchievementId;
    private Subscription mSubscription = Subscriptions.empty();

    public static AchievementUnlockedFragment newInstance(long achievementId) {
        AchievementUnlockedFragment fragment = new AchievementUnlockedFragment();
        Bundle args = new Bundle();
        args.putLong(AchievementsHelper.EXTRA_ID, achievementId);
        fragment.setArguments(args);
        return fragment;
    }

    public AchievementUnlockedFragment() {
        setRetainInstance(true);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null && getArguments().containsKey(AchievementsHelper.EXTRA_ID)) {
            mAchievementId = getArguments().getLong(AchievementsHelper.EXTRA_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_achievement_unlocked, container, false);
        ButterKnife.bind(this, fragmentView);
        setupToolbar();
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSubscription = COPDHelpApplication.get(getContext())
                .getApplicationComponent()
                .getAchievementInteractor()
                .realmFindById(mAchievementId)
                .subscribe(this::bindAchievement);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!mSubscription.isUnsubscribed()) {
            mSubscription.unsubscribe();
        }
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

    @OnClick(R.id.share_btn)
    void onShareButtonClicked(){
        Utils.shareView(getContext(),mAchievementContainer);
    }

    private void setupToolbar() {
        mToolbar.setNavigationIcon(new IconicsDrawable(getContext())
                .icon(GoogleMaterial.Icon.gmd_clear)
                .color(Color.WHITE)
                .sizeDp(16));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void bindAchievement(Achievement achievement) {
        int[] ids = getResources().getIntArray(R.array.medals_ids_full);
        TypedArray ta = getContext().getResources().obtainTypedArray(R.array.medals_drawables_full);
        for(int i = 0; i < ids.length; i++){
            if(ids[i] == achievement.getId()){
                mAchievementImage.setImageResource(ta.getResourceId(i,0));
                break;
            }
        }
        ta.recycle();
    }
}
