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
            JSONObject topRatedMoviesJson = JsonUtils.jsonObjectFromString(NetworkUtils.getTestUrl());

            Log.v("MovieFactory: ", "1");

            JSONArray topRatedMoviesResults = topRatedMoviesJson.getJSONArray("results");

            Log.v("MovieFactory: ", "2");

            for (int i = 0; i < topRatedMoviesResults.length(); i++) {
                JSONObject movieJson = topRatedMoviesResults.getJSONObject(i);
                long id = movieJson.getLong("id");
                String title = movieJson.getString("title");
                String releseDate = movieJson.getString("release_date");
                String posterPath = movieJson.getString("poster_path");
                double voteAverage = movieJson.getDouble("vote_average");
                String plot = movieJson.getString("overview");

                moviesList.add(new Movie(id, title, releseDate, posterPath, voteAverage, plot));

            }
            Log.v("MovieFactory: ", "RETURNED ARRAY LIST");

            return moviesList;
        } catch (JSONException jse) {
            jse.printStackTrace();
            Log.v("MovieFactory: ", "NULL");
            return null;
        }


    }

}
