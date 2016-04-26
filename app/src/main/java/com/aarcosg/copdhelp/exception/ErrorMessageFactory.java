package com.aarcosg.copdhelp.exception;

import android.content.Context;

import com.aarcosg.copdhelp.R;

public class ErrorMessageFactory {

    private ErrorMessageFactory() {}

    public static String create(Context context, Throwable throwable) {
        String message = context.getString(R.string.exception_message_generic);

        if (throwable instanceof InternetNotAvailableException) {
            message = context.getString(R.string.exception_message_internet_not_available);
        }

        return message;
    }
}