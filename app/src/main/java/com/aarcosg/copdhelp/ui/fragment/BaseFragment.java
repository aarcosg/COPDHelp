package com.aarcosg.copdhelp.ui.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.aarcosg.copdhelp.di.HasComponent;
import com.aarcosg.copdhelp.ui.activity.BaseActivity;


public abstract class BaseFragment extends Fragment {

    @SuppressWarnings("unchecked")
    protected <C> C getComponent(Class<C> componentType){
        return componentType.cast(((HasComponent<C>) getActivity()).getComponent());
    }

    protected FragmentManager getSupportFragmentManager(){
        return getActivity().getSupportFragmentManager();
    }

    protected ActionBar getSupportActionBar(){
        return ((BaseActivity)getActivity()).getSupportActionBar();
    }

    protected void setSupportActionBar(Toolbar toolbar){
        ((BaseActivity)getActivity()).setSupportActionBar(toolbar);
    }
}