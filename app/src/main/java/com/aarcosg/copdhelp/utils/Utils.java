package com.aarcosg.copdhelp.utils;


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

public class Utils {

    public static void hideKeyboard(final View view){
        view.post(() -> {
            InputMethodManager inputManager = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        });
    }

    public static int getSpinnerIndex(Spinner spinner, String value) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(value)){
                index = i;
                break;
            }
        }
        return index;
    }
}
