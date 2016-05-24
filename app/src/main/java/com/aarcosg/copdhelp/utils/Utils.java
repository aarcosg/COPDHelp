package com.aarcosg.copdhelp.utils;


import android.Manifest;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aarcosg.copdhelp.R;
import com.aarcosg.copdhelp.data.realm.RealmTable;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.RealmResults;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class Utils {

    private static final String TAG = Utils.class.getCanonicalName();

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

    public static int getSpinnerIndex(Spinner spinner, Integer value) {
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (Integer.valueOf(spinner.getItemAtPosition(i).toString()).equals(value)){
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

    public static Double calculatePercentageChange(double oldValue, double newValue){
        return oldValue == 0 ? 0 : (newValue - oldValue) / oldValue * 100;
    }

    public static Double getCountPercentageChange(int calendarField, RealmResults realmResults){
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

    public static Double getAveragePercentageChange(int calendarField, String realmField, RealmResults realmResults){
        Calendar calendar = Calendar.getInstance();
        String tableField;
        switch (calendarField){
            case Calendar.WEEK_OF_YEAR: tableField = RealmTable.WEEK_OF_YEAR; break;
            case Calendar.MONTH: tableField = RealmTable.MONTH; break;
            case Calendar.YEAR: tableField = RealmTable.YEAR; break;
            default: tableField = RealmTable.WEEK_OF_YEAR;
        }
        double previousAvg = realmResults.where()
                .equalTo(tableField,calendar.get(calendarField) - 1)
                .average(realmField);
        double currentAvg = realmResults.where()
                .equalTo(tableField,calendar.get(calendarField))
                .average(realmField);
        return calculatePercentageChange(previousAvg,currentAvg);
    }

    public static Double getSumPercentageChange(int calendarField, String realmField, RealmResults realmResults){
        Calendar calendar = Calendar.getInstance();
        String tableField;
        switch (calendarField){
            case Calendar.WEEK_OF_YEAR: tableField = RealmTable.WEEK_OF_YEAR; break;
            case Calendar.MONTH: tableField = RealmTable.MONTH; break;
            case Calendar.YEAR: tableField = RealmTable.YEAR; break;
            default: tableField = RealmTable.WEEK_OF_YEAR;
        }
        double previousAvg = realmResults.where()
                .equalTo(tableField,calendar.get(calendarField) - 1)
                .sum(realmField).doubleValue();
        double currentAvg = realmResults.where()
                .equalTo(tableField,calendar.get(calendarField))
                .sum(realmField).doubleValue();
        return calculatePercentageChange(previousAvg,currentAvg);
    }

    public static void animateNumberTextView(int initialValue, int finalValue, final TextView textView, final String format) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(2000);
        valueAnimator.addUpdateListener(animation ->
                textView.setText(String.format(format,valueAnimator.getAnimatedValue().toString())));
        valueAnimator.start();
    }

    public static String getDateTimeStamp(){
        return new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    }

    public static void shareView(Context context, View view) {
        RxPermissions.getInstance(context)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        .subscribeOn(Schedulers.immediate())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(granted -> {
            view.setDrawingCacheEnabled(true);
            view.buildDrawingCache(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            view.setDrawingCacheEnabled(false);
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, bytes);
            String pathName = Environment.getExternalStorageDirectory()
                    + File.separator + context.getString(R.string.app_name);
            String fileName = "image_" + Utils.getDateTimeStamp() + ".jpg";
            File path = new File(pathName);
            File file = new File(pathName + File.separator + fileName);
            try {
                if(!path.exists()){
                    path.mkdirs();
                }
                if (file.createNewFile()) {
                    FileOutputStream fos = new FileOutputStream(file);
                    fos.write(bytes.toByteArray());
                    fos.close();

                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/jpeg");
                    share.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                    context.startActivity(Intent.createChooser(share
                            , context.getString(R.string.share_image)));
                }
            } catch (IOException e) {
                Log.e(TAG,e.getLocalizedMessage());
                Toast.makeText(context
                        ,context.getString(R.string.exception_message_share_image_error)
                        ,Toast.LENGTH_LONG)
                        .show();
            } finally {
                file.deleteOnExit();
            }
        });
    }

    public static double calculateBMI(double height, double weight){
        double result = 0;
        if(height > 0 && weight > 0){
            result = weight / Math.pow(height / 100, 2);
        }
        return result;
    }

    public static int getBMIStateArrayIndex(double bmi){
        int index = 0;
        if(bmi < 21){
            index = 1;
        }else if(bmi >= 21 && bmi < 25){
            index = 2;
        }else if(bmi >= 25 && bmi < 30){
            index = 3;
        }else if(bmi >= 30){
            index = 4;
        }
        return index;
    }

    public static int sum(List<Integer> ints){
        int sum = 0;
        for(Integer i : ints){
            sum += i;
        }
        return sum;
    }
}
