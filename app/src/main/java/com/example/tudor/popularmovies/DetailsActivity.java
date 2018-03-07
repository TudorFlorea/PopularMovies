package com.example.tudor.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.tudor.popularmovies.data.Movie;

public class DetailsActivity extends AppCompatActivity {

    private TextView testTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        testTV = findViewById(R.id.test_content);

        Movie m = getIntent().getExtras().getParcelable("movie");
        String title = m.getTitle();

        testTV.setText(title);

    }
}
