package com.aarcosg.copdhelp.data.api.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class MedicalAttentionEntity implements Parcelable {

    protected MedicalAttentionEntity(Parcel in) {
    }

    public static final Creator<MedicalAttentionEntity> CREATOR = new Creator<MedicalAttentionEntity>() {
        @Override
        public MedicalAttentionEntity createFromParcel(Parcel in) {
            return new MedicalAttentionEntity(in);
        }

        @Override
        public MedicalAttentionEntity[] newArray(int size) {
            return new MedicalAttentionEntity[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
