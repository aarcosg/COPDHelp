package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.di.components.DaggerMainComponent;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.view.MainView;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIMainFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionMainFragment;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView, HasComponent<MainComponent> {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int MEDICAL_ATTENTION_MAIN_ID = 1;
    private static final int BMI_MAIN_ID = 2;

    @Inject
    MainPresenter mMainPresenter;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.appbar)
    AppBarLayout mAppBarLayout;

    private MainComponent mMainComponent;
    private Drawer mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeInjector();
        mMainPresenter.setView(this);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupNavigationDrawer(savedInstanceState);
        selectDrawerItem(MEDICAL_ATTENTION_MAIN_ID);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState = mDrawer.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public MainComponent getComponent() {
        return mMainComponent;
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);
    }

    private void setupNavigationDrawer(final Bundle savedInstanceState) {
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_drawer_header)
                .withSavedInstance(savedInstanceState)
                .build();
        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withSavedInstance(savedInstanceState)
                .withHasStableIds(true)
                .withDisplayBelowStatusBar(false)
                .withTranslucentStatusBar(true)
                .withAccountHeader(accountHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.medical_attention)).withIcon(GoogleMaterial.Icon.gmd_local_hospital).withIdentifier(MEDICAL_ATTENTION_MAIN_ID)
                        ,new PrimaryDrawerItem().withName(getString(R.string.bmi)).withIcon(CommunityMaterial.Icon.cmd_scale_bathroom).withIdentifier(BMI_MAIN_ID)
                )
                .withOnDrawerItemClickListener((view, position, drawerItem) -> {
                    if(drawerItem != null){
                        selectDrawerItem((int)drawerItem.getIdentifier());
                    }
                    return false;
                })
                .build();
    }

    private boolean selectDrawerItem(int fragmentId) {
        Fragment fragment;
        switch (fragmentId){
            case MEDICAL_ATTENTION_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_medical_attention));
                fragment = MedicalAttentionMainFragment.newInstance();
                break;
            case BMI_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_bmi));
                fragment = BMIMainFragment.newInstance();
                break;
            default:
                setTitle(getString(R.string.title_fragment_medical_attention));
                fragment = MedicalAttentionMainFragment.newInstance();
        }

        if(fragment != null){
            replaceFragment(R.id.fragment_container,fragment);
        }

        return false;
    }

    private void initializeInjector() {
        mMainComponent = DaggerMainComponent.builder()
                .applicationComponent(getApplicationComponent())
                .activityModule(getActivityModule())
                .build();
        mMainComponent.inject(this);
    }

    private void setAppBarElevation(float elevation){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppBarLayout.setElevation(elevation);
        }
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
