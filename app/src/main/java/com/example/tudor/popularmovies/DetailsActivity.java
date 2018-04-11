package com.example.tudor.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;

import com.example.tudor.popularmovies.adapters.ReviewsRecyclerViewAdapter;
import com.example.tudor.popularmovies.adapters.TrailersRecyclerViewAdapter;
import com.example.tudor.popularmovies.data.Movie;
import com.example.tudor.popularmovies.data.MovieFactory;
import com.example.tudor.popularmovies.data.Review;
import com.example.tudor.popularmovies.data.Trailer;
import com.example.tudor.popularmovies.database.MovieContract;
import com.example.tudor.popularmovies.database.MovieContract.MovieEntry;
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Movie>, AppBarLayout.OnOffsetChangedListener{

    @BindView(R.id.landscape_poster_iv) ImageView mBackdropImageView;
    @BindView(R.id.movie_vote_average_tv) TextView mVoteAverageTextView;
    @BindView(R.id.movie_plot_tv) TextView mPlotTextView;
    @BindView(R.id.trailers_rv) RecyclerView mTrailersRecyclerView;
    @BindView(R.id.reviews_rv) RecyclerView mReviewsRecyclerView;
    @BindView(R.id.movie_vote_count_tv) TextView mVoteCountTextView;
    @BindView(R.id.movie_popularity_tv) TextView mPopularityTextView;
    @BindView(R.id.release_date_tv) TextView mReleaseDateTextView;
    @BindView(R.id.flexible_example_fab) FloatingActionButton mFavoriteFAB;
    @BindView(R.id.details_trailers_cv)  CardView mTrailersCardView;
    @BindView(R.id.movie_toolbar) Toolbar mToolbar;
    @BindView(R.id.movie_appbar) AppBarLayout mAppBar;
    @BindView(R.id.movie_collapsing_toolbar) CollapsingToolbarLayout mCollapsingToolbarLayout;

    private final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private TrailersRecyclerViewAdapter mTrailersRecyclerViewAdapter;
    private ReviewsRecyclerViewAdapter mReviewRecyclerViewAdapter;
    private final String ACTION_LOAD_MOVIE_WITH_ID = "id";
    private final int MOVIE_LOADER_ID = 100;
    private SnapHelper mSnapHelperTrailers;
    private SnapHelper mSnapHelperReviews;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivty_details);

        ButterKnife.bind(this);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mAppBar.addOnOffsetChangedListener(this);


        mSnapHelperTrailers = new GravitySnapHelper(Gravity.START);
        mSnapHelperReviews = new GravitySnapHelper(Gravity.START);

        mFavoriteFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mMovie != null) {
                    ContentValues movieCV = new ContentValues();

                    movieCV.put(MovieEntry.COLUMN_MOVIE_ID, mMovie.getId());
                    movieCV.put(MovieEntry.COLUMN_MOVIE_TITLE, mMovie.getTitle());
                    movieCV.put(MovieEntry.COLUMN_RELEASE_DATE, mMovie.getReleaseDate());
                    movieCV.put(MovieEntry.COLUMN_POSTER_PATH, mMovie.getPosterPath());
                    movieCV.put(MovieEntry.COLUMN_BACKDROP_PATH, mMovie.getBackdropPath());
                    movieCV.put(MovieEntry.COLUMN_VOTE_AVERAGE, mMovie.getVoteAverage());
                    movieCV.put(MovieEntry.COLUMN_VOTE_COUNT, mMovie.getVoteCount());
                    movieCV.put(MovieEntry.COLUMN_POPULARITY, mMovie.getPopularity());
                    movieCV.put(MovieEntry.COLUMN_OVERVIEW, mMovie.getPlot());


                    Uri insertedUri = getContentResolver().insert(MovieContract.CONTENT_URI, movieCV);

                    if (insertedUri != null) {
                        (new DownloadImage(mMovie.getPosterName())).execute(mMovie.getPosterLink());
                        (new DownloadImage(mMovie.getBackdropName())).execute(mMovie.getBackdropLink());
                        Toast.makeText(DetailsActivity.this,insertedUri.toString(), Toast.LENGTH_SHORT).show();
                    }


                }
            }
        });


        try {
            mMovie = getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_movie_key));

            Bundle movieCall = new Bundle();
            movieCall.putString(ACTION_LOAD_MOVIE_WITH_ID, String.valueOf(mMovie.getId()));

            getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, movieCall, this);

            // Picasso image loading
                Picasso.with(this)
                        .load(mMovie.getBackdropLink())
                        .placeholder(R.drawable.walpaper_placeholder)
                        .error(R.drawable.walpaper_error)
                        .into(mBackdropImageView);

            Toast.makeText(this, getFileStreamPath(mMovie.getBackdropName()).toString(), Toast.LENGTH_LONG).show();

            //ImageUtils.loadMovieImageFromStorage(this, mMovie.getBackdropPath(), mBackdropImageView);


            mVoteAverageTextView.setText(String.valueOf(mMovie.getVoteAverage()));
            mPlotTextView.setText(mMovie.getPlot());
            mCollapsingToolbarLayout.setTitle(mMovie.getTitle());
            mReleaseDateTextView.setText(getResources().getString(R.string.release_date) + mMovie.getReleaseDate());
            mVoteCountTextView.setText(String.valueOf(mMovie.getVoteCount()));
            mPopularityTextView.setText(String.format("%.0f", mMovie.getRoundPopularity()));
            mTrailersRecyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            mSnapHelperTrailers.attachToRecyclerView(mTrailersRecyclerView);
            mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
            mSnapHelperReviews.attachToRecyclerView(mReviewsRecyclerView);

        } catch (NullPointerException npe) {
            npe.printStackTrace();
            //TODO implement error handling on the UI
        }

    }


    @Override
    public Loader<Movie> onCreateLoader(int id, final Bundle args) {
        return new AsyncTaskLoader<Movie>(this) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Movie loadInBackground() {

                if (args != null) {
                    String id = args.getString(ACTION_LOAD_MOVIE_WITH_ID);

                    return MovieFactory.movieWithId(id);

                }

                return null;
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Movie> loader, Movie movie) {
        mMovie = movie;
        mTrailersRecyclerView.setHasFixedSize(true);
        mReviewsRecyclerView.setHasFixedSize(true);
        if (movie == null) Log.v("DETAILS ACTVIVITY", "Movie is null");
        ArrayList<Trailer> trailers = movie.getTrailers();
        ArrayList<Review> reviews = movie.getReviews();
        Log.v("DETAILS ACTVIVITY", "Movie loaded");
        Log.v("DETAILS ACTVIVITY", String.valueOf(trailers.size()));
        if (trailers.size() > 0) {
            mTrailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(trailers, DetailsActivity.this);
            mTrailersRecyclerView.setAdapter(mTrailersRecyclerViewAdapter);
        } else {
            mTrailersCardView.setVisibility(View.GONE);
        }

        if (reviews.size() > 0) {
            mReviewRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(reviews, DetailsActivity.this);
            mReviewsRecyclerView.setAdapter(mReviewRecyclerViewAdapter);
        }

    }

    @Override
    public void onLoaderReset(Loader<Movie> loader) {

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

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private String TAG = "DownloadImage";
        private String mName;

        public DownloadImage(String imageName) {
            this.mName = imageName;
        }

        private Bitmap downloadImageBitmap(String sUrl) {
            Bitmap bitmap = null;
            try {
                InputStream inputStream = new URL(sUrl).openStream();   // Download Image from URL
                bitmap = BitmapFactory.decodeStream(inputStream);       // Decode Bitmap
                inputStream.close();
            } catch (Exception e) {
                Log.d(TAG, "Exception 1, Something went wrong!");
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImageBitmap(params[0]);
        }

        protected void onPostExecute(Bitmap result) {
            saveImage(getApplicationContext(), result, mName);
        }
    }

    public void saveImage(Context context, Bitmap b, String imageName) {
        FileOutputStream foStream;
        try {
            foStream = context.openFileOutput(imageName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, foStream);
            foStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 2, Something went wrong!");
            e.printStackTrace();
        }
    }

    public Bitmap loadImageBitmap(Context context, String imageName) {
        Bitmap bitmap = null;
        FileInputStream fiStream;
        try {
            fiStream    = context.openFileInput(imageName);
            bitmap      = BitmapFactory.decodeStream(fiStream);
            fiStream.close();
        } catch (Exception e) {
            Log.d("saveImage", "Exception 3, Something went wrong!");
            e.printStackTrace();
        }
        return bitmap;
    }

}
