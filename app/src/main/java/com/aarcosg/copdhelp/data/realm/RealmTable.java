package com.aarcosg.copdhelp.data.realm;

public interface RealmTable {

    String ID = "id";

    interface User {
    }

    interface MedicalAttention{
        String TYPE = "type";
        String TIMESTAMP = "timestamp";
        String NOTE = "note";
        String DAY = "day";
        String MONTH = "month";
        String YEAR = "year";
        String DAY_OF_WEEK = "dayOfWeek";
        String WEEK_OF_YEAR = "weekOfYear";
    }
}