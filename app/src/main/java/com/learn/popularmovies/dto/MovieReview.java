package com.learn.popularmovies.dto;

import com.google.gson.annotations.SerializedName;

/**
 * Created by E01090 on 1/5/2016.
 */
public class MovieReview {

    @SerializedName("author")
    private String mAuthor;

    @SerializedName("content")
    private String mContent;

    public String getmAuthor() {
        return mAuthor;
    }

    public void setmAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }
}
