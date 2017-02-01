package com.carlonuccio.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlonuccio.android.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by carlonuccio on 30/01/17.
 */

public class DetailActivity extends AppCompatActivity {

    private String mTitle;
    private String mPosterThumbnail;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;

    private TextView mOverviewTV;
    private TextView mTitleTV;
    private TextView mRatingTV;
    private TextView mReleaseTV;
    private ImageView mPosterIV;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            mMovie = (Movie) intentThatStartedThisActivity.getSerializableExtra("Movie");

            mTitle = mMovie.getmTitle();
            mPosterThumbnail = mMovie.getmPosterThumbnail();
            mOverview = mMovie.getmOverview();
            mUserRating = mMovie.getmUserRating();
            mReleaseDate = mMovie.getmReleaseDate();

            mOverviewTV = (TextView) findViewById(R.id.tv_overview_display);
            mTitleTV = (TextView) findViewById(R.id.tv_title_display);
            mRatingTV = (TextView) findViewById(R.id.tv_rating_display);
            mReleaseTV = (TextView) findViewById(R.id.tv_release_display);
            mPosterIV = (ImageView) findViewById(R.id.iv_poster_display);

            Uri posterUri = MovieUtils.getPosterUri("w500", mPosterThumbnail);
            Picasso.with(this).load(posterUri).into(mPosterIV);

            mOverviewTV.setText(mOverview);
            mTitleTV.setText(mTitle);

            mRatingTV.setText(String.format(getString(R.string.vote_average), mUserRating));

            mReleaseTV.setText(mReleaseDate);

        }


    }
}

