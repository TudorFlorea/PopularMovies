package com.example.tudor.popularmovies.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tudor.popularmovies.R;
import com.example.tudor.popularmovies.adapters.FavoriteMoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.data.MovieFactory;
import com.example.tudor.popularmovies.database.MovieContract;
import com.example.tudor.popularmovies.utils.DisplayUtils;
import com.example.tudor.popularmovies.utils.InterfaceUtils;
import com.example.tudor.popularmovies.utils.InternetUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tudor on 16.04.2018.
 */

public class FavoriteMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, InterfaceUtils.MovieItemListener{

    @BindView(R.id.favorite_movies_rv) RecyclerView mFavoriteMoviesRecyclerView;
    private FavoriteMoviesRecyclerViewAdapter mCursorMoviesAdapter;
    private MoviesRecyclerViewAdapter mMoviesAdapter;
    private GridLayoutManager mMoviesLayoutManager;

    private final int MOVIE_POSTER_WIDTH = 185;
    public final String RV_STATE_KEY = "recycler_list_state";
    public final String MOVIES_STATE_KEY = "movies_state";
    private final int MOST_POPULAR_MOVIES_LOADER_ID = 100;
    private final String CONFIGURATION_CHANGED = "configuration_changed";
    private final String CONFIGURATION_GHANGED_KEY = "configuration_changed_key";
    private Parcelable mRecyclerViewState;
    private ArrayList<Movie> mMovies;

    public FavoriteMoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_favorite_movies, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_STATE_KEY)) {
                mFavoriteMoviesRecyclerView.setHasFixedSize(true);
                mMoviesLayoutManager = new GridLayoutManager(getActivity(), DisplayUtils.numberOfColumns(getActivity().getWindowManager(), MOVIE_POSTER_WIDTH ), GridLayoutManager.VERTICAL, false);
                mFavoriteMoviesRecyclerView.setLayoutManager(mMoviesLayoutManager);
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_STATE_KEY);
                mMoviesAdapter = new MoviesRecyclerViewAdapter(getActivity(), mMovies, this);
                mFavoriteMoviesRecyclerView.setAdapter(mMoviesAdapter);
            }
        } else {
            mFavoriteMoviesRecyclerView.setHasFixedSize(true);
            mMoviesLayoutManager = new GridLayoutManager(getActivity(), DisplayUtils.numberOfColumns(getActivity().getWindowManager(), MOVIE_POSTER_WIDTH ), GridLayoutManager.VERTICAL, false);
            mFavoriteMoviesRecyclerView.setLayoutManager(mMoviesLayoutManager);
            mCursorMoviesAdapter = new FavoriteMoviesRecyclerViewAdapter(getActivity(), null, this);
            mFavoriteMoviesRecyclerView.setAdapter(mCursorMoviesAdapter);
            getActivity().getSupportLoaderManager().restartLoader(MOST_POPULAR_MOVIES_LOADER_ID, null, this);
        }


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<Cursor>(getActivity()) {

            Cursor mMovieData = null;

            @Override
            protected void onStartLoading() {
                if (mMovieData != null) {
                    deliverResult(mMovieData);
                } else {
                    forceLoad();
                }
            }

            @Override
            public Cursor loadInBackground() {

                try {
                    return getActivity().getContentResolver().query(
                            MovieContract.CONTENT_URI,
                            null,
                            null,
                            null,
                            null
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mMovieData = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovies = MovieFactory.listFromCursor(data);
        mCursorMoviesAdapter.swapCursor(data);
        mFavoriteMoviesRecyclerView.setAdapter(mCursorMoviesAdapter);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorMoviesAdapter.swapCursor(null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(MOVIES_STATE_KEY, mMovies);

        outState.putString(CONFIGURATION_GHANGED_KEY, CONFIGURATION_CHANGED);

        mRecyclerViewState = mMoviesLayoutManager.onSaveInstanceState();
        outState.putParcelable(RV_STATE_KEY, mRecyclerViewState);

    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null) {
            mRecyclerViewState = savedInstanceState.getParcelable(RV_STATE_KEY);
        }

    }

    @Override
    public void onItemClick(Movie movie, Class targetActivity) {
        Intent i = new Intent(getActivity(), targetActivity);
        i.putExtra(getResources().getString(R.string.intent_movie_key), movie);
        startActivity(i);
    }
}
