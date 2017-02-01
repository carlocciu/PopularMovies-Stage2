package com.carlonuccio.android.popularmovies.utilities;

/**
 * Created by carlonuccio on 30/01/17.
 */

import android.content.Context;
import android.net.Uri;

import com.carlonuccio.android.popularmovies.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class MovieUtils {

    public static ArrayList<Movie> getSimpleMoviePostersFromJson(Context context, String movieJsonStr) throws JSONException {

        final String RESULT = "results";
        final String POSTER_PATH = "poster_path";
        final String TITLE = "title";
        final String USER_RATING = "vote_average";
        final String RELEASE_DATE = "release_date";
        final String OVERVIEW = "overview";
        final String POPULARITY = "popularity";

        ArrayList<Movie> movieData = new ArrayList<>();

        JSONObject moviePageJson = new JSONObject(movieJsonStr);

        JSONArray singlePage = moviePageJson.getJSONArray(RESULT);

        for (int i = 0; i < singlePage.length(); i++) {
            movieData.add(i, new Movie(singlePage.getJSONObject(i).getString(TITLE),
                    singlePage.getJSONObject(i).getString(POSTER_PATH),
                    singlePage.getJSONObject(i).getString(OVERVIEW),
                    singlePage.getJSONObject(i).getDouble(USER_RATING),
                    singlePage.getJSONObject(i).getString(RELEASE_DATE),
                    singlePage.getJSONObject(i).getDouble(POPULARITY)));

        }

        return movieData;
    }

    public static Uri getPosterUri(String size, String posterPath) {
        final String BASE_URL = "http://image.tmdb.org/t/p";
        return Uri.parse(BASE_URL).buildUpon().appendPath(size).appendEncodedPath(posterPath).build();
    }
}
