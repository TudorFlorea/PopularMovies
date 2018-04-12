package com.example.tudor.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tudor on 08.04.2018.
 */

public class Review implements Parcelable {

    private String mId;
    private String mAuthor;
    private String mContent;
    private String mUrl;
    private int mMaxContent = 200;


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

    public String getByAuthor() {
        return "By " + this.mAuthor;
    }

    public String getContent() {
        return this.mContent;
    }

    public String getShortContent() {
        if (mContent.length() > mMaxContent) {
            return this.mContent.substring(0, mMaxContent) + "...";
        } else {
            return this.mContent;
        }

    }

    public String getUrl() {
        return this.mUrl;
    }


    protected Review(Parcel in) {
        mId = in.readString();
        mAuthor = in.readString();
        mContent = in.readString();
        mUrl = in.readString();
        mMaxContent = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mAuthor);
        dest.writeString(mContent);
        dest.writeString(mUrl);
        dest.writeInt(mMaxContent);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}