package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.data.local.db.converter.DateConverter;
import com.swipecrafts.school.ui.home.HomeType;
import com.swipecrafts.school.ui.home.HomeModel;
import com.swipecrafts.school.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Madhusudan Sapkota on 2/18/2018.
 */

@Entity(tableName = "notifications")
public class Notification extends HomeModel {

    @SerializedName("notice_id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("notice_title")
    @Expose
    @ColumnInfo(name = "title")
    private String title;

    @SerializedName("notice_description")
    @Expose
    @ColumnInfo(name = "description")
    private String description;

    @SerializedName("image_name")
    @Expose
    @ColumnInfo(name = "remote_image_url")
    private String remoteImgUrl;

    @ColumnInfo(name = "local_image_url")
    private String localImgUrl = "";

    @SerializedName("notice_date")
    @Expose
    @ColumnInfo(name = "notice_date")
    @TypeConverters({DateConverter.class})
    private Date time;

    @SerializedName("notice_link")
    @Expose
    @ColumnInfo(name = "notice_link")
    private String noticeLink = "";

    @SerializedName("is_important")
    @Expose
    @ColumnInfo(name = "importance") // only contains 0=GENERAL or 1=IMPORTANT
    private int importance;

    @SerializedName("parent_availability")
    @Expose
    @ColumnInfo(name = "parent_availability") // only contains 0 or 1
    private int parentAvailability;

    @Expose
    @ColumnInfo(name = "is_pinned") // only contains 0 or 1
    private int isPinned = 0;

    @SerializedName("category")
    @Expose
    @ColumnInfo(name = "category")
    private String category;

    @ColumnInfo(name = "inserted_at")
    @TypeConverters({DateConverter.class})
    private Date insertedAt = Calendar.getInstance(TimeZone.getTimeZone("Asia/Katmandu")).getTime();

    @Ignore
    private HomeType HOME_TYPE = null;

    {
        TYPE = HomeModel.NOTIFICATION_TYPE;
    }

    @Override
    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemoteImgUrl() {
        return remoteImgUrl;
    }

    public void setRemoteImgUrl(String remoteImgUrl) {
        this.remoteImgUrl = remoteImgUrl;
    }

    public String getLocalImgUrl() {
        return localImgUrl;
    }

    public void setLocalImgUrl(String localImgUrl) {
        this.localImgUrl = localImgUrl;
    }

    public String getNoticeLink() {
        return noticeLink;
    }

    public void setNoticeLink(String noticeLink) {
        this.noticeLink = noticeLink;
    }

    public int getImportance() {
        return importance;
    }

    public void setImportance(int importance) {
        this.importance = importance;
    }

    public int getParentAvailability() {
        return parentAvailability;
    }

    public void setParentAvailability(int parentAvailability) {
        this.parentAvailability = parentAvailability;
    }

    public int getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(int isPinned) {
        this.isPinned = isPinned;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Date getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Date insertedAt) {
        this.insertedAt = insertedAt;
    }

    @Override
    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    @Override
    public HomeType getHomeType() {
        if (HOME_TYPE != null) return HOME_TYPE;


        Calendar currentDate = java.util.Calendar.getInstance();
        currentDate.set(Calendar.HOUR_OF_DAY, 0);
        currentDate.set(Calendar.MINUTE, 0);
        currentDate.set(Calendar.SECOND, 0);
        currentDate.set(Calendar.MILLISECOND, 0);

        int dayAgo = Utils.getDaysDifference(currentDate.getTime(), getTime());

        if (dayAgo < 0) {
            HOME_TYPE = HomeType.UPCOMING;
        } else if (dayAgo == 0) {
            HOME_TYPE = HomeType.TODAY;
        } else {
            // check other conditions
            if (parentAvailability == 1) HOME_TYPE = HomeType.PARENT_AV;
            else HOME_TYPE = HomeType.OTHERS;
        }
        return HOME_TYPE;
    }
}
