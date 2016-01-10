package com.learn.popularmovies.Utils;

public interface IConstants  {
    String KEY_MOVIE_TITLE = "movieTitle";
    String KEY_MOVIE_RELEASE_DATE = "movieReleaseDate";
    String KEY_MOVIE_RATING = "movieRating";
    String KEY_MOVIE_DESCRIPTION = "movieDescription";
    String KEY_MOVIE_POSTER = "moviePosterUrl";

    String IMAGE_THUMB_BASE_URL = "http://image.tmdb.org/t/p/";
    String MOVIE_BASE_URL = "http://api.themoviedb.org/3/discover/movie?sort_by=";
    String REVIEW_BASE_URL = "http://api.themoviedb.org/3/movie/";
    String REVIEW_PARAM="/reviews";
    String TRAILER_PARAM = "/videos";
    String MOVIE_SORT_PARAM_POPULARITY = "popularity.desc";
    String MOVIE_SORT_PARAM_RATING = "vote_average.desc";
    String MOVIE_SORT_PARAM_FAVORITE = "favorite";
    String MOVIE_KEY_PARAM = "&api_key=";
    String REVIEW_KEY_PARAM = "?api_key=";
    String IMAGE_SIZE_W185 = "w185/";
    String IMAGE_SIZE_W342 = "w342/";
    String API_KEY = "";
    String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
}

