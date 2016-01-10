package com.learn.popularmovies.network;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.learn.popularmovies.INetworkTrailerCallback;
import com.learn.popularmovies.Utils.IConstants;
import com.learn.popularmovies.Utils.Utility;
import com.learn.popularmovies.dto.MovieTrailer;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Neelam  on 11/22/2015.
 */
public class NetworkCallGetTrailerAsync extends AsyncTask<Void, Void, String> {
    private INetworkTrailerCallback mCallback;
    private Context mContext;
    private String mMovieID;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    public NetworkCallGetTrailerAsync(INetworkTrailerCallback callback, Context ctx, String movieId) {
        mCallback = callback;
        mContext = ctx;
        mMovieID = movieId;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if(result != null) {
            Gson gson = new Gson();
            JsonParser parser = new JsonParser();
            JsonObject obj = parser.parse(result).getAsJsonObject();
            JsonElement arrayStr = obj.get("results");
            Type listType = new TypeToken<List<MovieTrailer>>() {
            }.getType();
            List<MovieTrailer> posts = (List<MovieTrailer>) gson.fromJson(arrayStr, listType);
            Log.d("Network", "PostEx");
            mCallback.getDataFromNetwork(posts);
        } else {
            mCallback.getDataFromNetwork(null);
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        String result = getMovieReviews();
        return result;
    }

    private String getMovieReviews() {
        String line = null;
        try {
            URL url = new URL(IConstants.REVIEW_BASE_URL+mMovieID+IConstants.TRAILER_PARAM+ IConstants.REVIEW_KEY_PARAM+IConstants.API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

// read the response
            System.out.println("Response Code: " + conn.getResponseCode());
            if(conn.getResponseCode() == 200) {
                InputStream in = new BufferedInputStream(conn.getInputStream());

                if (in != null) {
                    line = Utility.convertStreamToString(in);
                }
            }

            Log.d("Network", " Result ::: " + line);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return line;
    }


}
