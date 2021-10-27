package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import android.support.annotation.NonNull;
import android.util.Log;

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
 * Created by Madhusudan Sapkota on 2/27/2018.
 */


@Entity(tableName = "events")
public class Event extends HomeModel {

    @SerializedName("event_id")
    @Expose
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("event_title")
    @Expose
    @ColumnInfo(name = "event_title")
    private String title;

    @SerializedName("event_description")
    @Expose
    @ColumnInfo(name = "event_desc")
    private String description;

    @SerializedName("event_start_date")
    @Expose
    @ColumnInfo(name = "event_start_date")
    @TypeConverters({DateConverter.class})
    private Date eventStartDate;

    @SerializedName("event_end_date")
    @Expose
    @ColumnInfo(name = "event_end_date")
    @TypeConverters({DateConverter.class})
    private Date eventEndDate;

    @SerializedName("is_holiday")
    @Expose
    @ColumnInfo(name = "is_holiday")
    private boolean isHoliday;

    @SerializedName("event_category")
    @Expose
    @ColumnInfo(name = "event_category")
    private String eventCategory;

    @ColumnInfo(name = "inserted_at")
    @TypeConverters({DateConverter.class})
    private Date insertedAt = Calendar.getInstance(TimeZone.getTimeZone("Asia/Katmandu")).getTime();

    @Ignore
    private HomeType HOME_TYPE = null;


    { TYPE = HomeModel.EVENT_TYPE; }

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String eventTitle) {
        this.title = eventTitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String eventDesc) {
        this.description = eventDesc;
    }

    public Date getEventStartDate() {
        return eventStartDate;
    }

    public void setEventStartDate(Date eventStartDate) {
        this.eventStartDate = eventStartDate;
    }

    public Date getEventEndDate() {
        return eventEndDate;
    }

    public void setEventEndDate(Date eventEndDate) {
        this.eventEndDate = eventEndDate;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean isHoliday) {
        isHoliday = isHoliday;
    }

    public String getEventCategory() {
        return eventCategory;
    }

    public void setEventCategory(String eventCategory) {
        this.eventCategory = eventCategory;
    }

    public Date getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(Date insertedAt) {
        this.insertedAt = insertedAt;
    }

    @Override
    public Date getTime() {
        return eventStartDate;
    }

    @Override
    public HomeType getHomeType() {
//        return HOME_TYPE.value();
        if (HOME_TYPE != null) return HOME_TYPE;

        Date today = java.util.Calendar.getInstance().getTime();
        int dayAgo = Utils.getDaysDifference(today, getTime());

        if(dayAgo < 0){
            HOME_TYPE = HomeType.UPCOMING;
        }else if (dayAgo == 0){
            HOME_TYPE = HomeType.TODAY;
        }else {
            // check other conditions
            HOME_TYPE = HomeType.CALENDAR;
        }
        Log.e("HomeTypeEvent", HOME_TYPE.value()+" Name "+ HOME_TYPE.displayName());
        return HOME_TYPE;
    }
}