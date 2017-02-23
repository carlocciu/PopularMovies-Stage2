package com.carlonuccio.android.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carlonuccio.android.popularmovies.data.MovieContract.MovieEntry;

/**
 * Created by carlonuccio on 22/02/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "favorites_movie.db";

    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_TABLE =

                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +

                        MovieEntry.ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        MovieEntry.COLUMN_TITLE       + " TEXT NOT NULL, "                 +

                        MovieEntry.COLUMN_POSTER + " TEXT NOT NULL,"                  +

                        MovieEntry.COLUMN_OVERVIEW   + " TEXT NOT NULL, "                    +
                        MovieEntry.COLUMN_USER_RATING   + " REAL NOT NULL, "                    +

                        MovieEntry.COLUMN_RELEASE_DATE   + " TEXT NOT NULL "                    +


                        " );";

        db.execSQL(SQL_CREATE_MOVIE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
