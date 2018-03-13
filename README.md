# PopularMoviesStage1
# Popular Movies Stage 1 project for the Udacity Android Nanodegree Program

## Project Overview
The app fetches data from http://api.themoviedb.org and uses it to show the top rated or most popular movies in a grid layout displayed in a recyclerview. Each item can be clicked to show more details about the specific movie

## Api key
In order for the project to work you need a themoviedb api key. The key needs to be replaced in the:
/app/src/main/java/com/example/tudor/popularmovies/utils/NetworkUtils.java
private static String API_KEY = ApiUtils.getApiKey(); // Replace with your api key

## Screenshots
<img src="https://user-images.githubusercontent.com/17934944/37372160-801ee460-271a-11e8-80da-562a4e6fc801.png" width="300">
<img src="https://user-images.githubusercontent.com/17934944/37372191-99e5f50a-271a-11e8-9281-21f94b8d8da9.png" width="300">

## Common Project Requirements
- [x] App is written solely in the Java Programming Language.
- [x] Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
- [x] UI contains an element (i.e a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
- [x] UI contains a screen for displaying the details for a selected movie.
- [x] Movie details layout contains title, release date, movie poster, vote average, and plot synopsis.

## User Interface - Function
- [x] When a user changes the sort criteria (“most popular and highest rated”) the main view gets updated correctly.
- [x] When a movie poster thumbnail is selected, the movie details screen is launched.

## Network API Implementation 
- [x]In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.

## General Project Guidelines
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines (NOTE: For Stage 1 of the Popular Movies App, it is okay if the app does not restore the data using onSaveInstanceState/onRestoreInstanceState)
