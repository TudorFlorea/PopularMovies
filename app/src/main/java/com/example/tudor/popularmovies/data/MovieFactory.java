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

    public static ArrayList<Movie> getTopRatedMovies() {

        ArrayList<Movie> moviesList = new ArrayList<>();

        try {
            JSONObject topRatedMoviesJson = JsonUtils.jsonObjectFromString(NetworkUtils.getTopRatedRawJson());

            JSONArray topRatedMoviesResults = topRatedMoviesJson.getJSONArray("results");


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


    public static ArrayList<Movie> getMostPopularMovies() {
        ArrayList<Movie> moviesList = new ArrayList<>();

        try {
            JSONObject topRatedMoviesJson = JsonUtils.jsonObjectFromString(NetworkUtils.getMostPopularRawJson());

            JSONArray topRatedMoviesResults = topRatedMoviesJson.getJSONArray("results");


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

    private static Movie extractDataFromJsonObject(JSONObject movieJsonObj) {
        try {
            long id = movieJsonObj.getLong("id");
            String title = movieJsonObj.getString("title");
            String releseDate = movieJsonObj.getString("release_date");
            String posterPath = movieJsonObj.getString("poster_path");
            String backdropPath = movieJsonObj.getString("backdrop_path");
            double voteAverage = movieJsonObj.getDouble("vote_average");
            String plot = movieJsonObj.getString("overview");

            return new Movie(id, title, releseDate, posterPath, backdropPath, voteAverage, plot);
        } catch (JSONException jse) {
            jse.printStackTrace();
            return null;
        }
    }

}
