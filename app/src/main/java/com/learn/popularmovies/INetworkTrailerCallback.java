package com.learn.popularmovies;

import com.learn.popularmovies.dto.MovieReview;
import com.learn.popularmovies.dto.MovieTrailer;

import java.util.List;

/**
 * Created by Neelam on 11/25/2015.
 */
public interface INetworkTrailerCallback {
    void getDataFromNetwork(List<MovieTrailer> movieTrailer);
}
