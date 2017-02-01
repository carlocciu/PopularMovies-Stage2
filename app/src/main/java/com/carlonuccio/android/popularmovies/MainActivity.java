package com.carlonuccio.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {


        private static final String TAG = MainActivity.class.getSimpleName();

        private RecyclerView mRecyclerView;
        private MovieAdapter mMovieAdapter;
        private TextView mErrorMessageDisplay;
        private ProgressBar mLoadingIndicator;
        private int mPagesLoaded;

        SharedPreferences prefs;
        SharedPreferences.Editor editor;
        String keyForSorting;

        @Override
        protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movie);

        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);

        final GridLayoutManager layoutManager;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            layoutManager = new GridLayoutManager(this, 2, LinearLayoutManager.VERTICAL, false);
        } else {
            layoutManager = new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false);
        }


        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);

        mRecyclerView.setAdapter(mMovieAdapter);

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
        });


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        mPagesLoaded = 0;

        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();
        keyForSorting = this.getString(R.string.pref_sorting);

        loadMovieData();

    }


    private void loadMovieData() {
        showMovieDataView();
        mPagesLoaded++;
        new FetchMovieTask().execute(mPagesLoaded);
    }

    private void showMovieDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Movie singleMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra("Movie", singleMovie);
        startActivity(intentToStartDetailActivity);
    }

    private void showErrorMessage() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<Integer, Void, ArrayList<Movie>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<Movie> doInBackground(Integer... params) {

            Integer page = params[0];

            String sorting = PopularMoviePreferences
                    .getPreferredSorting(MainActivity.this);

            URL movieRequestURL = NetworkUtils.buildUrl(page, sorting);

            try {
                String jsonMovieResponse = NetworkUtils
                        .getResponseFromHttpUrl(movieRequestURL);

                ArrayList<Movie> simpleMovieJson = MovieUtils
                        .getSimpleMoviePostersFromJson(MainActivity.this, jsonMovieResponse);

                return simpleMovieJson;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();
                mMovieAdapter.setMovieData(movieData);
            } else {
                showErrorMessage();
            }
        }
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
                loadMovieData();
                return true;
            case (R.id.menuSortTopRated):
                editor.putString(keyForSorting, "top_rated");
                editor.commit();

                mPagesLoaded = 0;
                mMovieAdapter.clear();
                loadMovieData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
