package com.aarcosg.copdhelp.utils;


import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.TextView;

import com.aarcosg.copdhelp.data.realm.RealmTable;

import java.util.Calendar;

import io.realm.RealmResults;

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

    public static Double calculatePercentageChange(long oldValue, long newValue){
        return oldValue == 0 ? 0 : (double)(newValue - oldValue) / oldValue * 100;
    }

    public static Double getPercentageChange(int calendarField, RealmResults realmResults){
        Calendar calendar = Calendar.getInstance();
        String tableField;
        switch (calendarField){
            case Calendar.WEEK_OF_YEAR: tableField = RealmTable.WEEK_OF_YEAR; break;
            case Calendar.MONTH: tableField = RealmTable.MONTH; break;
            case Calendar.YEAR: tableField = RealmTable.YEAR; break;
            default: tableField = RealmTable.WEEK_OF_YEAR;
        }
        long previousCounter = realmResults.where()
                .equalTo(tableField,calendar.get(calendarField) - 1)
                .count();
        long currentCounter = realmResults.where()
                .equalTo(tableField,calendar.get(calendarField))
                .count();
        return calculatePercentageChange(previousCounter,currentCounter);
    }

    public static void animateNumberTextView(int initialValue, int finalValue, final TextView textView, final String format) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animation ->
                textView.setText(String.format(format,valueAnimator.getAnimatedValue().toString())));
        valueAnimator.start();
    }
}
