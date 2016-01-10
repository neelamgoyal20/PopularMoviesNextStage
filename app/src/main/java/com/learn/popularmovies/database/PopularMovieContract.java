package com.learn.popularmovies.database;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by E01090 on 1/9/2016.
 */
public final class PopularMovieContract {

    public static final String AUTHORITY = "com.learn.popularmovies";

    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_MOVIE = "movies";

    public static final class Movies implements BaseColumns {

        public static final Uri CONTENT_URI =
                Uri.withAppendedPath(
                        PopularMovieContract.BASE_URI,
                        PATH_MOVIE);

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + PATH_MOVIE;

        public static final String TABLE_MOVIES = "movies";

        public static final String MOVIE_ID = "movie_id"; // the movie id from the backend
        public static final String TITLE = "title";
        public static final String RELEASE_YEAR = "releaseyear";
        public static final String DESCRIPTION = "desc";
        public static final String POSTER_URL = "posterurl";
        public static final String RATING = "rating";

        public static final String[] PROJECTION_ALL = {_ID, MOVIE_ID, TITLE, RELEASE_YEAR, DESCRIPTION, POSTER_URL, RATING};

        public static final String SELECTION_ID_BASED = MOVIE_ID + " = ? ";

        public static final String SORT_ORDER_DEFAULT = TITLE + " ASC";

    }
}