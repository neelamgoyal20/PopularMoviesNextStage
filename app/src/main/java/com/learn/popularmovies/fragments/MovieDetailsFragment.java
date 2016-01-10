package com.learn.popularmovies.fragments;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.learn.popularmovies.CustomLinearLayoutManager;
import com.learn.popularmovies.INetworkReviewCallback;
import com.learn.popularmovies.INetworkTrailerCallback;
import com.learn.popularmovies.R;
import com.learn.popularmovies.Utils.IConstants;
import com.learn.popularmovies.database.PopularMovieContract;
import com.learn.popularmovies.dto.Movie;
import com.learn.popularmovies.dto.MovieReview;
import com.learn.popularmovies.dto.MovieTrailer;
import com.learn.popularmovies.fragments.adapters.TrailerAdapter;
import com.learn.popularmovies.network.NetworkCallGetReviewAsync;
import com.learn.popularmovies.network.NetworkCallGetTrailerAsync;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Neelam on 11/4/2015.
 */
public class MovieDetailsFragment extends Fragment{
    TextView mMovieReview;
    RecyclerView trailerList;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_detail, null);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        TextView movieTitle = (TextView)getView().findViewById(R.id.movie_title);
        TextView movieReleaseYear = (TextView)getView().findViewById(R.id.movie_release_year);
        TextView movieDescription = (TextView)getView().findViewById(R.id.movie_description);
        ImageView moviePoster = (ImageView)getView().findViewById(R.id.movie_poster);
        RatingBar movieRating = (RatingBar)getView().findViewById(R.id.movie_rating);
        mMovieReview = (TextView)getView().findViewById(R.id.movie_review);

        trailerList = (RecyclerView)getView().findViewById(R.id.trailer_list);
        trailerList.setHasFixedSize(true);
        LinearLayoutManager llm = new CustomLinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        trailerList.setLayoutManager(llm);

        FloatingActionButton fab = (FloatingActionButton) getView().findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        final Movie bundle = (Movie)getArguments().getParcelable("DetailObject");
        if (bundle!=null){
            movieTitle.setText(bundle.getmMovieTitle());
            movieReleaseYear.setText(bundle.getmMovieReleaseYear());
            movieRating.setRating(bundle.getmMovieRating());
            movieDescription.setText(bundle.getmMovieDescription());
            makeNetworkHit(bundle.getmID());
            String posterUrl = IConstants.IMAGE_THUMB_BASE_URL+ IConstants.IMAGE_SIZE_W342 + bundle.getmMoviePosterURL();

            if(bundle.isFavorite()) {
                fab.setVisibility(View.GONE);
            }else{
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // insert in DB
                        ContentValues values = new ContentValues();
                        values.clear();
                        values.put(PopularMovieContract.Movies.MOVIE_ID, bundle.getmID());
                        values.put(PopularMovieContract.Movies.TITLE, bundle.getmMovieTitle());
                        values.put(PopularMovieContract.Movies.DESCRIPTION, bundle.getmMovieDescription());
                        values.put(PopularMovieContract.Movies.POSTER_URL, bundle.getmMoviePosterURL());
                        values.put(PopularMovieContract.Movies.RATING, bundle.getmMovieRating());
                        values.put(PopularMovieContract.Movies.RELEASE_YEAR, bundle.getmMovieReleaseYear());
                        Uri movieResult = getActivity().getContentResolver().insert(PopularMovieContract.Movies.CONTENT_URI, values);
                    }
                });
            }
            if (posterUrl!=null) {
                Picasso.with(getActivity()).load(posterUrl).placeholder(android.R.drawable.ic_menu_gallery).into(moviePoster);
            }
        }

    }

    private void makeNetworkHit(String id){
        NetworkCallGetReviewAsync async = new NetworkCallGetReviewAsync(mCallback, getActivity(), id);
        async.execute();

        NetworkCallGetTrailerAsync trailerAsync = new NetworkCallGetTrailerAsync(mTrailerCallback, getActivity(), id);
        trailerAsync.execute();
    }

    private INetworkTrailerCallback mTrailerCallback = new INetworkTrailerCallback() {
        @Override
        public void getDataFromNetwork(List<MovieTrailer> movieTrailer) {
            if (isAdded()){
                if(movieTrailer!=null && movieTrailer.size()>0){
                    Log.d("MovieDetails", "Trailer " + movieTrailer.toString());
                    TrailerAdapter customAdapter = new TrailerAdapter(getActivity(), movieTrailer);
                    trailerList.setAdapter(customAdapter);

                } else {
                    Log.e("MovieDetails", "No Trailer");
                }
            }

        }
    };

    private INetworkReviewCallback mCallback = new INetworkReviewCallback() {
        @Override
        public void getDataFromNetwork(List<MovieReview> movieReview) {
            if(isAdded()) {
                if (movieReview != null && movieReview.size() > 0) {
                    mMovieReview.setText(movieReview.get(0).getmAuthor() + ":- " + movieReview.get(0).getmContent());
                } else {
                    mMovieReview.setText(getString(R.string.msg_no_item));
                }
            }
        }
    };

}

