package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 11/15/2018.
 */

@Entity(tableName = "bus_drivers")
public class BusDriver {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("bidi")
    @Expose
    private long mBusId;

    @ColumnInfo(name = "bus_name")
    @SerializedName("busname")
    @Expose
    private String mBusName;

    @ColumnInfo(name = "bus_number")
    @SerializedName("busnumber")
    @Expose
    private String mBusNo;

    @ColumnInfo(name = "driver_id")
    @SerializedName("drid")
    @Expose
    private String driverId;

    @ColumnInfo(name = "driver_first_name")
    @SerializedName("dfname")
    @Expose
    private String driverFName;

    @ColumnInfo(name = "driver_middle_name")
    @SerializedName("dmname")
    @Expose
    private String driverMName;

    @ColumnInfo(name = "driver_last_name")
    @SerializedName("dlname")
    @Expose
    private String driverLName;

    @ColumnInfo(name = "driver_address")
    @SerializedName("daddr")
    @Expose
    private String driverAddress;

    @ColumnInfo(name = "driver_contact")
    @SerializedName("dcont")
    @Expose
    private String driverContact;

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

    public void setBusNo(String mBusNo) {
        this.mBusNo = mBusNo;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getDriverFName() {
        return driverFName;
    }

    public void setDriverFName(String driverFName) {
        this.driverFName = driverFName;
    }

    public String getDriverMName() {
        return driverMName;
    }

    public void setDriverMName(String driverMName) {
        this.driverMName = driverMName;
    }

    public String getDriverLName() {
        return driverLName;
    }

    public void setDriverLName(String driverLName) {
        this.driverLName = driverLName;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverContact() {
        return driverContact;
    }

    public void setDriverContact(String driverContact) {
        this.driverContact = driverContact;
    }
}
