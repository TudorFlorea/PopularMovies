package com.example.tudor.popularmovies.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.tudor.popularmovies.FavoriteActivity;
import com.example.tudor.popularmovies.database.MovieContract.MovieEntry;
import com.example.tudor.popularmovies.R;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.utils.InterfaceUtils.MovieItemListener;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tudor on 09.04.2018.
 */

public class FavoriteMoviesRecyclerViewAdapter extends CursorRecyclerViewAdapter<FavoriteMoviesRecyclerViewAdapter.FavoriteMoviesViewHolder> {

    private Context mContext;
    private Cursor mCursor;
    private MovieItemListener mListerner;

    public FavoriteMoviesRecyclerViewAdapter(Context context, Cursor cursor, MovieItemListener listener) {
        super(context, cursor);
        mContext = context;
        mCursor = cursor;
        mListerner = listener;
    }

    @Override
    public FavoriteMoviesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_recycler_view_item, parent, false);

        return new FavoriteMoviesRecyclerViewAdapter.FavoriteMoviesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FavoriteMoviesViewHolder viewHolder, Cursor cursor) {

        cursor.moveToPosition(cursor.getPosition());

        viewHolder.setData(cursor);

    }

    public class FavoriteMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.movie_poster_iv)
        ImageView imageView;
        private Movie mMovie;

        public FavoriteMoviesViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        public void setData(Cursor cursor) {

            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_ID));
            long databaseId = cursor.getLong(cursor.getColumnIndexOrThrow(MovieEntry._ID));
            String title = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_MOVIE_TITLE));
            String releseDate = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_RELEASE_DATE));
            String posterPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_POSTER_PATH));
            String backdropPath = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_BACKDROP_PATH));
            double voteAverage = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_VOTE_AVERAGE));
            long voteCount = cursor.getLong(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_VOTE_COUNT));
            double popularity = cursor.getDouble(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_POPULARITY));
            String plot = cursor.getString(cursor.getColumnIndexOrThrow(MovieEntry.COLUMN_OVERVIEW));

            mMovie = new Movie(id, title, releseDate, posterPath, backdropPath, voteAverage, plot, voteCount, popularity, null, null);

            mMovie.setDatabaseId(databaseId);

            Picasso.with(mContext)
                    .load(mMovie.getPosterLink())
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_error)
                    .into(imageView);

        }

        @Override
        public void onClick(View view) {
            if (mListerner != null) {
                mListerner.onItemClick(mMovie, FavoriteActivity.class);
            }
        }
    }

}
