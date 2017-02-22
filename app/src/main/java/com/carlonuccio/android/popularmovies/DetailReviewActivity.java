package com.carlonuccio.android.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.carlonuccio.android.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by carlonuccio on 22/02/17.
 */

public class DetailReviewActivity extends AppCompatActivity {

    private String mAuthor;
    private String mContent;

    @BindView(R.id.tv_text_author) TextView authorTV;
    @BindView(R.id.tv_text_content) TextView contentTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_details);
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            Review review = intentThatStartedThisActivity.getParcelableExtra("Review");

            mAuthor = review.getmAuthor();
            mContent = review.getmContent();

            authorTV.setText(mAuthor);
            contentTV.setText(mContent);

        }

    }


}
