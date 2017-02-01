package com.carlonuccio.android.popularmovies;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.carlonuccio.android.popularmovies.utilities.MovieUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by carlonuccio on 30/01/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private ArrayList<Movie> mMoviePosterPath = new ArrayList<>();

    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(Movie singleMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {

        mClickHandler = clickHandler;
    }

    void clear(){
        mMoviePosterPath.clear();
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;
        public final Context mContext;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mContext = itemView.getContext();
            mMovieImageView = (ImageView) view.findViewById(R.id.tv_image_thumbnail);
            view.setOnClickListener(this);
        }

        void setImage(String movie){
            Uri posterUri = MovieUtils.getPosterUri("w500", movie);
            Picasso.with(mContext).load(posterUri).into(mMovieImageView);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Movie singleMovie = mMoviePosterPath.get(adapterPosition);
            mClickHandler.onClick(singleMovie);
        }
    }

    @Override
    public MovieAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String moviePoster = mMoviePosterPath.get(position).getmPosterThumbnail();
        movieAdapterViewHolder.setImage(moviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == mMoviePosterPath) return 0;
        return mMoviePosterPath.size();
    }

    public void setMovieData(ArrayList<Movie> movieData) {
        mMoviePosterPath.addAll(movieData);
        notifyDataSetChanged();
    }


}
