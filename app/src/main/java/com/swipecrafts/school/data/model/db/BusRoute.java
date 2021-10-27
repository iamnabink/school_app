package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Madhusudan Sapkota on 3/14/2018.
 */


@Entity( tableName = "bus_route",
        foreignKeys = @ForeignKey(entity = Bus.class,
        parentColumns = "id",
        childColumns = "bus_id",
        onDelete = CASCADE)
)
public class BusRoute {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("station_id")
    @Expose
    private long mStationId;

    @ColumnInfo(name = "station_name")
    @SerializedName("station_name")
    @Expose
    private String mStationName;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private double mLatitude;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private double mLongitude;

    @ColumnInfo(name = "bus_id")
    @Nullable
    private long busId;


    public BusRoute(@NonNull long mStationId, String mStationName, double mLatitude, double mLongitude, long busId) {
        this.mStationId = mStationId;
        this.mStationName = mStationName;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.busId = busId;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(long mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(long mLongitude) {
        this.mLongitude = mLongitude;
    }

    public long getStationId() {
        return mStationId;
    }

    public void setStationId(long mStationId) {
        this.mStationId = mStationId;
    }

    public String getStationName() {
        return mStationName;
    }

    public void setStationName(String mStationName) {
        this.mStationName = mStationName;
    }

    public long getBusId() {
        return busId;
    }

    public void setBusId(long busId) {
        this.busId = busId;
    }
}
