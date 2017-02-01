package com.carlonuccio.android.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.carlonuccio.android.popularmovies.R;

/**
 * Created by carlonuccio on 01/02/17.
 */

public class PopularMoviePreferences {

    public static String getPreferredSorting(Context context) {

        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String keyForSorting = context.getString(R.string.pref_sorting);
        String defaultSorting = context.getString(R.string.pref_sorting_default);
        return prefs.getString(keyForSorting, defaultSorting);
    }

}
