package com.learn.popularmovies.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn.popularmovies.R;
import com.learn.popularmovies.Utils.IConstants;
import com.learn.popularmovies.dto.MovieTrailer;

import java.util.List;

/**
 * Created by Neelam on 11/24/2015.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.CustomViewHolder>  {

    private Context mContext;
    private List<MovieTrailer> mTrailers;

    public TrailerAdapter(Context ctx, List<MovieTrailer> movieData){
        mContext = ctx;
        mTrailers = movieData;
    }

    @Override
    public int getItemCount() {
        if (mTrailers != null) {
            return mTrailers.size();
        }
        return 0;
    }


    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public void onBindViewHolder(CustomViewHolder holder, final int position) {
        String title = mTrailers.get(position).getmName();
        holder.tvTitle.setText(title);
        holder.rowItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent localIntent = new Intent(
                        "android.intent.action.VIEW", Uri
                        .parse(IConstants.YOUTUBE_BASE_URL + mTrailers.get(position).getmKey()));
                mContext.startActivity(localIntent);
            }
        });
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_movie_trailer, parent, false);
        return new CustomViewHolder(v);
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        LinearLayout rowItem;
        CustomViewHolder(View view) {
            super(view);
            tvTitle = (TextView) view.findViewById(R.id.trailer_title);
            rowItem = (LinearLayout)view.findViewById(R.id.rowItem);
        }
    }

}
