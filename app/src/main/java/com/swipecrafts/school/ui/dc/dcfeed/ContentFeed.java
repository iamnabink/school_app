package com.swipecrafts.school.ui.dc.dcfeed;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.utils.renderer.BaseModel;

import java.util.Date;

import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.AUDIO;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.ContentFeedType;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.IMAGE;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.PDF;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.TEXT;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.VIDEO;
import static com.swipecrafts.school.ui.dc.dcfeed.ContentFeedListAdapter.YTD_VIDEO;

public class ContentFeed implements BaseModel {
    @SerializedName("dc_content_id")
    @Expose
    private long mDcContentId;

    @SerializedName("dc_content_title")
    @Expose
    private String mDcContentTitle = "";

    @SerializedName("dc_content_desc")
    @Expose
    private String mDcContentDesc;

    @SerializedName("dc_content_category")
    @Expose
    private String mDcContentCategory = "";

    @SerializedName("dc_content_name")
    @Expose
    private String mDcContentName = "";

    @SerializedName("dc_content_link_key")
    @Expose
    private String mDcContentLinkKey = "";

    @SerializedName("dc_content_posted_on")
    @Expose
    private Date mDcContentPostedOn;

    @ContentFeedType
    private int contentType = -1;

    public String getDcContentCategory() {
        return mDcContentCategory;
    }

    public void setDcContentCategory(String mDcContentCategory) {
        this.mDcContentCategory = mDcContentCategory;
    }

    public String getDcContentDesc() {
        return mDcContentDesc;
    }

    public void setDcContentDesc(String mDcContentDesc) {
        this.mDcContentDesc = mDcContentDesc;
    }

    public long getDcContentId() {
        return mDcContentId;
    }

    public void setDcContentId(long mDcContentId) {
        this.mDcContentId = mDcContentId;
    }

    public String getDcContentLinkKey() {
        return mDcContentLinkKey;
    }

    public void setDcContentLinkKey(String mDcContentLinkKey) {
        this.mDcContentLinkKey = mDcContentLinkKey;
    }

    public String getDcContentName() {
        return mDcContentName;
    }

    public void setDcContentName(String mDcContentName) {
        this.mDcContentName = mDcContentName;
    }

    public Date getDcContentPostedOn() {
        return mDcContentPostedOn;
    }

    public void setDcContentPostedOn(Date mDcContentPostedOn) {
        this.mDcContentPostedOn = mDcContentPostedOn;
    }

    public String getDcContentTitle() {
        return mDcContentTitle;
    }

    public void setDcContentTitle(String mDcContentTitle) {
        this.mDcContentTitle = mDcContentTitle;
    }

    public void setContentType(@ContentFeedType int contentType) {
        this.contentType = contentType;
    }

    @Override
    @ContentFeedType
    public int getType() {
        this.contentType = this.contentType != -1 ? this.contentType : getContentType(mDcContentCategory);
        return contentType;
    }

    @ContentFeedType
    private int getContentType(String category) {
        if (category.equalsIgnoreCase("text")) return TEXT;
        else if (category.equalsIgnoreCase("pdf")) return PDF;
        else if (category.equalsIgnoreCase("image")) return IMAGE;
        else if (category.equalsIgnoreCase("video")) return VIDEO;
        else if (category.equalsIgnoreCase("ytvideo")) return YTD_VIDEO;
        else if (category.equalsIgnoreCase("audio")) return AUDIO;
        else return TEXT;
    }

    @Override
    public int compareTo(@NonNull BaseModel o) {
        return 0;
    }

    @Override
    public String toString() {
        return "{\n" +
                "        \"dc_content_id\": " + mDcContentId + ",\n" +
                "        \"dc_content_title\": " + mDcContentTitle + ",\n" +
                "        \"dc_content_desc\": " + mDcContentDesc + ",\n" +
                "        \"dc_content_category\": " + mDcContentCategory + ",\n" +
                "        \"dc_content_name\": " + mDcContentName + ",\n" +
                "        \"dc_content_link_key\": " + mDcContentLinkKey + ",\n" +
                "        \"dc_content_posted_on\": " + mDcContentPostedOn + "\n" +
                "    }";
    }
}
