package com.example.tudor.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tudor.popularmovies.data.Movie;
import com.squareup.picasso.Picasso;

public class DetailsActivity extends AppCompatActivity {

    private ImageView mBackdropImageView;
    private TextView mVoteAverageTextView;
    private TextView mMovieTitleTextView;
    private TextView mPlotTextView;


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

        mBackdropImageView = findViewById(R.id.landscape_poster_iv);
        mVoteAverageTextView = findViewById(R.id.movie_vote_average_tv);
        mMovieTitleTextView = findViewById(R.id.movie_title_tv);
        mPlotTextView = findViewById(R.id.movie_plot_tv);


        try {
            Movie m = getIntent().getExtras().getParcelable(getResources().getString(R.string.intent_movie_key));
            String poster = m.getBackdropLink();
            double voteAverage = m.getVoteAverage();
            String movieTitleAndYear = m.getTitleAndYear();
            String plot = m.getPlot();

            Picasso.with(this).load(poster).into(mBackdropImageView);

            mVoteAverageTextView.setText(String.valueOf(voteAverage));
            mMovieTitleTextView.setText(movieTitleAndYear);
            mPlotTextView.setText(plot);

        } catch (NullPointerException npe) {
            npe.printStackTrace();
            //TODO implement errorhandling on the UI
        }





    }



}
