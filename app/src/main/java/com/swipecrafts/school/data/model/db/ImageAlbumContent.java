package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "image_album_contents", foreignKeys = @ForeignKey(entity = Album.class,
        parentColumns = "id",
        childColumns = "album_id",
        onDelete = CASCADE))
public class ImageAlbumContent implements Serializable{

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("album_contentid")
    @Expose
    private String mAlbumContentId;

    @ColumnInfo(name = "title")
    @SerializedName("ctitle")
    @Expose
    private String mContentTitle;

    @ColumnInfo(name = "content1_url")
    @SerializedName("cname")
    @Expose
    private String mC1Url;

    @ColumnInfo(name = "content2_url")
    @SerializedName("curl")
    @Expose
    private String mC2Url;

    @ColumnInfo(name = "time")
    @SerializedName("uploaded_at")
    @Expose
    private Date mUploadedAt;

    @ColumnInfo(name = "album_id")
    @Nullable
    private long albumId;

    @NonNull
    public String getAlbumContentId() {
        return mAlbumContentId;
    }

    public void setAlbumContentId(@NonNull String mAlbumContentId) {
        this.mAlbumContentId = mAlbumContentId;
    }

    public String getContentTitle() {
        return mContentTitle;
    }

    public void setContentTitle(String mContentTitle) {
        this.mContentTitle = mContentTitle;
    }

    public String getC1Url() {
        return mC1Url;
    }

    public void setC1Url(String mC1Url) {
        this.mC1Url = mC1Url;
    }

    public String getC2Url() {
        return mC2Url;
    }

    public void setC2Url(String mC2Url) {
        this.mC2Url = mC2Url;
    }

    public Date getUploadedAt() {
        return mUploadedAt;
    }

    public void setUploadedAt(Date mUploadedAt) {
        this.mUploadedAt = mUploadedAt;
    }

    public long getAlbumId() {
        return albumId;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }
}
