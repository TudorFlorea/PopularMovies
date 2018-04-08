package com.example.tudor.popularmovies.utils;

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
    private static final String MOVIE_ENDPOINT = "/movie/";
    private static final String VIDEOS = "videos";
    private static final String REVIEWS = "reviews";
    private static final String APPED_TO_RESPONSE = "&append_to_response";

    private static final String MOST_POPULAR_URL = BASE_URL + MOST_POPULAR_ENDPOINT + API_KEY_QUERY_PARAM + API_KEY;
    private static final String TOP_RATED_URL = BASE_URL + TOP_RATED_ENDPOINT + API_KEY_QUERY_PARAM + API_KEY;

    /**
     * function that makes a HTTP request using the OkHttp library and returns the raw String response from the request
     * @param url - api call URL
     * @return String - the raw Json response from the HTTP request
     */

    private static String getResponseFromHttpUrl(URL url) {

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

    public static String getMovieRawJson(String id) {
        //https://api.themoviedb.org/3/movie/284054?api_key=ccb006f083a8e8709345d592bf884d6f&append_to_response=videos,reviews

        String movieUrl = BASE_URL + MOVIE_ENDPOINT + id + API_KEY_QUERY_PARAM + API_KEY + APPED_TO_RESPONSE + VIDEOS + "," + REVIEWS;

        return getResponseFromHttpUrl(buildUrlFromString(movieUrl));

    }

    /**
     * function that makes an api call to retrieve the most popular movies Json
     * @return raw json string with the most popular movies
     */

    public static String getMostPopularRawJson() {
            return getResponseFromHttpUrl(buildUrlFromString(MOST_POPULAR_URL));
    }

    /**
     * function that makes an api call to retrieve the top rated movies Json
     * @return raw json string with the top rated movies
     */

    public static String getTopRatedRawJson() {
        return getResponseFromHttpUrl(buildUrlFromString(TOP_RATED_URL));
    }

    /**
     * function that turns a string in a URL object
     * @param s - URL string
     * @return a new URL object or throws a MalformedURLException and returns null
     */

    private static URL buildUrlFromString (String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
