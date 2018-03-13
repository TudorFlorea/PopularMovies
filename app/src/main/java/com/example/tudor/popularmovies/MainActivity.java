package com.example.tudor.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tudor.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.data.MovieFactory;
import com.example.tudor.popularmovies.utils.InternetUtils;
import com.example.tudor.popularmovies.utils.NetworkUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Movie>>, MoviesRecyclerViewAdapter.ItemListener {

    private final String ACTION_RESET_LOADER = "action_reset";
    private final String ACTION_TOP_RATED = "top_rated";
    private final String ACTION_POPULAR = "most_popular";
    private final int MOVIES_LOADER_ID = 100;

    private RecyclerView mMoviesRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMoviesRV = findViewById(R.id.movies_rv);

        GridLayoutManager manager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mMoviesRV.setLayoutManager(manager);

        if (InternetUtils.isNetworkAvailable(this)) {
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, null, this);
            getSupportActionBar().setTitle(R.string.top_rated_title);
        } else {
            //TODO handle the lack of internet on the UI
        }


    }

    @Override
    public Loader<ArrayList<Movie>> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<ArrayList<Movie>>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public ArrayList<Movie> loadInBackground() {
                if (args != null) {
                    String action = args.getString(ACTION_RESET_LOADER);

                    switch (action) {
                        case ACTION_TOP_RATED:
                            return MovieFactory.getTopRatedMovies();
                        case ACTION_POPULAR:
                            return MovieFactory.getMostPopularMovies();
                        default:
                            return null;

                    }
                } else {
                    return MovieFactory.getTopRatedMovies();
                }


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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.action_popular:
                resetMoviesLoader(ACTION_POPULAR);
                getSupportActionBar().setTitle(R.string.most_popular_title);
                break;
            case R.id.action_top_rated:
                resetMoviesLoader(ACTION_TOP_RATED);
                getSupportActionBar().setTitle(R.string.top_rated_title);
                break;
            default:
                return false;
        }

        return true;
    }

    @Override
    public void onItemClick(Movie movie) {
        Intent i = new Intent(MainActivity.this, DetailsActivity.class);
        i.putExtra(getResources().getString(R.string.intent_movie_key), movie);
        startActivity(i);
    }

    private void resetMoviesLoader(String action) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_RESET_LOADER, action);
        if (InternetUtils.isNetworkAvailable(this)) {
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, bundle, this);
        } else {
            //TODO handle the lack of internet on the UI
        }
    }

}
