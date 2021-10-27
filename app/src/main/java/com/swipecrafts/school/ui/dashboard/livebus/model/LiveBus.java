package com.swipecrafts.school.ui.dashboard.livebus.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LiveBus {

    @SerializedName("driver_id")
    @Expose
    private String driverId;

    @SerializedName("app_bus_id")
    @Expose
    private String busId;

    @SerializedName("bus_no")
    @Expose
    private String busNumber;

    @SerializedName("bus_name")
    @Expose
    private String busName;


    private boolean status;

    public LiveBus() {
    }

    public LiveBus(String driverId, String busId, String busNumber, String busName) {
        this.driverId = driverId;
        this.busId = busId;
        this.busNumber = busNumber;
        this.busName = busName;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public String getBusId() {
        return busId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getStatus() {
        return this.status;
    }
}
