package com.example.tudor.popularmovies.utils;

import android.util.Log;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by tudor on 04.03.2018.
 */

public class NetworkUtils {

    private static String API_KEY = ApiUtils.getApiKey(); // Replace with your api key
    private static String API_KEY_QUERY_PARAM = "?api_key=";
    private static final String BASE_URL = "http://api.themoviedb.org/3";
    private static final String MOST_POPULAR_ENDPOINT = "/movie/popular";
    private static final String TOP_RATED_ENDPOINT = "/movie/top_rated";

    private static final String MOST_POPULAR_URL = BASE_URL + MOST_POPULAR_ENDPOINT + API_KEY_QUERY_PARAM + API_KEY;
    private static final String TOP_RATED_URL = BASE_URL + TOP_RATED_ENDPOINT + API_KEY_QUERY_PARAM + API_KEY;

    public static String getResponseFromHttpUrl(URL url) {

        OkHttpClient client = new OkHttpClient();

        Request.Builder builder = new Request.Builder();
        builder.url(url);

        Request request = builder.build();

        try {
            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            return null;
        }

    }

    public static String getMostPopularRawJson() {
            return getResponseFromHttpUrl(buildUrlFromString(MOST_POPULAR_URL));
    }

    public static String getTopRatedRawJson() {
        Log.v("NETOWK_URILS: ", TOP_RATED_URL);
        return getResponseFromHttpUrl(buildUrlFromString(TOP_RATED_URL));
    }

    private static URL buildUrlFromString (String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
