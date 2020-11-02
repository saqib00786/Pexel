package com.example.pexel.Model;

public class WallpaperModel {
    private int mId;
    private String mLargeImageUrl,mMediumImageUrl;

    public WallpaperModel() {
    }

    public WallpaperModel(int mId, String mLargeImageUrl, String mMediumImageUrl) {
        this.mId = mId;
        this.mLargeImageUrl = mLargeImageUrl;
        this.mMediumImageUrl = mMediumImageUrl;
    }

    public int getmId() {
        return mId;
    }

    public void setmId(int mId) {
        this.mId = mId;
    }

    public String getmLargeImageUrl() {
        return mLargeImageUrl;
    }

    public void setmLargeImageUrl(String mLargeImageUrl) {
        this.mLargeImageUrl = mLargeImageUrl;
    }

    public String getmMediumImageUrl() {
        return mMediumImageUrl;
    }

    public void setmMediumImageUrl(String mMediumImageUrl) {
        this.mMediumImageUrl = mMediumImageUrl;
    }
}
