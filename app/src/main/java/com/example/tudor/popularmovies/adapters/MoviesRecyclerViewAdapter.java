package com.example.tudor.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tudor.popularmovies.R;
import com.example.tudor.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by tudor on 04.03.2018.
 */

public class MoviesRecyclerViewAdapter extends RecyclerView.Adapter<MoviesRecyclerViewAdapter.MovieViewHolder> {

    private Context mContext;
    private ArrayList<Movie> mMovies;
    protected ItemListener mListener;


    public MoviesRecyclerViewAdapter(Context context, ArrayList<Movie> movies, ItemListener itemListener) {
        this.mContext = context;
        this.mMovies = movies;
        this.mListener = itemListener;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.movie_recycler_view_item, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        holder.setData(mMovies.get(position));
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView;
        public Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster_iv);

        }

        public void setData(Movie movieItem) {
            this.movie = movieItem;
            Picasso.with(mContext)
                    .load(movieItem.getPosterLink())
                    .into(imageView);
        }

        @Override
        public void onClick(View view) {
            if (mListener != null) {
                mListener.onItemClick(movie);
            }
        }
    }

    public interface ItemListener {
        void onItemClick(Movie movie);
    }
}
