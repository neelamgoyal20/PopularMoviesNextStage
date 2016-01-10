package com.learn.popularmovies.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by E01090 on 1/5/2016.
 */
public class MovieTrailer {
    @SerializedName("name")
    private String mName;

    @SerializedName("key")
    private String mKey;

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmKey() {
        return mKey;
    }

    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}
