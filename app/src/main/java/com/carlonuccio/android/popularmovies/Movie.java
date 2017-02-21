package com.carlonuccio.android.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by carlonuccio on 31/01/17.
 */

public class Movie implements Parcelable {

    private int ID;
    private String mTitle;
    private String mPosterThumbnail;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;

    public Movie(int ID, String mTitle, String mPosterThumbnail, String mOverview, double mUserRating, String mReleaseDate) {
        this.ID = ID;
        this.mTitle = mTitle;
        this.mPosterThumbnail = mPosterThumbnail;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }

    public int getID() {
        return ID;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmPosterThumbnail() {
        return mPosterThumbnail;
    }

    public double getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.ID);
        dest.writeString(this.mTitle);
        dest.writeString(this.mPosterThumbnail);
        dest.writeString(this.mOverview);
        dest.writeDouble(this.mUserRating);
        dest.writeString(this.mReleaseDate);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.ID = in.readInt();
        this.mTitle = in.readString();
        this.mPosterThumbnail = in.readString();
        this.mOverview = in.readString();
        this.mUserRating = in.readDouble();
        this.mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
