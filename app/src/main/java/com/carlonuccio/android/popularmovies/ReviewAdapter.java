package com.carlonuccio.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by carlonuccio on 22/02/17.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewAdapterViewHolder>{

    private Context mContext;
    private final ReviewAdapterOnClickHandler mClickHandler;

    private ArrayList<Review> mReviews = new ArrayList<>();

    public interface ReviewAdapterOnClickHandler {
        void onClick(Review singleReview);
    }

    public ReviewAdapter(Context context, ReviewAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    void clear(){
        mReviews.clear();
        notifyDataSetChanged();
    }

    public class ReviewAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mReviewAuthor;
        public final TextView mReviewContent;


        public ReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthor = (TextView) view.findViewById(R.id.tv_text_review_author);
            mReviewContent = (TextView) view.findViewById(R.id.tv_text_review_content);
            view.setOnClickListener(this);
        }

        void setText(Review review){
            mReviewAuthor.setText(review.getmAuthor());
            mReviewContent.setText(review.getmContent());
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Review singleReview = mReviews.get(adapterPosition);
            mClickHandler.onClick(singleReview);
        }
    }

    @Override
    public ReviewAdapter.ReviewAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewAdapter.ReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewAdapterViewHolder movieAdapterViewHolder, int position) {
        Review review = mReviews.get(position);
        movieAdapterViewHolder.setText(review);
    }

    @Override
    public int getItemCount() {
        if (null == mReviews) return 0;
        return mReviews.size();
    }

    public void setReviewData(ArrayList<Review> reviewData) {
        mReviews.addAll(reviewData);
        notifyDataSetChanged();
    }

}
