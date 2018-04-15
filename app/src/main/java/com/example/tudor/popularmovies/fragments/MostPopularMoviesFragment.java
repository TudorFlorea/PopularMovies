package com.example.tudor.popularmovies.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.tudor.popularmovies.R;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tudor on 15.04.2018.
 */

public class MostPopularMoviesFragment extends Fragment {

    @BindView(R.id.popular_movies_rv) RecyclerView mPopularMoviesRecyclerView;

    public MostPopularMoviesFragment() {

    }

    private final int MOST_POPULAR_MOVIES_LOADER_ID = 100;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

    }
}
