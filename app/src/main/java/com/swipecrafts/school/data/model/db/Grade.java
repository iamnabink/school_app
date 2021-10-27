package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 3/4/2018.
 */

@Entity(tableName = "grades")
public class Grade {


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("dc_grade_id")
    @Expose
    private long dcGradeId;

    @ColumnInfo(name = "grade_name")
    @SerializedName("dc_grade_name")
    @Expose
    private String dcGradeName;

    @ColumnInfo(name = "remote_grade_image")
    @SerializedName("dc_grade_bg_img")
    @Expose
    private String remoteDcGradeImg;

    @ColumnInfo(name = "local_grade_image")
    private String localDcGradeBgImg = "";

    @ColumnInfo(name = "grade_order")
    @SerializedName("dc_grade_order")
    @Expose
    private int dcGradeOrder;

    public Grade(@NonNull long dcGradeId, String dcGradeName, String remoteDcGradeImg, String localDcGradeBgImg, int dcGradeOrder) {
        this.dcGradeId = dcGradeId;
        this.dcGradeName = dcGradeName;
        this.remoteDcGradeImg = remoteDcGradeImg;
        this.localDcGradeBgImg = localDcGradeBgImg;
        this.dcGradeOrder = dcGradeOrder;
    }

    @Ignore
    public Grade(@NonNull long dcGradeId, String dcGradeName, String remoteDcGradeImg, int dcGradeOrder) {
        this.dcGradeId = dcGradeId;
        this.dcGradeName = dcGradeName;
        this.remoteDcGradeImg = remoteDcGradeImg;
        this.dcGradeOrder = dcGradeOrder;
    }

    @NonNull
    public long getDcGradeId() {
        return dcGradeId;
    }

    public void setDcGradeId(@NonNull long dcGradeId) {
        this.dcGradeId = dcGradeId;
    }

    public String getDcGradeName() {
        return dcGradeName;
    }

    public void setDcGradeName(String dcGradeName) {
        this.dcGradeName = dcGradeName;
    }

    public String getRemoteDcGradeImg() {
        return remoteDcGradeImg;
    }

    public void setRemoteDcGradeImg(String remoteDcGradeImg) {
        this.remoteDcGradeImg = remoteDcGradeImg;
    }

    public int getDcGradeOrder() {
        return dcGradeOrder;
    }

    public void setDcGradeOrder(int dcGradeOrder) {
        this.dcGradeOrder = dcGradeOrder;
    }

    public String getLocalDcGradeBgImg() {
        return localDcGradeBgImg == null ? "" : localDcGradeBgImg;
    }

    public void setLocalDcGradeBgImg(String localDcGradeBgImg) {
        this.localDcGradeBgImg = localDcGradeBgImg;
    }
}
