package com.example.tudor.popularmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.example.tudor.popularmovies.data.Review;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewActivity extends AppCompatActivity {

    @BindView(R.id.review_full_content_tv) TextView mReviewContentTextView;
    @BindView(R.id.review_full_content_author_tv) TextView mReviewAuthorTextView;
    @BindView(R.id.review_full_content_fab) FloatingActionButton mFab;

    private final String REVIEW_CONTENT = "content";
    private final String REVIEW_AUTHOR = "author";
    private final String REVIEW_URL = "url";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        ButterKnife.bind(this);

        if (getIntent().hasExtra(REVIEW_CONTENT) && getIntent().hasExtra(REVIEW_AUTHOR) && getIntent().hasExtra(REVIEW_URL)) {
            String content = getIntent().getExtras().getString(REVIEW_CONTENT);
            String author = getIntent().getExtras().getString(REVIEW_AUTHOR);
            final String url =  getIntent().getExtras().getString(REVIEW_URL);
            mReviewContentTextView.setText(content);
            mReviewAuthorTextView.setText(author);
            mFab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                }
            });
        }



    }

}
