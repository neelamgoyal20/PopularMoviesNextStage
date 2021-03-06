package com.learn.popularmovies.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.learn.popularmovies.INetworkCallback;
import com.learn.popularmovies.MainActivity;
import com.learn.popularmovies.MovieDetailActivity;
import com.learn.popularmovies.R;
import com.learn.popularmovies.Utils.IConstants;
import com.learn.popularmovies.Utils.Utility;
import com.learn.popularmovies.database.PopularMovieContract;
import com.learn.popularmovies.database.ReadFromDBAsync;
import com.learn.popularmovies.dto.Movie;
import com.learn.popularmovies.fragments.adapters.MovieGridAdapter;
import com.learn.popularmovies.network.NetworkCallAsync;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Neelam on 11/22/2015.
 */
public class MovieGridFragment extends Fragment {

    private GridView mMovieGrid;
    private boolean isListUpdated = false;
    private TextView mTxtForMsg;
    private ArrayList<Movie> mMovieList;
    private int selectedPosition;
    private String mSortStr = IConstants.MOVIE_SORT_PARAM_POPULARITY;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie, null);
        mMovieGrid = (GridView)view.findViewById(R.id.movie_grid);
        mTxtForMsg = (TextView)view.findViewById(R.id.txt_header);
        setHasOptionsMenu(true);
        if(savedInstanceState!=null){
            mMovieList = savedInstanceState.getParcelableArrayList("mMovieList");
            isListUpdated = savedInstanceState.getBoolean("isListUpdated");
            selectedPosition = savedInstanceState.getInt("selectedPosition");
            mSortStr = savedInstanceState.getString("mSortStr");
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(IConstants.MOVIE_SORT_PARAM_FAVORITE.equals(mSortStr)){
            getFromDB();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(Utility.isInternetAvailable(getActivity())) {
            if(!isListUpdated) {
                if(IConstants.MOVIE_SORT_PARAM_FAVORITE.equals(mSortStr)){
                    getFromDB();
                } else {
                    makeNetworkHit();
                }
            } else{
                setGridView();
            }
        } else {
            isListUpdated = false;
            mTxtForMsg.setText(R.string.msg_no_item);
            Toast.makeText(getActivity(), getResources().getString(R.string.msg_no_internet),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sort_popularity:
                mSortStr = IConstants.MOVIE_SORT_PARAM_POPULARITY;
                makeNetworkHit();
                return true;
            case R.id.action_sort_rating:
                mSortStr = IConstants.MOVIE_SORT_PARAM_RATING;
                makeNetworkHit();
                return true;
            case R.id.action_sort_favorite :
                mSortStr = IConstants.MOVIE_SORT_PARAM_FAVORITE;
                getFromDB();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    INetworkCallback mCallback = new INetworkCallback() {
        @Override
        public void getDataFromNetwork(List<Movie> movieList) {
            if(movieList!=null && movieList.size()>0) {
                isListUpdated = true;
                mMovieList = new ArrayList<>(movieList);
                setGridView();

            } else {
                isListUpdated = false;
                mTxtForMsg.setText(R.string.msg_no_item);
            }
        }
    };

    private void setGridView(){
        mTxtForMsg.setText(R.string.movie_data);
        MovieGridAdapter gridAdapter = new MovieGridAdapter(getActivity(), mMovieList);
        mMovieGrid.setAdapter(gridAdapter);
        mMovieGrid.setSelection(selectedPosition);
        mMovieGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                refreshDetailsFragment(position);
            }
        });
        if(MainActivity.mDualPane){
            android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
            MovieDetailsFragment detailFragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("DetailObject", mMovieList.get(selectedPosition));
            detailFragment.setArguments(bundle);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.detail_frame, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("mMovieList", mMovieList);
        outState.putBoolean("isListUpdated", isListUpdated);
//        selectedPosition = mMovieGrid.getFirstVisiblePosition();
        outState.putInt("selectedPosition", selectedPosition);
        outState.putString("mSortStr", mSortStr);
        super.onSaveInstanceState(outState);
    }

    private void makeNetworkHit(){
        NetworkCallAsync async = new NetworkCallAsync(mCallback, getActivity(), mSortStr);
        async.execute();
    }

    private void getFromDB(){
        ReadFromDBAsync async = new ReadFromDBAsync(mCallback, getActivity());
        async.execute();
    }

    private void refreshDetailsFragment(int position){
        selectedPosition = position;
        if (MainActivity.mDualPane){
            android.support.v4.app.FragmentManager manager = getActivity().getSupportFragmentManager();
            manager.popBackStack();
            MovieDetailsFragment detailFragment = new MovieDetailsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelable("DetailObject", mMovieList.get(position));
            detailFragment.setArguments(bundle);
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.detail_frame, detailFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        } else {
            Intent gridDetailIntent = new Intent(getActivity(), MovieDetailActivity.class);
            gridDetailIntent.putExtra("DetailObject", mMovieList.get(position));
            startActivity(gridDetailIntent);
        }
    }
}
