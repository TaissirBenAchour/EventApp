package com.example.cassio.Graduation_Project.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cassio on 28/01/18.
 */

public class Committee implements Parcelable {
    String test ;

    public Committee(String test) {
        this.test = test;
    }

    protected Committee(Parcel in) {
        test = in.readString();
    }

    public static final Creator<Committee> CREATOR = new Creator<Committee>() {
        @Override
        public Committee createFromParcel(Parcel in) {
            return new Committee(in);
        }

        @Override
        public Committee[] newArray(int size) {
            return new Committee[size];
        }
    };

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Committee() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(test);
    }
}
