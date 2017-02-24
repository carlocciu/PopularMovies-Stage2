package com.carlonuccio.android.popularmovies.utilities;

/**
 * Created by carlonuccio on 30/01/17.
 */

import android.net.Uri;
import android.util.Log;

import com.carlonuccio.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * These utilities will be used to communicate with the themoviedb servers.
 */
public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_URL =
            "http://api.themoviedb.org/3/movie/";

    private static final String API_PARAM_PAGE = "page";
    private static final String API_LANGUAGE = "language";

    private static final String API_KEY = "api_key";

    private static final String TRAILERS_PATH = "videos";
    private static final String REVIEW_PATH = "reviews";

    public static URL buildUrl(Integer page, String sorting) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(sorting)
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_API_KEY)
                .appendQueryParameter(API_LANGUAGE, "en")
                .appendQueryParameter(API_PARAM_PAGE, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildTrailerUrl(int id) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(TRAILERS_PATH)
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_API_KEY)
                .appendQueryParameter(API_LANGUAGE, "en")
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildReviewUrl(int id, int page) {
        Uri builtUri = Uri.parse(MOVIE_URL).buildUpon()
                .appendPath(String.valueOf(id))
                .appendPath(REVIEW_PATH)
                .appendQueryParameter(API_KEY, BuildConfig.THE_MOVIE_API_KEY)
                .appendQueryParameter(API_LANGUAGE, "en")
                .appendQueryParameter(API_PARAM_PAGE, String.valueOf(page))
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
