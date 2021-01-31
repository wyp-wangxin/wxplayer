package com.wyp.wxplayer.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wschun on 2016/9/29.
 */

public class Peopler implements Parcelable{
    private String name;
    private int age;

    public Peopler(String name, int age) {
        this.name = name;
        this.age = age;
    }

    protected Peopler(Parcel in) {
        name = in.readString();
        age = in.readInt();
    }

    public static final Creator<Peopler> CREATOR = new Creator<Peopler>() {
        @Override
        public Peopler createFromParcel(Parcel in) {
            return new Peopler(in);
        }

        @Override
        public Peopler[] newArray(int size) {
            return new Peopler[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(name);
        dest.writeInt(age);
    }
}
