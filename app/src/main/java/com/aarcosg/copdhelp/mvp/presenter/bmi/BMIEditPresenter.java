package com.aarcosg.copdhelp.mvp.presenter.bmi;

import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.mvp.presenter.EditPresenter;

public interface BMIEditPresenter extends EditPresenter<BMI> {

    void onHeightOrWeightChanged(String height, String weight);

    void loadLastHeightSaved();
}
