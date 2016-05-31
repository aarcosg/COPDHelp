package com.aarcosg.copdhelp.ui.fragment;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aarcosg.copdhelp.Constants;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.entity.User;
import com.aarcosg.copdhelp.ui.activity.SettingsActivity;
import com.aarcosg.copdhelp.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;

public class SettingsFragment extends PreferenceFragment {

    private static final String TAG = SettingsFragment.class.getCanonicalName();
    private static final String CATEGORY_PATIENT = "category_patient";

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    private SharedPreferences mPrefs;
    private Realm mRealm;
    private User mUser;
    private PreferenceCategory mPatientCategory;
    private EditTextPreference mAgePreference;
    private ListPreference mGenrePreference;
    private SwitchPreference mSendDataPreference;
    private SwitchPreference mDailyFormNotificationsPreference;
    private SwitchPreference mIsSmokerPreference;
    private EditTextPreference mSmokingYearsPreference;

    public static SettingsFragment newInstance() {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPrefs = Utils.getSharedPreferences(getActivity().getApplicationContext());
        mRealm = getRealm();
        mUser = mRealm.where(User.class).findFirst();

        addPreferencesFromResource(R.xml.preferences);

        mPatientCategory = (PreferenceCategory)findPreference(CATEGORY_PATIENT);

        mAgePreference = (EditTextPreference)findPreference(Constants.PROPERTY_AGE);
        mAgePreference.setSummary(mPrefs.getString(Constants.PROPERTY_AGE,getString(R.string.not_registered)));
        mAgePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            preference.setSummary(newValue.toString()+ " " + getString(R.string.years));
            getRealm().beginTransaction();
            mUser.setAge(Integer.valueOf(newValue.toString()));
            getRealm().commitTransaction();
            return true;
        });

        mGenrePreference = (ListPreference)findPreference(Constants.PROPERTY_GENRE);
        String genreSummary = mPrefs.getString(Constants.PROPERTY_GENRE,getString(R.string.not_registered));
        if(!genreSummary.equals(getString(R.string.not_registered))){
            genreSummary = getResources().getStringArray(R.array.pref_genre_list_entries)[Integer.valueOf(genreSummary)];
        }
        mGenrePreference.setSummary(genreSummary);

        mGenrePreference.setOnPreferenceChangeListener((preference, newValue) -> {
            int genre = Integer.valueOf(newValue.toString());
            preference.setSummary(getResources().getStringArray(R.array.pref_genre_list_entries)[genre]);
            getRealm().beginTransaction();
            mUser.setGenre(genre);
            getRealm().commitTransaction();
            return true;
        });

        mIsSmokerPreference = (SwitchPreference) findPreference(Constants.PROPERTY_IS_SMOKER);
        mIsSmokerPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean isSmoker = Boolean.valueOf(newValue.toString());
            getRealm().beginTransaction();
            mUser.setSmoker(isSmoker);
            getRealm().commitTransaction();
            return true;
        });

        mSmokingYearsPreference = (EditTextPreference)findPreference(Constants.PROPERTY_SMOKING_YEARS);
        mSmokingYearsPreference.setSummary(mPrefs.getString(Constants.PROPERTY_SMOKING_YEARS,"0" + " " + getString(R.string.years)));
        mSmokingYearsPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            int years = 0;
            try{
                years = Integer.valueOf(newValue.toString());
            }catch (NumberFormatException e){
                years = 0;
            }
            preference.setSummary(years + " " + getString(R.string.years));
            getRealm().beginTransaction();
            mUser.setSmokingYears(years);
            getRealm().commitTransaction();
            return true;
        });

        mSendDataPreference = (SwitchPreference) findPreference(Constants.PROPERTY_SEND_DATA);

        mDailyFormNotificationsPreference = (SwitchPreference) findPreference(Constants.PROPERTY_GET_DAILY_NOTIFICATIONS);

        findPreference(Constants.PROPERTY_APP_VERSION).setSummary("v. " + Utils.getAppVersion(getActivity().getApplicationContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, fragmentView);
        setupToolbar();
        setHasOptionsMenu(true);
        return fragmentView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mRealm != null){
            mRealm.close();
        }
    }

    private void setupToolbar(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(getResources().getDimension(R.dimen.toolbar_elevation));
        }
        SettingsActivity activity = (SettingsActivity) getActivity();
        activity.setSupportActionBar(mToolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private Realm getRealm(){
        if(mRealm == null || mRealm.isClosed()){
            mRealm = Realm.getDefaultInstance();
        }
        return mRealm;
    }

}
