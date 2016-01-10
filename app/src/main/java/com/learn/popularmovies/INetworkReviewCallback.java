package com.learn.popularmovies;

import com.learn.popularmovies.dto.Movie;
import com.learn.popularmovies.dto.MovieReview;

import java.util.List;

/**
 * Created by Neelam on 11/25/2015.
 */
public interface INetworkReviewCallback {
    void getDataFromNetwork(List<MovieReview> movieReview);
}
