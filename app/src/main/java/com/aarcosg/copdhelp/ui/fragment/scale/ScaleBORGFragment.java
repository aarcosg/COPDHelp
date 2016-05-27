package com.aarcosg.copdhelp.ui.fragment.scale;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import com.aarcosg.copdhelp.COPDHelpApplication;
import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.aarcosg.copdhelp.ui.fragment.BaseFragment;
import com.aarcosg.copdhelp.utils.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscription;
import rx.subscriptions.Subscriptions;

public class ScaleBORGFragment extends BaseFragment {

    //TODO add mvp pattern
    private static final String TAG = ScaleBORGFragment.class.getCanonicalName();

    @Bind(R.id.mmrc_grade_spinner)
    Spinner mBorgSpinner;
    @Bind(R.id.save_btn)
    Button mSaveBtn;

    private Subscription mSubscription = Subscriptions.empty();

    public static ScaleBORGFragment newInstance() {
        ScaleBORGFragment fragment = new ScaleBORGFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public ScaleBORGFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_scale_borg, container, false);
        ButterKnife.bind(this, fragmentView);
        return fragmentView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindScaleValue();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
        if(!mSubscription.isUnsubscribed()){
            mSubscription.unsubscribe();
        }
    }

    @OnClick(R.id.save_btn)
    void onSaveButtonClicked(){
        mSubscription = COPDHelpApplication.get(getActivity())
                .getApplicationComponent()
                .getUserInteractor()
                .updateCOPDScaleGrade(1L, RealmTable.User.SCALE_BORG, Double.valueOf((String)mBorgSpinner.getSelectedItem()))
                .subscribe(
                        user -> showSaveSuccessMsg()
                );
    }

    private void bindScaleValue(){
        mSubscription = COPDHelpApplication.get(getActivity())
                .getApplicationComponent()
                .getUserInteractor()
                .realmFindById(1L)
                .subscribe(
                        user -> {
                            if(user != null){
                                mBorgSpinner.setSelection(Utils.getSpinnerIndex(mBorgSpinner,user.getScaleBORG()));
                            }
                        }
                );
    }

    private void showSaveSuccessMsg(){
        Snackbar.make(
                mSaveBtn.getRootView()
                ,R.string.scale_grade_save_realm_success
                ,Snackbar.LENGTH_LONG)
                .show();
    }

}
