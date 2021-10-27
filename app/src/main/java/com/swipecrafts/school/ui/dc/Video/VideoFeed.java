package com.swipecrafts.school.ui.dc.Video;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter;
import com.swipecrafts.school.utils.renderer.BaseModel;

public class VideoFeed {

    @SerializedName("dc_video_id")
    @Expose
    private long Id;

    @SerializedName("content")
    @Expose
    private String dc_video_content;

    @SerializedName("page_number")
    @Expose
    private String dc_video_page;

    @SerializedName("title")
    @Expose
    private String is_video_title;

    public VideoFeed() {
        this.dc_video_content = dc_video_content;
        this.dc_video_page = dc_video_page;
        this.is_video_title = is_video_title;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getDc_video_content() {
        return dc_video_content;
    }

    public void setDc_video_content(String dc_video_content) {
        this.dc_video_content = dc_video_content;
    }

    public String getDc_video_page() {
        return dc_video_page;
    }

    public void setDc_video_page(String dc_video_page) {
        this.dc_video_page = dc_video_page;
    }

    public String getIs_video_title() {
        return is_video_title;
    }

    public void setIs_video_title(String is_video_title) {
        this.is_video_title = is_video_title;
    }

}
