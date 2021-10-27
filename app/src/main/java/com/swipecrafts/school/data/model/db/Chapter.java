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

@Entity(tableName = "chapters",
        foreignKeys = @ForeignKey(entity = DCSubject.class,
        parentColumns = "id",
        childColumns = "subject_id",
        onDelete = CASCADE))
public class Chapter {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("dc_chapter_id")
    @Expose
    private long dcChapterId;

    @ColumnInfo(name = "title")
    @SerializedName("dc_chapter_title")
    @Expose
    private String dcChapterTitle;

    @ColumnInfo(name = "is_premium")
    @SerializedName("is_premium")
    @Expose
    private String isPremium;

    @ColumnInfo(name = "subject_id")
    @Nullable
    private long subjectId;

    public Chapter(@NonNull long dcChapterId, String dcChapterTitle, String isPremium, long subjectId) {
        this.dcChapterId = dcChapterId;
        this.dcChapterTitle = dcChapterTitle;
        this.isPremium = isPremium;
        this.subjectId = subjectId;
    }

    @Ignore
    public Chapter(@NonNull long dcChapterId, String dcChapterTitle, String isPremium) {
        this.dcChapterId = dcChapterId;
        this.dcChapterTitle = dcChapterTitle;
        this.isPremium = isPremium;
    }

    @NonNull
    public long getDcChapterId() {
        return dcChapterId;
    }

    public void setDcChapterId(@NonNull long dcChapterId) {
        this.dcChapterId = dcChapterId;
    }

    public String getDcChapterTitle() {
        return dcChapterTitle;
    }

    public void setDcChapterTitle(String dcChapterTitle) {
        this.dcChapterTitle = dcChapterTitle;
    }

    public String getIsPremium() {
        return isPremium;
    }

    public void setIsPremium(String isPremium) {
        this.isPremium = isPremium;
    }

    public long getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(long subjectId) {
        this.subjectId = subjectId;
    }
}
