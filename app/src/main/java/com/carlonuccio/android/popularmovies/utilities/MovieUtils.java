package com.carlonuccio.android.popularmovies.utilities;

/**
 * Created by carlonuccio on 30/01/17.
 */

import android.content.Context;
import android.net.Uri;

import com.carlonuccio.android.popularmovies.Movie;
import com.carlonuccio.android.popularmovies.Review;
import com.carlonuccio.android.popularmovies.Trailer;

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
        final String ID = "id";


        ArrayList<Movie> movieData = new ArrayList<>();

        JSONObject moviePageJson = new JSONObject(movieJsonStr);

        JSONArray singlePage = moviePageJson.getJSONArray(RESULT);

        for (int i = 0; i < singlePage.length(); i++) {
            movieData.add(i, new Movie(singlePage.getJSONObject(i).getInt(ID),
                    singlePage.getJSONObject(i).getString(TITLE),
                    singlePage.getJSONObject(i).getString(POSTER_PATH),
                    singlePage.getJSONObject(i).getString(OVERVIEW),
                    singlePage.getJSONObject(i).getDouble(USER_RATING),
                    singlePage.getJSONObject(i).getString(RELEASE_DATE)));
        }

        return movieData;
    }

    public static ArrayList<Trailer> getSimpleTrailersFromJson(Context context, String trailerJsonStr) throws JSONException {

        final String RESULT = "results";
        final String NAME = "name";
        final String KEY = "key";
        final String SITE = "site";

        ArrayList<Trailer> trailerData = new ArrayList<>();

        JSONObject trailerJson = new JSONObject(trailerJsonStr);

        JSONArray singlePage = trailerJson.getJSONArray(RESULT);

        for (int i = 0; i < singlePage.length(); i++) {
            if (singlePage.getJSONObject(i).getString(SITE).equals("YouTube")) {
                trailerData.add(i, new Trailer(
                        singlePage.getJSONObject(i).getString(NAME),
                        singlePage.getJSONObject(i).getString(KEY),
                        singlePage.getJSONObject(i).getString(SITE)));
            }
        }

        return trailerData;
    }

    public static ArrayList<Review> getSimpleReviewsFromJson(Context context, String reviewsJsonStr) throws JSONException {

        final String RESULT = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        ArrayList<Review> reviewData = new ArrayList<>();

        JSONObject reviewJSON = new JSONObject(reviewsJsonStr);

        JSONArray singlePage = reviewJSON.getJSONArray(RESULT);

        for (int i = 0; i < singlePage.length(); i++) {
                reviewData.add(i, new Review(
                        singlePage.getJSONObject(i).getString(AUTHOR),
                        singlePage.getJSONObject(i).getString(CONTENT)));
        }

        return reviewData;
    }

    public static Uri getPosterUri(String size, String posterPath) {
        final String BASE_URL = "http://image.tmdb.org/t/p";
        return Uri.parse(BASE_URL).buildUpon().appendPath(size).appendEncodedPath(posterPath).build();
    }
}
