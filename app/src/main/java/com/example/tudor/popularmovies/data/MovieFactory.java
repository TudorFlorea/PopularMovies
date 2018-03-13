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

                moviesList.add(extractDataFromJsonObject(movieJson));

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

                moviesList.add(extractDataFromJsonObject(movieJson));

            }

            return moviesList;
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

    /**
     *
     * @param movieJsonObj
     * @return Movie object
     */

    private static Movie extractDataFromJsonObject(JSONObject movieJsonObj) {
        try {
            long id = movieJsonObj.optLong(MOVIE_ID);
            String title = movieJsonObj.getString(MOVIE_TITLE);
            String releseDate = movieJsonObj.optString(MOVIE_RELEASE_DATE);
            String posterPath = movieJsonObj.optString(MOVIE_POSTER_PATH);
            String backdropPath = movieJsonObj.optString(MOVIE_BACKDROP_PATH);
            double voteAverage = movieJsonObj.optDouble(MOVIE_VOTE_AVERAGE);
            String plot = movieJsonObj.optString(MOVIE_OVERVIEW);

            return new Movie(id, title, releseDate, posterPath, backdropPath, voteAverage, plot);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

}
