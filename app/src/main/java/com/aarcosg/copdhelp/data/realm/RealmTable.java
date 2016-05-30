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
        String INDEX_COPDPS = "indexCOPDPS";
        String INDEX_CAT = "indexCAT";
        String INDEX_BODE = "indexBODE";
        String INDEX_BODEX = "indexBODEx";
        String SCALE_MMRC = "scaleMMRC";
        String SCALE_BORG = "scaleBORG";
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

    interface Exercise{
        String TYPE = "type";
        String DURATION = "duration";
        String NOTE = "note";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }

    interface Achievement{
        String COMPLETED = "completed";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }

    interface Medicine{
        String MEDICINE = "medicine";
        String DOSE = "dose";
        String TIMESTAMP = RealmTable.TIMESTAMP;
        String DAY = RealmTable.DAY;
        String MONTH = RealmTable.MONTH;
        String YEAR = RealmTable.YEAR;
        String DAY_OF_WEEK = RealmTable.DAY_OF_WEEK;
        String WEEK_OF_YEAR = RealmTable.WEEK_OF_YEAR;
    }
}