package com.carlonuccio.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;

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

public class DetailActivity extends AppCompatActivity implements TrailerAdapter.TrailerAdapterOnClickHandler, ReviewAdapter.ReviewAdapterOnClickHandler {

    private String mTitle;
    private String mPosterThumbnail;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;
    private int mID;

    private TrailerAdapter mTrailerAdapter;
    private static final int ID_TRAILER_LOADER = 17;

    private ReviewAdapter mReviewAdapter;
    private static final int ID_REVIEW_LOADER = 23;
    private int mPagesLoaded;

    Bundle args = new Bundle();

    @BindView(R.id.tv_overview_display) TextView mOverviewTV;
    @BindView(R.id.tv_title_display) TextView mTitleTV;
    @BindView(R.id.tv_rating_display) TextView mRatingTV;
    @BindView(R.id.tv_release_display) TextView mReleaseTV;
    @BindView(R.id.iv_poster_display) ImageView mPosterIV;

    @BindView(R.id.recyclerview_trailer) RecyclerView mRecyclerTrailerView;
    @BindView(R.id.tv_error_trailer_message_display) TextView mErrorTV;
    @BindView(R.id.pb_loading_trailer_indicator) ProgressBar mLoadingIndicator;

    @BindView(R.id.recyclerview_review) RecyclerView mRecyclerReviewView;
    @BindView(R.id.tv_error_review_message_display) TextView mErrorReviewTV;
    @BindView(R.id.pb_loading_review_indicator) ProgressBar mReviewLoadingIndicator;

    @BindView(R.id.star) CheckBox mStarCB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        final LinearLayoutManager layoutTrailerManager;
        layoutTrailerManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerTrailerView.setLayoutManager(layoutTrailerManager);

        mRecyclerTrailerView.setHasFixedSize(true);

        mTrailerAdapter = new TrailerAdapter(this, this);

        mRecyclerTrailerView.setAdapter(mTrailerAdapter);


        final LinearLayoutManager layoutReviewManager;
        layoutReviewManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        mRecyclerReviewView.setLayoutManager(layoutReviewManager);

        mRecyclerReviewView.setHasFixedSize(true);

        mReviewAdapter = new ReviewAdapter(this, this);

        mRecyclerReviewView.setAdapter(mReviewAdapter);



        mRecyclerReviewView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getLayoutManager().getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int pastVisiblesItems = layoutReviewManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    loadReviewData();
                }
            }
        });

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            final Movie mMovie = intentThatStartedThisActivity.getParcelableExtra("Movie");

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

            if (mMovie.isBookmarked(this))
                mStarCB.setChecked(true);
            else
                mStarCB.setChecked(false);


            mStarCB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton arg0, boolean isChecked) {
                    Context context = getApplicationContext();
                    if (!mMovie.isBookmarked(context)){
                        if(mMovie.saveToBookmarks(context)){
                            mStarCB.setChecked(true);
                        }
                    }
                    else{
                        if(mMovie.removeFromBookmarks(context)){
                            mStarCB.setChecked(false);
                        }
                    }
                }
            });

        }

        mPagesLoaded = 0;
        args.putInt("id", mID);
        args.putInt("page",++mPagesLoaded);
        getSupportLoaderManager().initLoader(ID_TRAILER_LOADER, args, trailerLoader);
        getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, args, reviewLoader);

    }

    private void loadReviewData(){
        args.putInt("page",++mPagesLoaded);

        if (getSupportLoaderManager().getLoader(ID_REVIEW_LOADER) == null) {
            getSupportLoaderManager().initLoader(ID_REVIEW_LOADER, args, reviewLoader);
        } else {
            getSupportLoaderManager().restartLoader(ID_REVIEW_LOADER, args, reviewLoader);
        }
    }

    private void showTrailerDataView() {
        mErrorTV.setVisibility(View.INVISIBLE);
        mRecyclerTrailerView.setVisibility(View.VISIBLE);
    }

    private void showReviewData() {
        mErrorReviewTV.setVisibility(View.INVISIBLE);
        mRecyclerReviewView.setVisibility(View.VISIBLE);
    }


    private void showErrorMessage() {
        mRecyclerTrailerView.setVisibility(View.INVISIBLE);
        mErrorTV.setVisibility(View.VISIBLE);
    }

    private void showErrorReviewMessage() {
        mRecyclerReviewView.setVisibility(View.INVISIBLE);
        mErrorReviewTV.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(Trailer singleTrailer) {
        String site = singleTrailer.getmSite();
        String key = singleTrailer.getmKey();

        if (site.equals("YouTube")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + key));
            startActivity(intent);
        }
    }

    @Override
    public void onClick(Review singleReview) {
        Intent intentToStartDetailActivity = new Intent(this, DetailReviewActivity.class);
        intentToStartDetailActivity.putExtra("Review", singleReview);
        startActivity(intentToStartDetailActivity);
    }

    private LoaderManager.LoaderCallbacks<ArrayList<Trailer>> trailerLoader
            = new LoaderManager.LoaderCallbacks<ArrayList<Trailer>>() {

        @Override
        public Loader<ArrayList<Trailer>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<Trailer>>(DetailActivity.this) {

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

                        return MovieUtils.getSimpleTrailersFromJson(DetailActivity.this, jsonMovieResponse);


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
            if (null == data) {
                showErrorMessage();
            } else {
                showTrailerDataView();
                mTrailerAdapter.setTrailerData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Trailer>> loader) {

        }

    };



    private LoaderManager.LoaderCallbacks<ArrayList<Review>> reviewLoader
            = new LoaderManager.LoaderCallbacks<ArrayList<Review>>() {

        @Override
        public Loader<ArrayList<Review>> onCreateLoader(int id, final Bundle args) {
            return new AsyncTaskLoader<ArrayList<Review>>(DetailActivity.this) {

                ArrayList<Review> mReviews;

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    if (args == null) {
                        return;
                    }

                    mReviewLoadingIndicator.setVisibility(View.VISIBLE);

                    if (mReviews != null)
                        deliverResult(mReviews);
                    else
                        forceLoad();

                }

                @Override
                public ArrayList<Review> loadInBackground() {
                    int id = args.getInt("id");
                    int page = args.getInt("page");

                    URL trailerRequestURL = NetworkUtils.buildReviewUrl(id, page);

                    try {
                        String jsonMovieResponse = NetworkUtils
                                .getResponseFromHttpUrl(trailerRequestURL);

                        return MovieUtils.getSimpleReviewsFromJson(DetailActivity.this, jsonMovieResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }

                @Override
                public void deliverResult(ArrayList<Review> data) {
                    mReviews = data;
                    mReviewLoadingIndicator.setVisibility(View.INVISIBLE);
                    super.deliverResult(data);
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Review>> loader, ArrayList<Review> data) {
            mReviewLoadingIndicator.setVisibility(View.INVISIBLE);
            if (null == data) {
                showErrorReviewMessage();
            } else {
                showReviewData();
                mReviewAdapter.setReviewData(data);
            }
        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Review>> loader) {

        }

    };



}

