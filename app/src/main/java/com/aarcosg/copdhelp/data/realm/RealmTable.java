package com.aarcosg.copdhelp.data.realm;

public interface RealmTable {

    String ID = "id";
    String TIMESTAMP = "timestamp";
    String DAY = "day";
    String MONTH = "month";
    String YEAR = "year";
    String DAY_OF_WEEK = "dayOfWeek";
    String WEEK_OF_YEAR = "weekOfYear";

    interface User {
    }

    interface MedicalAttention{
        String TYPE = "type";
        String TIMESTAMP = "timestamp";
        String NOTE = "note";
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }
}