package com.example.tudor.popularmovies.data;

/**
 * Created by tudor on 08.04.2018.
 */

public class Review {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;

    public Review(String id, String author, String content, String url) {
        this.mId = id;
        this.mAuthor = author;
        this.mContent = content;
        this.mUrl = url;
    }

    public String getId() {
        return this.mId;
    }

    public String getAuthor() {
        return this.mAuthor;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getUrl() {
        return this.mUrl;
    }

}