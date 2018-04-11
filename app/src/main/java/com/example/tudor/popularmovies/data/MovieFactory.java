package com.example.tudor.popularmovies.data;

import android.util.Log;

import com.example.tudor.popularmovies.utils.JsonUtils;
import com.example.tudor.popularmovies.utils.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by tudor on 04.03.2018.
 */

public class MovieFactory {

    private static final String MOVIES_RESULTS = "results";
    private static final String MOVIE_ID = "id";
    private static final String MOVIE_TITLE = "title";
    private static final String MOVIE_RELEASE_DATE = "release_date";
    private static final String MOVIE_POSTER_PATH = "poster_path";
    private static final String MOVIE_BACKDROP_PATH = "backdrop_path";
    private static final String MOVIE_VOTE_AVERAGE = "vote_average";
    private static final String MOVIE_OVERVIEW = "overview";
    private static final String MOVIE_VOTE_COUNT = "vote_count";
    private static final String MOVIE_POPULARITY = "popularity";
    private static final String MOVIE_REVIEWS = "reviews";
    private static final String REVIEW_RESULTS = "results";
    private static final String REVIEW_AUTHOR = "author";
    private static final String REVIEW_CONTENT = "content";
    private static final String REVIEW_ID = "id";
    private static final String REVIEW_URL = "url";
    private static final String MOVIE_VIDEOS = "videos";
    private static final String VIDEO_RESULTS = "results";
    private static final String VIDEO_ID = "id";
    private static final String VIDEO_KEY = "key";
    private static final String VIDEO_NAME = "name";
    private static final String VIDEO_SITE = "site";


    /**
     * function that makes a HTTP request to get the top rated movies and makes Movie objects out of the json and adds them in an ArrayList
     * @return ArrayList with top rated movies
     */

    public static ArrayList<Movie> getTopRatedMovies() {

        ArrayList<Movie> moviesList = new ArrayList<>();

        try {
            JSONObject topRatedMoviesJson = JsonUtils.jsonObjectFromString(NetworkUtils.getTopRatedRawJson());

            JSONArray topRatedMoviesResults = topRatedMoviesJson.optJSONArray(MOVIES_RESULTS);


            for (int i = 0; i < topRatedMoviesResults.length(); i++) {
                JSONObject movieJson = topRatedMoviesResults.getJSONObject(i);

                moviesList.add(extractMovieFromJsonObject(movieJson));

            }

            return moviesList;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }

    }


    /**
     * function that makes a HTTP request to get the popular movies movies and makes Movie objects out of the json and adds them in an ArrayList
     * @return ArrayList with popular movies
     */
    public static ArrayList<Movie> getMostPopularMovies() {
        ArrayList<Movie> moviesList = new ArrayList<>();

        try {
            JSONObject topRatedMoviesJson = JsonUtils.jsonObjectFromString(NetworkUtils.getMostPopularRawJson());

            JSONArray topRatedMoviesResults = topRatedMoviesJson.getJSONArray(MOVIES_RESULTS);


            for (int i = 0; i < topRatedMoviesResults.length(); i++) {
                JSONObject movieJson = topRatedMoviesResults.getJSONObject(i);

                moviesList.add(extractMovieFromJsonObject(movieJson));

            }

            return moviesList;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }


    public static Movie movieWithId(String id) {

            JSONObject movieJson = JsonUtils.jsonObjectFromString(NetworkUtils.getMovieRawJson(id));

            String stringTag = movieJson == null ? "Json is null" :  movieJson.toString();

        Log.v("MOVIE FACTORY: ", stringTag);

            return extractMovieFromJsonObject(movieJson);
    }

    /**
     *
     * @param movieJsonObj
     * @return Movie object
     */

    private static Movie extractMovieFromJsonObject(JSONObject movieJsonObj) {

            long id;
            String title;
            String releseDate;
            String posterPath ;
            String backdropPath;
            double voteAverage;
            String plot;
            long voteCount;
            double popularity;
            ArrayList<Trailer> trailers = new ArrayList<>();
            ArrayList<Review> reviews = new ArrayList<>();

            id = movieJsonObj.has(MOVIE_ID) ? movieJsonObj.optLong(MOVIE_ID) : 0;
            title = movieJsonObj.has(MOVIE_TITLE) ? movieJsonObj.optString(MOVIE_TITLE) : null;
            releseDate = movieJsonObj.has(MOVIE_RELEASE_DATE) ? movieJsonObj.optString(MOVIE_RELEASE_DATE) : null;
            posterPath = movieJsonObj.has(MOVIE_POSTER_PATH) ? movieJsonObj.optString(MOVIE_POSTER_PATH) : null;
            backdropPath = movieJsonObj.has(MOVIE_BACKDROP_PATH) ? movieJsonObj.optString(MOVIE_BACKDROP_PATH) : null;
            voteAverage = movieJsonObj.has(MOVIE_VOTE_AVERAGE) ? movieJsonObj.optDouble(MOVIE_VOTE_AVERAGE) : 0;
            plot = movieJsonObj.has(MOVIE_OVERVIEW) ? movieJsonObj.optString(MOVIE_OVERVIEW) : null;
            voteCount = movieJsonObj.has(MOVIE_VOTE_COUNT) ? movieJsonObj.optLong(MOVIE_VOTE_COUNT) : 0;
            popularity = movieJsonObj.has(MOVIE_POPULARITY) ? movieJsonObj.optDouble(MOVIE_POPULARITY) : 0;

            if (movieJsonObj.has(MOVIE_REVIEWS)) {
                JSONObject reviewsJsonObject = movieJsonObj.optJSONObject(MOVIE_REVIEWS);
                if (reviewsJsonObject.has(REVIEW_RESULTS)) {
                    reviews = extractReviewsFromJsonArray(reviewsJsonObject.optJSONArray(REVIEW_RESULTS));
                }

            }

            if (movieJsonObj.has(MOVIE_VIDEOS)) {
                JSONObject videosJsonObject = movieJsonObj.optJSONObject(MOVIE_VIDEOS);
                if (videosJsonObject.has(VIDEO_RESULTS)) {
                    trailers = extractTrailersFromJsonArray(videosJsonObject.optJSONArray(VIDEO_RESULTS));
                }
            }

            return new Movie(id, title, releseDate, posterPath, backdropPath, voteAverage, plot, voteCount, popularity, reviews, trailers);

    }

    private static ArrayList<Review> extractReviewsFromJsonArray(JSONArray movieReviews) {

            ArrayList<Review> reviews = new ArrayList<>();

            JSONObject movieReview;
            String author;
            String content;
            String id;
            String url;

            for (int i = 0; i < movieReviews.length(); i++) {

                movieReview = movieReviews.optJSONObject(i);

                author = movieReview.optString(REVIEW_AUTHOR, "");
                content = movieReview.optString(REVIEW_CONTENT, "");
                id = movieReview.optString(REVIEW_ID, "");
                url = movieReview.optString(REVIEW_URL, "");

                reviews.add(new Review(id, author, content, url));

            }

        return reviews;
    }

    private static ArrayList<Trailer> extractTrailersFromJsonArray(JSONArray movieTrailers) {

        ArrayList<Trailer> trailers = new ArrayList<>();

        JSONObject movieTrailer;
        String id;
        String key;
        String name;
        String site;

        for (int i = 0; i < movieTrailers.length(); i++) {

            movieTrailer = movieTrailers.optJSONObject(i);

            id = movieTrailer.optString(VIDEO_ID, "");
            key = movieTrailer.optString(VIDEO_KEY, "");
            name = movieTrailer.optString(VIDEO_NAME, "");
            site = movieTrailer.optString(VIDEO_SITE, "");

            trailers.add(new Trailer(id, key, name, site));

        }

        return trailers;
    }

}
