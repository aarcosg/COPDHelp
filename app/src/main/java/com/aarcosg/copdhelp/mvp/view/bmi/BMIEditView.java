package com.aarcosg.copdhelp.mvp.view.bmi;

import com.aarcosg.copdhelp.data.realm.entity.BMI;
import com.aarcosg.copdhelp.mvp.view.EditView;

public interface BMIEditView extends EditView<BMI> {

    void bindBMICalcResult(double bmiResult, int bmiStateArrayIndex);

}
