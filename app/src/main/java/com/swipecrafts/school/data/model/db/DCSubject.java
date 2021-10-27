package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static android.arch.persistence.room.ForeignKey.CASCADE;

/**
 * Created by Madhusudan Sapkota on 3/5/2018.
 */

@Entity(tableName = "subjects",
        foreignKeys = @ForeignKey(entity = Grade.class,
        parentColumns = "id",
        childColumns = "gradeId",
        onDelete = CASCADE))
public class DCSubject {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("dc_subject_id")
    @Expose
    private long dcSubjectId;

    @ColumnInfo(name = "subject_code")
    @SerializedName("dc_subject_code")
    @Expose
    private String dcSubjectCode;

    @ColumnInfo(name = "subject_name")
    @SerializedName("dc_subject_name")
    @Expose
    private String dcSubjectName;

    @ColumnInfo(name = "remote_subject_icon")
    @SerializedName("dc_subject_icon")
    @Expose
    private String remoteSubjectIcon;

    @ColumnInfo(name = "local_subject_icon")
    private String localSubjectIcon = "";

    @ColumnInfo(name = "gradeId")
    @Nullable
    private String gradeID;

    public DCSubject(@NonNull long dcSubjectId, String dcSubjectCode, String dcSubjectName, String remoteSubjectIcon, String localSubjectIcon, String gradeID) {
        this.dcSubjectId = dcSubjectId;
        this.dcSubjectCode = dcSubjectCode;
        this.dcSubjectName = dcSubjectName;
        this.remoteSubjectIcon = remoteSubjectIcon;
        this.localSubjectIcon = localSubjectIcon;
        this.gradeID = gradeID;
    }

    @Ignore
    public DCSubject(@NonNull long dcSubjectId, String dcSubjectCode, String dcSubjectName, String remoteSubjectIcon) {
        this.dcSubjectId = dcSubjectId;
        this.dcSubjectCode = dcSubjectCode;
        this.dcSubjectName = dcSubjectName;
        this.remoteSubjectIcon = remoteSubjectIcon;
    }

    @NonNull
    public long getDcSubjectId() {
        return dcSubjectId;
    }

    public void setDcSubjectId(@NonNull long dcSubjectId) {
        this.dcSubjectId = dcSubjectId;
    }

    public String getDcSubjectCode() {
        return dcSubjectCode;
    }

    public void setDcSubjectCode(String dcSubjectCode) {
        this.dcSubjectCode = dcSubjectCode;
    }

    public String getDcSubjectName() {
        return dcSubjectName;
    }

    public void setDcSubjectName(String dcSubjectName) {
        this.dcSubjectName = dcSubjectName;
    }

    public String getRemoteSubjectIcon() {
        return remoteSubjectIcon;
    }

    public void setRemoteSubjectIcon(String remoteSubjectIcon) {
        this.remoteSubjectIcon = remoteSubjectIcon;
    }

    public String getLocalSubjectIcon() {
        return localSubjectIcon == null ? "" : localSubjectIcon;
    }

    public void setLocalSubjectIcon(String localSubjectIcon) {
        this.localSubjectIcon = localSubjectIcon;
    }

    public String getGradeID() {
        return gradeID;
    }

    public void setGradeID(String gradeID) {
        this.gradeID = gradeID;
    }
}
