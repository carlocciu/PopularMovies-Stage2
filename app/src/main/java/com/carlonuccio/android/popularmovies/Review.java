package com.carlonuccio.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by carlonuccio on 21/02/17.
 */

public class Review implements Parcelable {

    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
    private String mAuthor;
    private String mContent;

    public Review(String mAuthor, String mContent) {
        this.mAuthor = mAuthor;
        this.mContent = mContent;
    }

    protected Review(Parcel in) {
        this.mAuthor = in.readString();
        this.mContent = in.readString();
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAuthor);
        dest.writeString(this.mContent);
    }
}
