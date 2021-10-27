package com.swipecrafts.school.ui.dashboard.videos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Madhusudan Sapkota on 3/27/2018.
 */

public class VideoAlbumContent implements Serializable{
    @SerializedName("album_contentid")
    @Expose
    private String albumContentid;
    @SerializedName("ctitle")
    @Expose
    private String ctitle;
    @SerializedName("cthumbnail")
    @Expose
    private String cthumbnail;
    @SerializedName("clink_key")
    @Expose
    private String clinkKey;
    @SerializedName("uploaded_at")
    @Expose
    private String uploadedAt;

    public String getAlbumContentid() {
        return albumContentid;
    }

    public void setAlbumContentid(String albumContentid) {
        this.albumContentid = albumContentid;
    }

    public String getCtitle() {
        return ctitle;
    }

    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    public String getCthumbnail() {
        return cthumbnail;
    }

    public void setCthumbnail(String cthumbnail) {
        this.cthumbnail = cthumbnail;
    }

    public String getClinkKey() {
        return clinkKey;
    }

    public void setClinkKey(String clinkKey) {
        this.clinkKey = clinkKey;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

}
