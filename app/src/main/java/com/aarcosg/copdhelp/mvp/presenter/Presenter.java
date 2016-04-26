package com.aarcosg.copdhelp.mvp.presenter;

import com.aarcosg.copdhelp.mvp.view.View;

public interface Presenter {

    void setView(View v);
    void onPause();

}