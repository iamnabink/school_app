package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "menuItems")
public class DashboardItem {

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    @SerializedName("app_menu_id")
    @Expose
    public long id;

    @ColumnInfo(name = "menu")
    @SerializedName("app_menu")
    @Expose
    public String menuName;

    @ColumnInfo(name = "remote_resource")
    @SerializedName("app_icon")
    @Expose
    private String remoteIconResource;

    @ColumnInfo(name = "menu_order")
    @SerializedName("order")
    @Expose
    private int order;

    @ColumnInfo(name = "local_resource")
    private String localIconResource = "";

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getRemoteIconResource() {
        return remoteIconResource;
    }

    public void setRemoteIconResource(String remoteIconResource) {
        this.remoteIconResource = remoteIconResource;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getLocalIconResource() {
        return localIconResource;
    }

    public void setLocalIconResource(String localIconResource) {
        this.localIconResource = localIconResource;
    }
}
