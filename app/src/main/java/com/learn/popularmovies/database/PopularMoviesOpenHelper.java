package com.learn.popularmovies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by E01090 on 1/9/2016.
 */
public class PopularMoviesOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "popularmoviesdb";
    private static final int VERSION = 1;

    public PopularMoviesOpenHelper(Context context){
        super(context, DB_NAME, null, VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_MOVIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE_MOVIES);
        onCreate(db);
    }

    String CREATE_TABLE_MOVIES =
            "CREATE TABLE " + PopularMovieContract.Movies.TABLE_MOVIES  + "("
                    + "_id INTEGER  PRIMARY KEY AUTOINCREMENT, \n"
                    + PopularMovieContract.Movies.MOVIE_ID + " TEXT UNIQUE NOT NULL,"
                    + PopularMovieContract.Movies.TITLE + " TEXT,"
                    + PopularMovieContract.Movies.RELEASE_YEAR + " TEXT,"
                    + PopularMovieContract.Movies.DESCRIPTION + " TEXT,"
                    + PopularMovieContract.Movies.RATING + " FLOAT,"
                    + PopularMovieContract.Movies.POSTER_URL + " TEXT"
                    + ")";

    String DROP_TABLE_MOVIES =
            "DROP TABLE IF EXISTS " + PopularMovieContract.Movies.TABLE_MOVIES;
}
