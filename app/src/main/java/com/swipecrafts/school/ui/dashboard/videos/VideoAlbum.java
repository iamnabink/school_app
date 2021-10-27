package com.swipecrafts.school.ui.dashboard.videos;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.data.local.db.converter.DateConverter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by Madhusudan Sapkota on 3/27/2018.
 */

public class VideoAlbum implements Serializable{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("album_id")
    @Expose
    private long mAlbumId;

    @ColumnInfo(name = "name")
    @SerializedName("album_name")
    @Expose
    private String mAlbumName;

    @ColumnInfo(name = "description")
    @SerializedName("album_desc")
    @Expose
    private String mAlbumDesc;

    @ColumnInfo(name = "time")
    @TypeConverters({DateConverter.class})
    @SerializedName("album_created_at")
    @Expose
    private Date mAlbumCreatedAt;

    @ColumnInfo(name = "event_name")
    @SerializedName("school_event")
    @Expose
    private String mSchoolEvent;

    @ColumnInfo(name = "album_img_url")
    @Nullable
    private String albumImgUrl;

    @Ignore
    @SerializedName("album_content")
    @Expose
    private List<VideoAlbumContent> mImageAlbumContent;

    public List<VideoAlbumContent> getAlbumContent() {
        return mImageAlbumContent;
    }

    public void setAlbumContent(List<VideoAlbumContent> imageAlbumContent) {
        mImageAlbumContent = imageAlbumContent;
    }

    public Date getAlbumCreatedAt() {
        return mAlbumCreatedAt;
    }

    public void setAlbumCreatedAt(Date albumCreatedAt) {
        mAlbumCreatedAt = albumCreatedAt;
    }

    public String getAlbumDesc() {
        return mAlbumDesc;
    }

    public void setAlbumDesc(String albumDesc) {
        mAlbumDesc = albumDesc;
    }

    public long getAlbumId() {
        return mAlbumId;
    }

    public void setAlbumId(long albumId) {
        mAlbumId = albumId;
    }

    public String getAlbumName() {
        return mAlbumName;
    }

    public void setAlbumName(String albumName) {
        mAlbumName = albumName;
    }

    public String getSchoolEvent() {
        return mSchoolEvent;
    }

    public void setSchoolEvent(String schoolEvent) {
        mSchoolEvent = schoolEvent;
    }

    @Nullable
    public String getAlbumImgUrl() {
        return albumImgUrl;
    }

    public void setAlbumImgUrl(@Nullable String albumImgUrl) {
        this.albumImgUrl = albumImgUrl;
    }
}
