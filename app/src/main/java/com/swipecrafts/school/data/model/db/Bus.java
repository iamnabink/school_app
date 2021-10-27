package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 3/14/2018.
 */

@Entity(tableName = "bus")
public class Bus {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("app_bus_id")
    @Expose
    private long mBusId;

    @ColumnInfo(name = "bus_name")
    @SerializedName("bus_name")
    @Expose
    private String mBusName;

    @ColumnInfo(name = "bus_number")
    @SerializedName("bus_no")
    @Expose
    private String mBusNo;

    public Bus(@NonNull long mBusId, String mBusName, String mBusNo) {
        this.mBusId = mBusId;
        this.mBusName = mBusName;
        this.mBusNo = mBusNo;
    }

    @NonNull
    public long getBusId() {
        return mBusId;
    }

    public void setBusId(@NonNull long mBusId) {
        this.mBusId = mBusId;
    }

    public String getBusName() {
        return mBusName;
    }

    public void setBusName(String mBusName) {
        this.mBusName = mBusName;
    }

    public String getBusNo() {
        return mBusNo;
    }

    public void semBusNo(String mBusNo) {
        this.mBusNo = mBusNo;
    }
}
