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
import com.aarcosg.copdhelp.ui.fragment.exercise.ExerciseMainFragment;
import com.aarcosg.copdhelp.ui.fragment.medicalattention.MedicalAttentionMainFragment;
import com.aarcosg.copdhelp.ui.fragment.medicinereminder.MedicineReminderListFragment;
import com.aarcosg.copdhelp.ui.fragment.smoke.SmokeMainFragment;
import com.aarcosg.copdhelp.ui.receiver.Reminders;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
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
    private static final int MEDICINE_REMINDER_MAIN_ID = 3;
    private static final int SMOKE_MAIN_ID = 4;
    private static final int EXERCISE_MAIN_ID = 5;
    private static final int COPDPS_MAIN_ID = 6;
    private static final int COPDCAT_MAIN_ID = 7;
    private static final int COPDBODE_MAIN_ID = 8;
    private static final int COPDBODEX_MAIN_ID = 9;

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
        if(getIntent().hasExtra(Reminders.EXTRA_ID)){
            mDrawer.setSelection(MEDICINE_REMINDER_MAIN_ID,true);
        }else{
            mDrawer.setSelection(MEDICAL_ATTENTION_MAIN_ID,true);
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
                        //new SectionDrawerItem().withName(R.string.drawer_item_section_patient),
                        new PrimaryDrawerItem().withName(getString(R.string.medical_attention)).withIcon(FontAwesome.Icon.faw_hospital_o).withIdentifier(MEDICAL_ATTENTION_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.nutrition)).withIcon(GoogleMaterial.Icon.gmd_restaurant_menu).withIdentifier(BMI_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.dose_reminder)).withIcon(GoogleMaterial.Icon.gmd_alarm).withIdentifier(MEDICINE_REMINDER_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.smoking)).withIcon(GoogleMaterial.Icon.gmd_smoke_free).withIdentifier(SMOKE_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.exercise)).withIcon(GoogleMaterial.Icon.gmd_directions_walk).withIdentifier(EXERCISE_MAIN_ID)
                        , new PrimaryDrawerItem().withName(getString(R.string.copdps)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDPS_MAIN_ID).withSelectable(false)
                        , new PrimaryDrawerItem().withName(getString(R.string.copdcat)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDCAT_MAIN_ID).withSelectable(false)
                        , new PrimaryDrawerItem().withName(getString(R.string.bode)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDBODE_MAIN_ID).withSelectable(false)
                        , new PrimaryDrawerItem().withName(getString(R.string.bodex)).withIcon(FontAwesome.Icon.faw_user_md).withIdentifier(COPDBODEX_MAIN_ID).withSelectable(false)
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
