package com.example.tudor.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.LinearLayout;
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
import com.example.tudor.popularmovies.utils.InternetUtils;
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
    @BindView(R.id.movie_sv) NestedScrollView mNestedScrollView;
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
    @BindView(R.id.details_no_internet_tv) TextView mNoInternetTextView;
    @BindView(R.id.details_no_internet_layout) LinearLayout mNoInternetLayout;

    private final int PERCENTAGE_TO_SHOW_IMAGE = 20;
    private TrailersRecyclerViewAdapter mTrailersRecyclerViewAdapter;
    private ReviewsRecyclerViewAdapter mReviewRecyclerViewAdapter;
    private final String ACTION_LOAD_MOVIE_WITH_ID = "id";
    private final String MOVIE_KEY = "movie";
    private final String TRAILERS_STATE_KEY = "trailers";
    private final String REVIEWS_STATE_KEY = "reviews";
    private final int MOVIE_LOADER_ID = 100;
    private SnapHelper mSnapHelperTrailers;
    private SnapHelper mSnapHelperReviews;
    private int mMaxScrollSize;
    private boolean mIsImageHidden;
    private Parcelable mTrailersListState;
    private Parcelable mReviewsListState;
    private LinearLayoutManager mTrailersLayoutManager;
    private LinearLayoutManager mReviewsLayoutManager;

    private Movie mMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.acitivty_details);

        ButterKnife.bind(this);

        setupAppBar();

        setupFAB();

        setupUI(savedInstanceState);

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
        ArrayList<Trailer> trailers = movie.getTrailers();
        ArrayList<Review> reviews = movie.getReviews();

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

    private void setupAppBar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });

        mAppBar.addOnOffsetChangedListener(this);

    }

    private void setupFAB() {
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
    }

    private void noInternetUI(final Bundle savedInstanceState) {
        mAppBar.setVisibility(View.GONE);
        mNestedScrollView.setVisibility(View.GONE);
        mFavoriteFAB.setVisibility(View.GONE);
        mNoInternetLayout.setVisibility(View.VISIBLE);
        mNoInternetLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setupUI(savedInstanceState);
            }
        });
    }

    private void setupMainUI() {

        mAppBar.setVisibility(View.VISIBLE);
        mNestedScrollView.setVisibility(View.VISIBLE);
        mFavoriteFAB.setVisibility(View.VISIBLE);
        mNoInternetLayout.setVisibility(View.GONE);

        // Picasso image loading
        Picasso.with(this)
                .load(mMovie.getBackdropLink())
                .placeholder(R.drawable.walpaper_placeholder)
                .error(R.drawable.walpaper_error)
                .into(mBackdropImageView);

        //Movie title
        mCollapsingToolbarLayout.setTitle(mMovie.getTitle());

        //Movie plot
        mPlotTextView.setText(mMovie.getPlot());

        //Release date
        mReleaseDateTextView.setText(getResources().getString(R.string.release_date) + mMovie.getReleaseDate());

        //Average vote
        mVoteAverageTextView.setText(String.valueOf(mMovie.getVoteAverage()));

        //Vote count

        mVoteCountTextView.setText(String.valueOf(mMovie.getVoteCount()));

        //Popularity
        mPopularityTextView.setText(String.format("%.0f", mMovie.getRoundPopularity()));

        //Trailers RV
        mTrailersLayoutManager = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mTrailersRecyclerView.setLayoutManager(mTrailersLayoutManager);
        mTrailersRecyclerView.setHasFixedSize(true);
        if (mMovie != null) {
            mTrailersRecyclerViewAdapter = new TrailersRecyclerViewAdapter(mMovie.getTrailers(), DetailsActivity.this);
            mTrailersRecyclerView.setAdapter(mTrailersRecyclerViewAdapter);
        }

        mSnapHelperTrailers = new GravitySnapHelper(Gravity.START);
        mSnapHelperTrailers.attachToRecyclerView(mTrailersRecyclerView);

        //Reviews RV
        mReviewsLayoutManager = new LinearLayoutManager(DetailsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        mReviewsRecyclerView.setLayoutManager(mReviewsLayoutManager);
        mReviewsRecyclerView.setHasFixedSize(true);
        if (mMovie != null) {
            mReviewRecyclerViewAdapter = new ReviewsRecyclerViewAdapter(mMovie.getReviews(), DetailsActivity.this);
            mReviewsRecyclerView.setAdapter(mReviewRecyclerViewAdapter);
        }


        mSnapHelperReviews = new GravitySnapHelper(Gravity.START);
        mSnapHelperReviews.attachToRecyclerView(mReviewsRecyclerView);

    }

    private void setupUI(Bundle savedInstanceState) {
        if (InternetUtils.isNetworkAvailable(this)) {
            try {

                if (savedInstanceState == null) {
                    mMovie = getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_movie_key));

                    Bundle movieCall = new Bundle();
                    movieCall.putString(ACTION_LOAD_MOVIE_WITH_ID, String.valueOf(mMovie.getId()));

                    getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID, movieCall, this);
                } else {
                    mMovie = savedInstanceState.getParcelable(MOVIE_KEY);

                }


                setupMainUI();

            } catch (NullPointerException npe) {
                npe.printStackTrace();
            }
        } else {
            noInternetUI(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        mTrailersListState = mTrailersLayoutManager.onSaveInstanceState();
        mReviewsListState = mReviewsLayoutManager.onSaveInstanceState();

        outState.putParcelable(MOVIE_KEY, mMovie);
        outState.putParcelable(TRAILERS_STATE_KEY, mTrailersListState);
        outState.putParcelable(REVIEWS_STATE_KEY, mReviewsListState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        if (savedInstanceState != null) {
            mTrailersListState = savedInstanceState.getParcelable(TRAILERS_STATE_KEY);
            mReviewsListState = savedInstanceState.getParcelable(REVIEWS_STATE_KEY);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();

        mTrailersLayoutManager.onRestoreInstanceState(mTrailersListState);
        mReviewsLayoutManager.onRestoreInstanceState(mReviewsListState);
    }
}
