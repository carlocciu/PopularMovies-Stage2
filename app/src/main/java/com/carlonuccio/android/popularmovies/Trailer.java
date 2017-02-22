package com.carlonuccio.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by carlonuccio on 21/02/17.
 */

public class Trailer implements Parcelable {

    private String mName;
    private String mKey;
    private String mSite;

    public Trailer(String mName, String mKey, String mSite) {
        this.mName = mName;
        this.mKey = mKey;
        this.mSite = mSite;
    }

    public String getmName() {
        return mName;
    }

    public String getmKey() {
        return mKey;
    }

    public String getmSite() {
        return mSite;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mName);
        dest.writeString(this.mKey);
        dest.writeString(this.mSite);
    }

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        this.mName = in.readString();
        this.mKey = in.readString();
        this.mSite = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
