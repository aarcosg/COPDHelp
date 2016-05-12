package com.aarcosg.copdhelp.utils;


import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;

import java.util.Calendar;

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

    public static Calendar getFirstDayOfWeek(int weekOfYear){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.WEEK_OF_YEAR,weekOfYear);
        calendar.set(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.getFirstDayOfWeek(),0,0,0);
        return calendar;
    }

    public static Calendar getFirstDayOfCurrentWeek(){
        return getFirstDayOfWeek(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
    }

    public static Calendar getLastDayOfWeek(int weekOfYear){
        Calendar calendar = getFirstDayOfWeek(weekOfYear);
        calendar.add(Calendar.DATE,6);
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);
        calendar.set(Calendar.SECOND,59);
        return calendar;
    }

    public static Calendar getLastDayOfCurrentWeek(){
        return getLastDayOfWeek(Calendar.getInstance().get(Calendar.WEEK_OF_YEAR));
    }
}
