package com.carlonuccio.android.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import com.carlonuccio.android.popularmovies.data.MovieContract;
import com.carlonuccio.android.popularmovies.data.MovieContract.MovieEntry;

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

    private byte[] mPoster;

    public Movie(int ID, String mTitle, String mPosterThumbnail, String mOverview, double mUserRating, String mReleaseDate) {
        this.ID = ID;
        this.mTitle = mTitle;
        this.mPosterThumbnail = mPosterThumbnail;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }

    public Movie(int ID, String mTitle, byte[] mPoster, String mOverview, double mUserRating, String mReleaseDate) {
        this.ID = ID;
        this.mTitle = mTitle;
        this.mPoster = mPoster;
        this.mOverview = mOverview;
        this.mUserRating = mUserRating;
        this.mReleaseDate = mReleaseDate;
    }

    public byte[] getmPoster() {
        return mPoster;
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


    public boolean saveToBookmarks(Context context) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieEntry.ID, this.ID);
        contentValues.put(MovieEntry.COLUMN_TITLE, this.mTitle);
        contentValues.put(MovieEntry.COLUMN_OVERVIEW, this.mOverview);
        contentValues.put(MovieEntry.COLUMN_POSTER, this.mPosterThumbnail);
        contentValues.put(MovieEntry.COLUMN_USER_RATING, this.mUserRating);
        contentValues.put(MovieEntry.COLUMN_RELEASE_DATE, this.mReleaseDate);

        if (context.getContentResolver().insert(MovieEntry.CONTENT_URI, contentValues) != null) {
            Toast.makeText(context, R.string.insert_fav, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, R.string.insert_fav_error, Toast.LENGTH_SHORT).show();
            return false;
        }

    }

    public boolean removeFromBookmarks(Context context) {
        Uri uri = MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(String.valueOf(this.ID)).build();

        long movieDeleted = context.getContentResolver().delete(uri, null, null);
        if (movieDeleted > 0) {
            Toast.makeText(context, R.string.delete_fav, Toast.LENGTH_SHORT).show();
            return true;
        } else {
            Toast.makeText(context, R.string.delete_fav_error, Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public boolean isBookmarked(Context context) {
        Cursor cursor = context.getContentResolver().query(MovieEntry.CONTENT_URI,
                new String[]{MovieEntry.ID},
                MovieEntry.ID + "=?",
                new String[]{Long.toString(this.ID)}, null);
        if (cursor != null) {
            boolean favorited = cursor.getCount() > 0;
            cursor.close();
            return favorited;
        }
        return false;
    }
}
