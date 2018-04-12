package com.example.tudor.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by tudor on 07.04.2018.
 */

public class Trailer implements Parcelable {

    private String mId;
    private String mKey;
    private String mName;
    private String mSite;

    private final String SITE_YOUTUBE = "YouTube";
    private final String SITE_YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";
    //https://img.youtube.com/vi/dxWvtMOGAhw/0.jpg
    private final String YOUTUBE_THUMBNAIL_BASE_URL = "https://img.youtube.com/vi/";
    private final String YOUTUBE_THUMBNAIL_IMAGE = "/0.jpg";


    public Trailer(String id, String key, String name, String site) {
        this.mId = id;
        this.mKey = key;
        this.mName = name;
        this.mSite = site;
    }

    public String getId() {
        return this.mId;
    }

    public  String getKey() {
        return this.mKey;
    }

    public String getName() {
        return this.mName;
    }

    public String getSite() {
        return this.mSite;
    }

    public String getVideoUrl() {

        switch (getSite()) {
            case SITE_YOUTUBE:
                return SITE_YOUTUBE_BASE_URL + getKey();
            default:
                try {
                    throw new Exception("Unknown site: " + getSite());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
        }
    }

    public String getImageUrl() {
        switch (getSite()) {
            case SITE_YOUTUBE:
                return YOUTUBE_THUMBNAIL_BASE_URL + getKey() + YOUTUBE_THUMBNAIL_IMAGE;
            default:
                try {
                    throw new Exception("Unknown site: " + getSite());
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
        }
    }



    protected Trailer(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Trailer> CREATOR = new Parcelable.Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
