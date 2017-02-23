package com.carlonuccio.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlonuccio.android.popularmovies.data.PopularMoviePreferences;
import com.carlonuccio.android.popularmovies.utilities.MovieUtils;
import com.carlonuccio.android.popularmovies.utilities.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.carlonuccio.android.popularmovies.EndlessRecyclerViewScrollListener;

import com.carlonuccio.android.popularmovies.data.MovieContract.MovieEntry;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Movie>> {


    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.recyclerview_movie) RecyclerView mRecyclerView;
    @BindView(R.id.tv_error_message_display) TextView mErrorMessageDisplay;
    @BindView(R.id.pb_loading_indicator) ProgressBar mLoadingIndicator;

    private MovieAdapter mMovieAdapter;
    private int mPagesLoaded;

    private int mPosition = RecyclerView.NO_POSITION;

    private static final int ID_MOVIE_LOADER = 15;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String keyForSorting;

    private EndlessRecyclerViewScrollListener scrollListener;

    Bundle args = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setElevation(0f);
        ButterKnife.bind(this);

        final GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        }


        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);


        mMovieAdapter = new MovieAdapter(this, this);
        mMovieAdapter.setHasStableIds(true);

        mRecyclerView.setAdapter(mMovieAdapter);


        scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                if (!(PopularMoviePreferences.getPreferredSorting(MainActivity.this).equals("favorites")))
                    loadMovieData();
            }
        };
        // Adds the scroll listener to RecyclerView
        mRecyclerView.addOnScrollListener(scrollListener);

        /*

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loadMovieData();
                }
            }
        });*/

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        keyForSorting = this.getString(R.string.pref_sorting);

        mPagesLoaded = 0;
        loadMovieData();

    }


    private void loadMovieData() {
        args.putInt("page",++mPagesLoaded);

        if (getSupportLoaderManager().getLoader(ID_MOVIE_LOADER) == null) {
            getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, args, this);
        } else {
            getSupportLoaderManager().restartLoader(ID_MOVIE_LOADER, args, this);
        }

    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie singleMovie) {
        Intent intentToStartDetailActivity = new Intent(this, DetailActivity.class);
        intentToStartDetailActivity.putExtra("Movie", singleMovie);
        startActivityForResult(intentToStartDetailActivity, 1);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {

        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            ArrayList<Movie> mMovie;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);

                if (mMovie != null)
                    deliverResult(mMovie);
                else
                    forceLoad();


            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                Integer page = args.getInt("page");

                String sorting = PopularMoviePreferences
                        .getPreferredSorting(MainActivity.this);

                if (!(sorting.equals("favorites"))) {

                    URL movieRequestURL = NetworkUtils.buildUrl(page, sorting);

                    try {
                        String jsonMovieResponse = NetworkUtils
                                .getResponseFromHttpUrl(movieRequestURL);

                        return MovieUtils.getSimpleMoviePostersFromJson(MainActivity.this, jsonMovieResponse);


                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
                else {
                    Cursor retCursor = getContentResolver().query(MovieEntry.CONTENT_URI, null, null, null, null);
                    ArrayList<Movie> mArrayList = new ArrayList<>();
                    if (retCursor != null) {
                        for (retCursor.moveToFirst(); !retCursor.isAfterLast(); retCursor.moveToNext()) {
                            mArrayList.add(new Movie(retCursor.getInt(retCursor.getColumnIndex(MovieEntry.ID)),
                                    retCursor.getString(retCursor.getColumnIndex(MovieEntry.COLUMN_TITLE)),
                                    retCursor.getString(retCursor.getColumnIndex(MovieEntry.COLUMN_POSTER)),
                                    retCursor.getString(retCursor.getColumnIndex(MovieEntry.COLUMN_OVERVIEW)),
                                    retCursor.getDouble(retCursor.getColumnIndex(MovieEntry.COLUMN_USER_RATING)),
                                    retCursor.getString(retCursor.getColumnIndex(MovieEntry.COLUMN_RELEASE_DATE))));
                        }
                        return mArrayList;
                    }
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<Movie> data) {
                mMovie = data;
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == data){
            showErrorMessage();
        } else {
            showMovieDataView();
            mMovieAdapter.setMovieData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.sorting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case (R.id.menuSortPopularity):
                editor.putString(keyForSorting, "popular");
                editor.commit();

                mPagesLoaded = 0;
                mMovieAdapter.clear();
                scrollListener.resetState();
                loadMovieData();
                return true;
            case (R.id.menuSortTopRated):
                editor.putString(keyForSorting, "top_rated");
                editor.commit();

                mPagesLoaded = 0;
                mMovieAdapter.clear();
                scrollListener.resetState();
                loadMovieData();
                return true;
            case (R.id.menuSortFavorites):
                editor.putString(keyForSorting, "favorites");
                editor.commit();

                mPagesLoaded = 0;
                mMovieAdapter.clear();
                scrollListener.resetState();
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume()
    {
        super.onResume();
        getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, args, this);
    }


}
