package com.example.tudor.popularmovies;

import android.content.Intent;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tudor.popularmovies.adapters.TrailersRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.database.MovieContract;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {

    @BindView(R.id.landscape_poster_iv) ImageView mBackdropImageView;
    @BindView(R.id.movie_vote_average_tv) TextView mVoteAverageTextView; //
    @BindView(R.id.movie_plot_tv) TextView mPlotTextView; //
    @BindView(R.id.movie_vote_count_tv) TextView mVoteCountTextView; //
    @BindView(R.id.movie_popularity_tv) TextView mPopularityTextView; //
    @BindView(R.id.release_date_tv) TextView mReleaseDateTextView; //
    @BindView(R.id.flexible_example_fab) FloatingActionButton mFavoriteFAB;
    @BindView(R.id.movie_toolbar) Toolbar mToolbar; //
    @BindView(R.id.movie_appbar) AppBarLayout mAppBar; //
    @BindView(R.id.movie_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout; //

    private final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private final String ACTION_LOAD_MOVIE_WITH_ID = "id";
    private final int DELETE_MOVIE_LOADER_ID = 100;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private Movie mMovie;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_favorite);

        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mAppBar.addOnOffsetChangedListener(this);

        mFavoriteFAB.setImageResource(R.drawable.ic_delete_white);

        mFavoriteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // delete image
                getSupportLoaderManager().restartLoader(DELETE_MOVIE_LOADER_ID, null, deleteMovieListener);
            }
        });

        try {

            if (getIntent().hasExtra(getResources().getString(R.string.intent_movie_key))) {
                mMovie = getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_movie_key));

                Bundle movieCall = new Bundle();
                movieCall.putString(ACTION_LOAD_MOVIE_WITH_ID, String.valueOf(mMovie.getId()));

                populateUi();
            }




        } catch (NullPointerException npe) {
            npe.printStackTrace();
            //TODO implement error handling on the UI
        }


    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

        if (mMaxScrollSize == 0) mMaxScrollSize = appBarLayout.getTotalScrollRange();

        int currentScrollPercentage = (Math.abs(verticalOffset) * 100 / mMaxScrollSize);

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            mIsImageHidden = true;

            ViewCompat.animate(mFavoriteFAB).scaleY(0).scaleX(0).start();
        } else {
            if (mIsImageHidden) {
                mIsImageHidden = false;
                ViewCompat.animate(mFavoriteFAB).scaleY(1).scaleX(1).start();
            }
        }

    }

    private void populateUi() {
        if (mMovie != null) {
            // Picasso image loading
            Picasso.with(this)
                    .load(getFileStreamPath(mMovie.getBackdropName()))
                    .placeholder(R.drawable.walpaper_placeholder)
                    .error(R.drawable.walpaper_error)
                    .into(mBackdropImageView);

            //Toast.makeText(this, getFileStreamPath(mMovie.getBackdropName()).toString(), Toast.LENGTH_LONG).show();

            mVoteAverageTextView.setText(String.valueOf(mMovie.getVoteAverage()));
            mPlotTextView.setText(mMovie.getPlot());
            mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
            mReleaseDateTextView.setText(getResources().getString(R.string.release_date) + mMovie.getReleaseDate());
            mVoteCountTextView.setText(String.valueOf(mMovie.getVoteCount()));
            mPopularityTextView.setText(String.format("%.0f", mMovie.getRoundPopularity()));
        }
    }

    LoaderManager.LoaderCallbacks<Cursor> testCb = new LoaderManager.LoaderCallbacks<Cursor>() {
        @Override
        public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
            return new AsyncTaskLoader<Cursor>(FavoriteActivity.this) {

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
                                Uri.withAppendedPath(MovieContract.CONTENT_URI, String.valueOf(mMovie.getDatabaseID())),
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
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            cursor.moveToFirst();
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(MovieContract.MovieEntry._ID));

            Toast.makeText(FavoriteActivity.this, "ID: " + String.valueOf(id), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    LoaderManager.LoaderCallbacks<Integer> deleteMovieListener = new LoaderManager.LoaderCallbacks<Integer>() {
        @Override
        public Loader<Integer> onCreateLoader(int id, Bundle args) {
            return new AsyncTaskLoader<Integer>(FavoriteActivity.this) {

                @Override
                protected void onStartLoading() {
                    super.onStartLoading();
                    forceLoad();
                }

                @Override
                public Integer loadInBackground() {
                    try {
                        return Integer.valueOf(getContentResolver().delete(
                                Uri.withAppendedPath(MovieContract.CONTENT_URI, String.valueOf(mMovie.getDatabaseID())),
                                null,
                                null

                        ));
                    } catch (Exception e) {
                        e.printStackTrace();
                        return null;
                    }
                }
            };
        }

        @Override
        public void onLoadFinished(Loader<Integer> loader, Integer data) {
            Toast.makeText(FavoriteActivity.this, String.valueOf(data), Toast.LENGTH_SHORT).show();
            if (data > 0) {
                Intent i = new Intent(FavoriteActivity.this, MainActivity.class);
                i.putExtra("state", "favorite_movie_delete");
                startActivity(i);
                finish();
            }
        }

        @Override
        public void onLoaderReset(Loader<Integer> loader) {

        }
    };

}
