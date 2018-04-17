package com.example.tudor.popularmovies;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import com.example.tudor.popularmovies.fragments.FavoriteMoviesFragment;
import com.example.tudor.popularmovies.fragments.MostPopularMoviesFragment;
import com.example.tudor.popularmovies.fragments.TopRatedMoviesFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final String RECYCLER_VIEW_STATE_KEY = "state";
    private final String MOVIE_FRAGMENT_LOADED_KEY = "movie_loaded_key";
    private final String MOVIE_FRAGMENT_LOADED = "movie_loaded";
    private final String SORT_MOST_POPULAR = "most_popular";
    private final String SORT_TOP_RATED = "top_rated";
    private final String SORT_FAVORITE = "favorite";



    @BindView(R.id.bottom_navigation) BottomNavigationView mBottomNavigationView;
    @BindView(R.id.fragment_container) FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupBottomNavigation();

        if (savedInstanceState == null) {
            loadFragmentFromPreference();
        }


    }


    private void setupBottomNavigation() {

        mBottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_bottom_most_popular:
                        replaceFragment(new MostPopularMoviesFragment());
                        saveSortToSharedPreferences(SORT_MOST_POPULAR);
                        return true;

                    case R.id.action_bottom_top_rated:
                        replaceFragment(new TopRatedMoviesFragment());
                        saveSortToSharedPreferences(SORT_TOP_RATED);
                        return true;

                    case R.id.action_bottom_favorite:
                        replaceFragment(new FavoriteMoviesFragment());
                        saveSortToSharedPreferences(SORT_FAVORITE);
                        return true;
                }

                return false;
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_container, fragment);
        ft.commit();
    }

    private void loadFragmentFromPreference() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        String sort = sharedPreferences.getString(getString(R.string.sort_criteria_preference), SORT_MOST_POPULAR);

        switch (sort) {
            case SORT_MOST_POPULAR:
                replaceFragment(new MostPopularMoviesFragment());
                mBottomNavigationView.setSelectedItemId(R.id.action_bottom_most_popular);
                break;

            case SORT_TOP_RATED:
                replaceFragment(new TopRatedMoviesFragment());
                mBottomNavigationView.setSelectedItemId(R.id.action_bottom_top_rated);
                break;

            case SORT_FAVORITE:
                replaceFragment(new FavoriteMoviesFragment());
                mBottomNavigationView.setSelectedItemId(R.id.action_bottom_favorite);
                break;
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(MOVIE_FRAGMENT_LOADED_KEY, MOVIE_FRAGMENT_LOADED);
    }

    private void saveSortToSharedPreferences(String sort) {
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.sort_criteria_preference), sort);
        editor.apply();
    }

}
