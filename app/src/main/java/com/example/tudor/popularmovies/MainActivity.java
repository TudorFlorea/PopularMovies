package com.example.tudor.popularmovies;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tudor.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.data.MovieFactory;
import com.example.tudor.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, MoviesRecyclerViewAdapter.ItemListener {

    private RecyclerView mMoviesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRV = (RecyclerView) findViewById(R.id.movies_rv);

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mMoviesRV.setLayoutManager(manager);

        getSupportLoaderManager().restartLoader(1, null, this);
    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

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
    public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
        MoviesRecyclerViewAdapter adapter = new MoviesRecyclerViewAdapter(MainActivity.this, data, this);
        mMoviesRV.setAdapter(adapter);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

    }

    @Override
    public void onItemClick(Movie movie) {
        Toast.makeText(this, movie.getImagePath(), Toast.LENGTH_SHORT).show();
    }
}
