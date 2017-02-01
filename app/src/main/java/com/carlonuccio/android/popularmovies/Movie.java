package com.carlonuccio.android.popularmovies;

import java.io.Serializable;

/**
 * Created by carlonuccio on 31/01/17.
 */

public class Movie implements Serializable {

    private String mTitle;
    private String mPosterThumbnail;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;
    private double mPopularity;

    public Movie(String mTitle, String mPosterThumbnail, String mOverview, double mUserRating, String mReleaseDate, double mPopularity) {
        this.mTitle = mTitle;
        this.mPosterThumbnail = mPosterThumbnail;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
        this.mPopularity = mPopularity;
    }

    public String getmOverview() {
        return mOverview;
    }

    public String getmTitle() {
        return mTitle;
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

    public double getmPopularity() {
        return mPopularity;
    }
}
