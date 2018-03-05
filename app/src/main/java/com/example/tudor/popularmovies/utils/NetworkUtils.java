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

    public static String getTestUrl() {
        URL testUrl;
        try {
            testUrl = new URL("http://api.themoviedb.org/3/movie/popular?api_key=" + ApiUtils.getApiKey());
            return getResponseFromHttpUrl(testUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }


    }
}
