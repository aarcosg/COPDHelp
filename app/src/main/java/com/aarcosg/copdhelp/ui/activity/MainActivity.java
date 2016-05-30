package com.aarcosg.copdhelp.ui.activity;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.aarcosg.copdhelp.BuildConfig;
import com.aarcosg.copdhelp.Constants;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.di.components.DaggerMainComponent;
import com.aarcosg.copdhelp.di.components.MainComponent;
import com.aarcosg.copdhelp.mvp.presenter.MainPresenter;
import com.aarcosg.copdhelp.mvp.view.MainView;
import com.aarcosg.copdhelp.receiver.RemindersHelper;
import com.aarcosg.copdhelp.service.AchievementsHelper;
import com.aarcosg.copdhelp.service.COPDHelpService;
import com.aarcosg.copdhelp.ui.fragment.AboutFragment;
import com.aarcosg.copdhelp.ui.fragment.achievement.AchievementMainFragment;
import com.aarcosg.copdhelp.ui.fragment.bmi.BMIMainFragment;
import com.aarcosg.copdhelp.ui.fragment.exercise.ExerciseMainFragment;
import com.aarcosg.copdhelp.ui.fragment.guides.MainGuidesListFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionMainFragment;
import com.aarcosg.copdhelp.ui.fragment.medicinereminder.MedicineReminderListFragment;
import com.aarcosg.copdhelp.ui.fragment.mycopd.MyCOPDMainFragment;
import com.aarcosg.copdhelp.ui.fragment.scale.ScaleBORGFragment;
import com.aarcosg.copdhelp.ui.fragment.scale.ScaleMMRCDyspneaFragment;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeMainFragment;
import com.aarcosg.copdhelp.ui.fragment.video.VideoListFragment;
import com.aarcosg.copdhelp.utils.Utils;
import com.mikepenz.community_material_typeface_library.CommunityMaterial;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView, HasComponent<MainComponent>, MyCOPDMainFragment.OnMyCOPDCardSelectedListener {

    private static final String TAG = MainActivity.class.getCanonicalName();
    private static final int MODE_NORMAL_RUN = 0;
    private static final int MODE_FIRST_RUN = 1;
    private static final int MODE_UPGRADE = 2;
    public static final int MEDICAL_ATTENTION_MAIN_ID = 1;
    public static final int BMI_MAIN_ID = 2;
    public static final int MEDICINE_REMINDER_MAIN_ID = 3;
    public static final int SMOKE_MAIN_ID = 4;
    public static final int EXERCISE_MAIN_ID = 5;
    public static final int COPDPS_MAIN_ID = 6;
    public static final int COPDCAT_MAIN_ID = 7;
    public static final int COPDBODE_MAIN_ID = 8;
    public static final int COPDBODEX_MAIN_ID = 9;
    public static final int GUIDES_MAIN_ID = 10;
    public static final int SCALE_MMRC_DYSPNEA_MAIN_ID = 11;
    public static final int SCALE_BORG_MAIN_ID = 12;
    public static final int ACHIEVEMENTS_MAIN_ID = 13;
    public static final int VIDEOS_MAIN_ID = 14;
    public static final int MY_COPD_MAIN_ID = 15;
    private static final int SETTINGS_MAIN_ID = 16;
    private static final int PATIENT_EXPANDABLE_ID = 17;
    private static final int DIAGNOSES_EXPANDABLE_ID = 18;
    private static final int SCALES_EXPANDABLE_ID = 19;
    private static final int ABOUT_APP_MAIN_ID = 20;

    @Inject
    COPDHelpService mCOPDHelpService;
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
        switch (checkFirstRun()){
            case MODE_FIRST_RUN:
                AppIntroActivity.launch(this);
                Utils.getSharedPreferences(this)
                        .edit()
                        .putBoolean(Constants.PROPERTY_GET_DAILY_NOTIFICATIONS,true)
                        .commit();
                finish();
                return;
            case MODE_NORMAL_RUN: break;
            case MODE_UPGRADE: break;
        }
        initializeInjector();
        mMainPresenter.setView(this);
        setupBackgroundService();
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setupToolbar();
        setupNavigationDrawer(savedInstanceState);
        mDrawer.setSelection(MY_COPD_MAIN_ID,true);
        onNewIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra(RemindersHelper.EXTRA_ID) && intent.hasExtra(RemindersHelper.EXTRA_NOTIFICATION_ID)){
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                    .cancel(intent.getIntExtra(RemindersHelper.EXTRA_NOTIFICATION_ID,1));
            mDrawer.setSelection(MEDICINE_REMINDER_MAIN_ID,true);
        } else if (intent.hasExtra(AchievementsHelper.EXTRA_ID) && intent.hasExtra(AchievementsHelper.EXTRA_NOTIFICATION_ID)){
            ((NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE))
                    .cancel(intent.getIntExtra(AchievementsHelper.EXTRA_NOTIFICATION_ID,1));
            mDrawer.setSelection(ACHIEVEMENTS_MAIN_ID,true);
            AchievementActivity.launch(this,intent.getLongExtra(AchievementsHelper.EXTRA_ID,0));
        }
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

    @Override
    public void openFragment(int fragmentId) {
        mDrawer.setSelection(fragmentId,true);
    }

    private void setupToolbar() {
        setSupportActionBar(mToolbar);
        setTitle(R.string.app_name);
    }

    private void setupBackgroundService() {
        if(!mCOPDHelpService.isRunning()){
            mCOPDHelpService.start();
        }
    }

    private void setupNavigationDrawer(final Bundle savedInstanceState) {
        IconicsDrawable headerIcon = new IconicsDrawable(this,"faw_user_md")
                .colorRes(android.R.color.white)
                .backgroundColorRes(R.color.accent);
        AccountHeader accountHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.nav_drawer_header)
                .withProfileImagesClickable(false)
                .withSelectionListEnabled(false)
                .addProfiles(new ProfileDrawerItem().withName(getString(R.string.app_name)).withEmail(getString(R.string.app_name_subtitle)).withIcon(headerIcon))
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
                        new PrimaryDrawerItem().withName(getString(R.string.my_copd)).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(MY_COPD_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.guides)).withIcon(GoogleMaterial.Icon.gmd_local_library).withIdentifier(GUIDES_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.videos)).withIcon(GoogleMaterial.Icon.gmd_video_library).withIdentifier(VIDEOS_MAIN_ID)
                        , new ExpandableDrawerItem().withName(R.string.drawer_item_expandable_patient).withIcon(GoogleMaterial.Icon.gmd_person).withIdentifier(PATIENT_EXPANDABLE_ID).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(getString(R.string.dose_reminder)).withIcon(GoogleMaterial.Icon.gmd_alarm).withIdentifier(MEDICINE_REMINDER_MAIN_ID).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.medical_attention)).withIcon(FontAwesome.Icon.faw_hospital_o).withIdentifier(MEDICAL_ATTENTION_MAIN_ID).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.nutrition)).withIcon(GoogleMaterial.Icon.gmd_restaurant_menu).withIdentifier(BMI_MAIN_ID).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.exercise)).withIcon(GoogleMaterial.Icon.gmd_directions_walk).withIdentifier(EXERCISE_MAIN_ID).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.smoking)).withIcon(GoogleMaterial.Icon.gmd_smoke_free).withIdentifier(SMOKE_MAIN_ID).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.achievements)).withIcon(CommunityMaterial.Icon.cmd_trophy).withIdentifier(ACHIEVEMENTS_MAIN_ID).withLevel(2))
                        , new ExpandableDrawerItem().withName(R.string.drawer_item_expandable_diagnoses).withIcon(CommunityMaterial.Icon.cmd_stethoscope).withIdentifier(DIAGNOSES_EXPANDABLE_ID).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(getString(R.string.copdps)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDPS_MAIN_ID).withSelectable(false).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.copdcat)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDCAT_MAIN_ID).withSelectable(false).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.bode)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDBODE_MAIN_ID).withSelectable(false).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.bodex)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDBODEX_MAIN_ID).withSelectable(false).withLevel(2))
                        , new ExpandableDrawerItem().withName(R.string.drawer_item_expandable_scales).withIcon(CommunityMaterial.Icon.cmd_ruler).withIdentifier(SCALES_EXPANDABLE_ID).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(getString(R.string.scale_mmrc)).withIcon(CommunityMaterial.Icon.cmd_ruler).withIdentifier(SCALE_MMRC_DYSPNEA_MAIN_ID).withLevel(2)
                                , new SecondaryDrawerItem().withName(getString(R.string.scale_borg)).withIcon(CommunityMaterial.Icon.cmd_ruler).withIdentifier(SCALE_BORG_MAIN_ID).withLevel(2))
                        , new PrimaryDrawerItem().withName(getString(R.string.settings)).withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(SETTINGS_MAIN_ID).withSelectable(false)
                        , new PrimaryDrawerItem().withName(getString(R.string.about_application)).withIcon(GoogleMaterial.Icon.gmd_info_outline).withIdentifier(ABOUT_APP_MAIN_ID)
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
        Fragment fragment = null;
        switch (fragmentId){
            case MY_COPD_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.title_fragment_my_copd));
                fragment = MyCOPDMainFragment.newInstance();
                break;
            case MEDICAL_ATTENTION_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_medical_attention));
                fragment = MedicalAttentionMainFragment.newInstance();
                break;
            case BMI_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_nutrition));
                fragment = BMIMainFragment.newInstance();
                break;
            case MEDICINE_REMINDER_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.title_fragment_medicine_reminder));
                fragment = MedicineReminderListFragment.newInstance();
                break;
            case SMOKE_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_smoke));
                fragment = SmokeMainFragment.newInstance();
                break;
            case EXERCISE_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_exercise));
                fragment = ExerciseMainFragment.newInstance();
                break;
            case GUIDES_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.title_fragment_guides));
                fragment = MainGuidesListFragment.newInstance();
                break;
            case ACHIEVEMENTS_MAIN_ID:
                setAppBarElevation(0);
                setTitle(getString(R.string.title_fragment_achievements));
                fragment = AchievementMainFragment.newInstance();
                break;
            case VIDEOS_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.title_fragment_videos));
                fragment = VideoListFragment.newInstance();
                break;
            case SCALE_MMRC_DYSPNEA_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.scale_mmrc_dyspnea_title));
                fragment = ScaleMMRCDyspneaFragment.newInstance();
                break;
            case SCALE_BORG_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.scale_borg_title));
                fragment = ScaleBORGFragment.newInstance();
                break;
            case ABOUT_APP_MAIN_ID:
                setAppBarElevation(getResources().getDimension(R.dimen.toolbar_elevation));
                setTitle(getString(R.string.about_application));
                fragment = AboutFragment.newInstance();
                break;
            case SETTINGS_MAIN_ID:
                SettingsActivity.launch(this);
                break;
            case COPDPS_MAIN_ID:
                COPDPSActivity.launch(this);
                break;
            case COPDCAT_MAIN_ID:
                COPDCATActivity.launch(this);
                break;
            case COPDBODE_MAIN_ID:
                COPDBODEActivity.launch(this);
                break;
            case COPDBODEX_MAIN_ID:
                COPDBODEXActivity.launch(this);
                break;
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

    private int checkFirstRun() {
        final String PREF_VERSION_CODE_KEY = "version_code";
        final int DOESNT_EXIST = -1;
        int res = MODE_NORMAL_RUN;
        int currentVersionCode = BuildConfig.VERSION_CODE;

        // Get saved version code
        SharedPreferences prefs = Utils.getSharedPreferences(this);
        int savedVersionCode = prefs.getInt(PREF_VERSION_CODE_KEY, DOESNT_EXIST);
        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {
            // This is just a normal run
        } else if (savedVersionCode == DOESNT_EXIST) {
            //This is a new install
            res = MODE_FIRST_RUN;
        } else if (currentVersionCode > savedVersionCode) {
            //This is an upgrade
            res = MODE_UPGRADE;
        }
        // Update the shared preferences with the current version code
        prefs.edit().putInt(PREF_VERSION_CODE_KEY, currentVersionCode).commit();
        return res;
    }

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        ActivityCompat.startActivity(activity, intent, null);
    }
}
