package com.carlonuccio.android.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by carlonuccio on 21/02/17.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerAdapterViewHolder> {

    private final TrailerAdapterOnClickHandler mClickHandler;
    private Context mContext;
    private ArrayList<Trailer> mTrailers = new ArrayList<>();

    public TrailerAdapter(Context context, TrailerAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    void clear() {
        mTrailers.clear();
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapter.TrailerAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.trailer_list_item, viewGroup, false);
        return new TrailerAdapter.TrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapterViewHolder movieAdapterViewHolder, int position) {
        String moviePoster = mTrailers.get(position).getmName();
        movieAdapterViewHolder.setText(moviePoster);
    }

    @Override
    public int getItemCount() {
        if (null == mTrailers) return 0;
        return mTrailers.size();
    }

    public void setTrailerData(ArrayList<Trailer> movieData) {
        mTrailers.addAll(movieData);
        notifyDataSetChanged();
    }

    public interface TrailerAdapterOnClickHandler {
        void onClick(Trailer singleTrailer);
    }

    public class TrailerAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView mTrailerTitle;

        public TrailerAdapterViewHolder(View view) {
            super(view);
            mTrailerTitle = (TextView) view.findViewById(R.id.tv_text_trailer);
            view.setOnClickListener(this);
        }

        void setText(String movie) {
            mTrailerTitle.setText(movie);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            Trailer singleTrailer = mTrailers.get(adapterPosition);
            mClickHandler.onClick(singleTrailer);
        }
    }
}
