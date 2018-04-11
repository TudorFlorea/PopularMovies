package com.example.tudor.popularmovies.database;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by tudor on 09.04.2018.
 */

public class MovieContract {

    public static final String AUTHORITY = "com.example.tudor.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

    public static final class MovieEntry implements BaseColumns {

        //public Movie(long id, String title, String releaseDate, String posterPath, String backdropPath, double voteAverage, String plot,
        // long voteCount, double popularity, ArrayList<Review> reviews, ArrayList<Trailer> trailers)

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_MOVIE_ID = "id";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_VOTE_COUNT = "vote_count";
        public static final String COLUMN_POPULARITY = "popularity";

    }

}
