package com.example.tudor.popularmovies.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.tudor.popularmovies.R;
import com.example.tudor.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.data.MovieFactory;
import com.example.tudor.popularmovies.utils.DisplayUtils;
import com.example.tudor.popularmovies.utils.InterfaceUtils;
import com.example.tudor.popularmovies.utils.InternetUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tudor on 16.04.2018.
 */

public class TopRatedMoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, InterfaceUtils.MovieItemListener{

    @BindView(R.id.top_rated_movies_rv) RecyclerView mTopRatedMoviesRecyclerView;
    @BindView(R.id.top_rated_no_internet_layout) LinearLayout mNoInternetLayout;
    private MoviesRecyclerViewAdapter mMoviesAdapter;
    private GridLayoutManager mMoviesLayoutManager;

    private final int TOP_RATED_MOVIES_LOADER_ID = 200;
    private final int MOVIE_POSTER_WIDTH = 185;
    public final String RV_STATE_KEY = "recycler_list_state";
    public final String MOVIES_STATE_KEY = "movies_state";
    private Parcelable mRecyclerViewState;
    private ArrayList<Movie> mMovies;

    public TopRatedMoviesFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top_rated, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        mNoInternetLayout.setVisibility(View.GONE);
        mTopRatedMoviesRecyclerView.setHasFixedSize(true);
        mMoviesLayoutManager = new GridLayoutManager(getActivity(), DisplayUtils.numberOfColumns(getActivity().getWindowManager(), MOVIE_POSTER_WIDTH ), GridLayoutManager.VERTICAL, false);
        mTopRatedMoviesRecyclerView.setLayoutManager(mMoviesLayoutManager);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(MOVIES_STATE_KEY)) {
                mMovies = savedInstanceState.getParcelableArrayList(MOVIES_STATE_KEY);
                mMoviesAdapter = new MoviesRecyclerViewAdapter(getActivity(), mMovies, this);
                mTopRatedMoviesRecyclerView.setAdapter(mMoviesAdapter);
            }
        } else {
            if (InternetUtils.isNetworkAvailable(getContext())) {
                mTopRatedMoviesRecyclerView.setVisibility(View.VISIBLE);
                mMoviesAdapter = new MoviesRecyclerViewAdapter(getActivity(), null, this);
                mTopRatedMoviesRecyclerView.setAdapter(mMoviesAdapter);
                getActivity().getSupportLoaderManager().restartLoader(TOP_RATED_MOVIES_LOADER_ID, null, this);
            } else {
                mTopRatedMoviesRecyclerView.setVisibility(View.GONE);
                mNoInternetLayout.setVisibility(View.VISIBLE);
                mNoInternetLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().getSupportLoaderManager().restartLoader(TOP_RATED_MOVIES_LOADER_ID, null, TopRatedMoviesFragment.this);
                    }
                });
            }
        }
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(getActivity()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                return MovieFactory.getTopRatedMovies();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> movies) {
        if (movies != null) {
            mNoInternetLayout.setVisibility(View.GONE);
            mTopRatedMoviesRecyclerView.setVisibility(View.VISIBLE);
            mMovies = movies;
            mMoviesAdapter = new MoviesRecyclerViewAdapter(getActivity(), movies, this);
            mTopRatedMoviesRecyclerView.setAdapter(mMoviesAdapter);
            mMoviesAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(MOVIES_STATE_KEY, mMovies);

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
