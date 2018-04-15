package com.example.tudor.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.tudor.popularmovies.adapters.FavoriteMoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.adapters.MoviesRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.data.MovieFactory;
import com.example.tudor.popularmovies.database.MovieContract;
import com.example.tudor.popularmovies.utils.InternetUtils;
import com.example.tudor.popularmovies.utils.InterfaceUtils.MovieItemListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieItemListener {


    private ArrayList<Movie> mMovies;

    private final String ACTION_RESET_LOADER = "action_reset";
    private final String ACTION_TOP_RATED = "top_rated";
    private final String ACTION_POPULAR = "most_popular";
    private final String ACTION_FAVORITE = "favorite";
    private final String MOVIES_KEY = "movies";
    private final String RECYCLER_VIEW_STATE_KEY = "state";
    private final String LIST_STATE_KEY = "list_state";
    private final String FAVORITE_MOVIE_DELETE_KEY = "favorite_movie_delete";
    private final String SORT_MOST_POPULAR = "most_popular";
    private final String SORT_TOP_RATED = "top_rated";
    private final String SORT_FAVORITE = "favorite";
    private final int MOVIES_API_LOADER_ID = 100;
    private final int MOVIES_FAVORITE_LOADER_ID = 200;
    private final int MOVIE_POSTER_WIDTH = 185;

    private FavoriteMoviesRecyclerViewAdapter mFavoriteMoviesAdapter;
    private MoviesRecyclerViewAdapter mMoviesAdapter;
    private Parcelable listState;
    private GridLayoutManager mMoviesLayoutManager;

    @BindView(R.id.movies_rv) RecyclerView mMoviesRV;
    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Log.v("MainActivity", "recreated");
        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(MOVIES_KEY);
            mMoviesAdapter = new MoviesRecyclerViewAdapter(this, mMovies, this);
        }

        mFavoriteMoviesAdapter = new FavoriteMoviesRecyclerViewAdapter(this, null, this);

        mMoviesLayoutManager = new GridLayoutManager(this, numberOfColumns(), GridLayoutManager.VERTICAL, false);
        mMoviesRV.setLayoutManager(mMoviesLayoutManager);

        setupBottomNavigation();

        if (getIntent().hasExtra(RECYCLER_VIEW_STATE_KEY)) {
            if (getIntent().getExtras().getString(RECYCLER_VIEW_STATE_KEY).equals(FAVORITE_MOVIE_DELETE_KEY)) {
                Log.v("MainActivity", "RV state ");
                getSupportLoaderManager().restartLoader(MOVIES_FAVORITE_LOADER_ID, null, moviesCursorLoaderListener);
                mBottomNavigationView.setSelectedItemId(R.id.action_bottom_favorite);
            }
        } else {
            if (InternetUtils.isNetworkAvailable(this)) {

                SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
                String sort = sharedPreferences.getString(getString(R.string.sort_criteria_preference), SORT_MOST_POPULAR);
                Log.v("MainActivity", "Sort order pref " + sort);
                Bundle bundle = new Bundle();
                bundle.putString(getString(R.string.sort_criteria_preference), sort);

                switch (sort) {
                    case SORT_TOP_RATED:
                        getSupportLoaderManager().restartLoader(MOVIES_API_LOADER_ID, bundle, moviesApiLoaderListener);
                        mBottomNavigationView.setSelectedItemId(R.id.action_bottom_top_rated);
                        break;
                    case SORT_MOST_POPULAR:
                        getSupportLoaderManager().restartLoader(MOVIES_API_LOADER_ID, bundle, moviesApiLoaderListener);
                        mBottomNavigationView.setSelectedItemId(R.id.action_bottom_most_popular);
                        break;
                    case SORT_FAVORITE:
                        getSupportLoaderManager().restartLoader(MOVIES_FAVORITE_LOADER_ID, bundle, moviesCursorLoaderListener);
                        mBottomNavigationView.setSelectedItemId(R.id.action_bottom_favorite);
                        break;
                    default:

                }


            } else {
                //TODO handle the lack of internet on the UI
            }
        }




    }

    /********************************************
     * LoaderCallbacks implementations
     ********************************************/

    private LoaderManager.LoaderCallbacks<ArrayList<Movie>> moviesApiLoaderListener = new LoaderManager.LoaderCallbacks<ArrayList<Movie>>() {
        @Override
        public Loader<ArrayList<Movie>> onCreateLoader(int id,final Bundle args) {
            return new AsyncTaskLoader<ArrayList<Movie>>(MainActivity.this) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public ArrayList<Movie> loadInBackground() {
                    if (args != null) {
                        if (args.containsKey(ACTION_RESET_LOADER)) {
                            String action = args.getString(ACTION_RESET_LOADER);
                            String sort = args.getString(getResources().getString(R.string.sort_criteria_preference));
                            Log.v("MAINACTIVITY", "args reset");
                            Log.v("MAINACTIVITY", action);
                            for (String key : args.keySet()) {
                                Log.v("Main test", key + " " + args.get(key).toString());
                            }
                            if (sort != null)
                            Log.v("MAINACTIVITY", sort);
                            switch (action) {
                                case ACTION_TOP_RATED:
                                    return MovieFactory.getTopRatedMovies();
                                case ACTION_POPULAR:
                                    return MovieFactory.getMostPopularMovies();
                                default:
                                    return null;
                            }

                        } else if (args.containsKey(getResources().getString(R.string.sort_criteria_preference))) {
                            Log.v("MAINACTIVITY", "args sort pref");
                            String sort = args.getString(getResources().getString(R.string.sort_criteria_preference));
                            switch (sort) {
                                case SORT_TOP_RATED:
                                    Log.v("MAINACTIVITY", "top rated sort");
                                    return MovieFactory.getTopRatedMovies();
                                case SORT_MOST_POPULAR:
                                    Log.v("MAINACTIVITY", "most popular");
                                    return MovieFactory.getMostPopularMovies();
                                default:
                                    return null;
                            }
                        }

                        }  else {
                        Log.v("MAINACTIVITY", "args null");
                        return MovieFactory.getTopRatedMovies();
                    }
                    Log.v("MAINACTIVITY", "return null");
                    return null;
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<ArrayList<Movie>> loader, ArrayList<Movie> data) {
            if (data != null) {
                mMovies = data;
                MoviesRecyclerViewAdapter adapter = new MoviesRecyclerViewAdapter(MainActivity.this, data, MainActivity.this);
                mMoviesRV.setAdapter(adapter);
            } else {
                Toast.makeText(MainActivity.this, "No data availeble", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        public void onLoaderReset(Loader<ArrayList<Movie>> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Cursor> moviesCursorLoaderListener = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Cursor>(MainActivity.this) {

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
                        return getContentResolver().query(
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
            mFavoriteMoviesAdapter.swapCursor(data);
            mMoviesRV.setAdapter(mFavoriteMoviesAdapter);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mFavoriteMoviesAdapter.swapCursor(null);
        }
    };

    /********************************************
     * Interfaces implementation
     ********************************************/

    @Override
    public void onItemClick(Movie movie, Class targetActivity) {
        Intent i = new Intent(MainActivity.this, targetActivity);
        i.putExtra(getResources().getString(R.string.intent_movie_key), movie);
        startActivity(i);
    }

    /********************************************
     * Activity lifecycle methods
     ********************************************/

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putParcelableArrayList(MOVIES_KEY, mMovies);

        listState = mMoviesLayoutManager.onSaveInstanceState();

        outState.putParcelable(LIST_STATE_KEY, listState);


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            listState = savedInstanceState.getParcelable(LIST_STATE_KEY);
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //mMoviesLayoutManager.onRestoreInstanceState(listState);
        Log.v("MainActivity", "onRestart");
    }


    /********************************************
     * Helper methods
     ********************************************/

    private void resetMoviesLoader(String action) {
        Bundle bundle = new Bundle();
        bundle.putString(ACTION_RESET_LOADER, action);
        switch (action) {
            case ACTION_POPULAR:
                if (InternetUtils.isNetworkAvailable(this)) {
                    getSupportLoaderManager().restartLoader(MOVIES_API_LOADER_ID, bundle, moviesApiLoaderListener);
                } else {
                    //TODO handle the lack of internet on the UI
                }
                break;
            case ACTION_TOP_RATED:
                if (InternetUtils.isNetworkAvailable(this)) {
                    getSupportLoaderManager().restartLoader(MOVIES_API_LOADER_ID, bundle, moviesApiLoaderListener);
                } else {
                    //TODO handle the lack of internet on the UI
                }
                break;
            case ACTION_FAVORITE:
                getSupportLoaderManager().restartLoader(MOVIES_FAVORITE_LOADER_ID, bundle, moviesCursorLoaderListener);
                break;
        }

    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = MOVIE_POSTER_WIDTH * 2;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) {
            return 2;
        } else {
            return nColumns;
        }

    }

    /********************************************
     * UI METHODS
     ********************************************/

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

    private void setupBottomNavigation() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.action_bottom_most_popular:
                        resetMoviesLoader(ACTION_POPULAR);
                        saveSortToSharedPreferences(SORT_MOST_POPULAR);
                        return true;

                    case R.id.action_bottom_top_rated:
                        resetMoviesLoader(ACTION_TOP_RATED);
                        saveSortToSharedPreferences(SORT_TOP_RATED);
                        return true;

                    case R.id.action_bottom_favorite:
                        resetMoviesLoader(ACTION_FAVORITE);
                        saveSortToSharedPreferences(SORT_FAVORITE);
                        return true;
                }

                return false;
            }
        });
    }


    private void saveSortToSharedPreferences(String sort) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sort_criteria_preference), sort);
        editor.apply();
    }

}
