package com.carlonuccio.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.carlonuccio.android.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by carlonuccio on 30/01/17.
 */

public class DetailActivity extends AppCompatActivity {

    private String mTitle;
    private String mPosterThumbnail;
    private String mOverview;
    private double mUserRating;
    private String mReleaseDate;

    @BindView(R.id.tv_overview_display) TextView mOverviewTV;
    @BindView(R.id.tv_title_display) TextView mTitleTV;
    @BindView(R.id.tv_rating_display) TextView mRatingTV;
    @BindView(R.id.tv_release_display) TextView mReleaseTV;
    @BindView(R.id.iv_poster_display) ImageView mPosterIV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Movie mMovie = getIntent().getParcelableExtra("Movie");

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

    }
}

