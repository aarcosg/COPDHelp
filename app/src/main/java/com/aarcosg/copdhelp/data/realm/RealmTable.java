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
        String NOTE = "note";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }

    interface BMI{
        String HEIGHT = "height";
        String WEIGHT = "weight";
        String BMI = "bmi";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }

    interface MedicineReminder{
        String ENABLED = "enabled";
        String MEDICINE = "medicine";
        String FREQUENCY = "frequency";
        String DOSE = "dose";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }

    interface Smoke{
        String QUANTITY = "quantity";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }
}