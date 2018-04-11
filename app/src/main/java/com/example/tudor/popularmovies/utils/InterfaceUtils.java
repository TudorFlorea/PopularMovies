package com.example.tudor.popularmovies.utils;

import com.example.tudor.popularmovies.data.Movie;

/**
 * Created by tudor on 09.04.2018.
 */

public class InterfaceUtils {
    public interface MovieItemListener {
        void onItemClick(Movie movie, Class targetActivity);
    }
}
