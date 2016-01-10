package com.learn.popularmovies.database;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.learn.popularmovies.INetworkCallback;
import com.learn.popularmovies.R;
import com.learn.popularmovies.dto.Movie;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neelam  on 11/22/2015.
 */
public class ReadFromDBAsync extends AsyncTask<Void, Void, List<Movie>> {
    private INetworkCallback mCallback;
    private ProgressDialog mProgressDialog;
    private Context mContext;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgressDialog = ProgressDialog.show(mContext, null,
                mContext.getString(R.string.msg_fetching_data), true);
    }

    public ReadFromDBAsync(INetworkCallback callback, Context ctx) {
        mCallback = callback;
        mContext = ctx;
    }

    @Override
    protected void onPostExecute(List<Movie> result) {
        super.onPostExecute(result);
        if(mProgressDialog!=null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mCallback.getDataFromNetwork(result);
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {
        List<Movie> result = discoverMovies();
        return result;
    }

    private List<Movie> discoverMovies() {
        Cursor cursor = mContext.getContentResolver().query(PopularMovieContract.Movies.CONTENT_URI, PopularMovieContract.Movies.PROJECTION_ALL, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            List<Movie> movieList = new ArrayList<Movie>();
            while (!cursor.isAfterLast()) {
                Movie movieObj = new Movie();
                movieObj.setmID(cursor.getString(cursor.getColumnIndex(PopularMovieContract.Movies.MOVIE_ID)));
                movieObj.setmMovieDescription(cursor.getString(cursor.getColumnIndex(PopularMovieContract.Movies.DESCRIPTION)));
                movieObj.setmMoviePosterURL(cursor.getString(cursor.getColumnIndex(PopularMovieContract.Movies.POSTER_URL)));
                movieObj.setmMovieReleaseYear(cursor.getString(cursor.getColumnIndex(PopularMovieContract.Movies.RELEASE_YEAR)));
                movieObj.setmMovieTitle(cursor.getString(cursor.getColumnIndex(PopularMovieContract.Movies.TITLE)));
                movieObj.setmMovieRating(cursor.getFloat(cursor.getColumnIndex(PopularMovieContract.Movies.RATING)));
                movieObj.setIsFavorite(true);
                movieList.add(movieObj);
                cursor.moveToNext();
            }
            cursor.close();
            return movieList;
        } else {
            return null;
        }
    }
}
