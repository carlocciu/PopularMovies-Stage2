package com.carlonuccio.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carlonuccio.android.popularmovies.data.PopularMoviePreferences;
import com.carlonuccio.android.popularmovies.utilities.MovieUtils;
import com.carlonuccio.android.popularmovies.utilities.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by carlonuccio on 30/01/17.
 */

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, LoaderManager.LoaderCallbacks<ArrayList<Trailer>> {

    private String mTitle;
    private String mPosterThumbnail;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;
    private int mID;

    private TrailerAdapter mTrailerAdapter;
    private static final int ID_TRAILER_LOADER = 17;
    Bundle args = new Bundle();

    @BindView(R.id.tv_overview_display) TextView mOverviewTV;
    @BindView(R.id.tv_title_display) TextView mTitleTV;
    @BindView(R.id.tv_rating_display) TextView mRatingTV;
    @BindView(R.id.tv_release_display) TextView mReleaseTV;
    @BindView(R.id.iv_poster_display) ImageView mPosterIV;
    @BindView(R.id.recyclerview_trailer) RecyclerView mRecyclerTrailerView;
    @BindView(R.id.tv_error_trailer_message_display) TextView mErrorTV;
    @BindView(R.id.pb_loading_trailer_indicator) ProgressBar mLoadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        final LinearLayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerTrailerView.setLayoutManager(layoutManager);

        mRecyclerTrailerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this, this);

        mRecyclerTrailerView.setAdapter(mTrailerAdapter);


        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Movie mMovie = intentThatStartedThisActivity.getParcelableExtra("Movie");

            mID = mMovie.getID();
            mTitle = mMovie.getmTitle();
            mPosterThumbnail = mMovie.getmPosterThumbnail();
            mOverview = mMovie.getmOverview();
            mUserRating = mMovie.getmUserRating();
            mReleaseDate = mMovie.getmReleaseDate();

            Uri posterUri = MovieUtils.getPosterUri("w500", mPosterThumbnail);
            Picasso.with(this).load(posterUri).into(mPosterIV);

            mOverviewTV.setText(mOverview);
            mTitleTV.setText(mTitle);

            mRatingTV.setText(String.format(getString(R.string.vote_average), mUserRating));

            mReleaseTV.setText(mReleaseDate);

        }

        args.putInt("id",mID);
        getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, args, this);

    }

    private void showTrailerDataView() {
        mErrorTV.setVisibility(View.INVISIBLE);
        mRecyclerTrailerView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mErrorTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Trailer singleTrailer) {
        String site = singleTrailer.getmSite();
        String key = singleTrailer.getmKey();

        if (site.equals("YouTube")){
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
            startActivity(intent);
        }
    }

    @Override
    public Loader<ArrayList<Trailer>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Trailer>>(this) {

            ArrayList<Trailer> mTrailer;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                if (args == null) {
                    return;
                }

                mLoadingIndicator.setVisibility(View.VISIBLE);

                if (mTrailer != null)
                    deliverResult(mTrailer);
                else
                    forceLoad();


            }

            @Override
            public ArrayList<Trailer> loadInBackground() {
                int id = args.getInt("id");

                URL trailerRequestURL = NetworkUtils.buildTrailerUrl(id);

                try {
                    String jsonMovieResponse = NetworkUtils
                            .getResponseFromHttpUrl(trailerRequestURL);

                    ArrayList<Trailer> simpleTrailerJson = MovieUtils
                            .getSimpleTrailersFromJson(DetailActivity.this, jsonMovieResponse);

                    return simpleTrailerJson;

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void deliverResult(ArrayList<Trailer> data) {
                mTrailer = data;
                mLoadingIndicator.setVisibility(View.INVISIBLE);
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Trailer>> loader, ArrayList<Trailer> data) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (null == data){
            showErrorMessage();
        } else {
            showTrailerDataView();
            mTrailerAdapter.setTrailerData(data);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

    }
}

