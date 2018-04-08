package com.example.tudor.popularmovies;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tudor.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.details_layout) FrameLayout mDetailsLayout;
    @BindView(R.id.landscape_poster_iv) ImageView mBackdropImageView;
    @BindView(R.id.movie_vote_average_tv) TextView mVoteAverageTextView;
    @BindView(R.id.movie_title_tv) TextView mMovieTitleTextView;
    @BindView(R.id.movie_plot_tv) TextView mPlotTextView;
    @BindView(R.id.action_favorite_movie) FloatingActionButton mFavoriteMovieFAB;


    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        try {
            getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
            getSupportActionBar().hide();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }


        setContentView(R.layout.activity_details);

        ButterKnife.bind(this);

        mFavoriteMovieFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DetailsActivity.this, "Movie Favorite", Toast.LENGTH_SHORT).show();
            }
        });


        try {
            mMovie = getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_movie_key));
            String poster = mMovie.getBackdropLink();
            double voteAverage = mMovie.getVoteAverage();
            String movieTitleAndYear = mMovie.getTitleAndYear();
            String plot = mMovie.getPlot();
            String backgroundImage = mMovie.getPosterLink();

            // Picasso image loading
            Picasso.with(this)
                    .load(poster)
                    .placeholder(R.drawable.walpaper_placeholder)
                    .error(R.drawable.walpaper_error)
                    .into(mBackdropImageView);

            /**
             *

            Picasso.with(this)
                    .load(backgroundImage)
                    .into(new Target() {
                        @Override
                        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                            mDetailsLayout.setBackground(new BitmapDrawable(getResources(), bitmap));
                        }

                        @Override
                        public void onBitmapFailed(Drawable errorDrawable) {

                        }

                        @Override
                        public void onPrepareLoad(Drawable placeHolderDrawable) {

                        }
                    });

            */

            mVoteAverageTextView.setText(String.valueOf(voteAverage));
            mMovieTitleTextView.setText(movieTitleAndYear);
            mPlotTextView.setText(plot);

        } catch (NullPointerException npe) {
            npe.printStackTrace();
            //TODO implement error handling on the UI
        }





    }



}
