package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "gender")
public class Gender {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    private String id;

    @SerializedName("category_id")
    @Expose
    private long categoryId;

    @ColumnInfo(name = "category")
    @SerializedName("category")
    @Expose
    private String mCategory;

    @ColumnInfo(name = "code")
    @SerializedName("code")
    @Expose
    private String mCode;

    @ColumnInfo(name = "description")
    @SerializedName("description")
    @Expose
    private String mDescription;

    @ColumnInfo(name = "remote_img_url")
    @SerializedName("img")
    @Expose
    private String mCategoryImage = "";

    @ColumnInfo(name = "day_name")
    private String dayName;

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    @NonNull
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(@NonNull long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getCode() {
        return mCode;
    }

    public void setCode(String mCode) {
        this.mCode = mCode;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getCategoryImage() {
        return mCategoryImage;
    }

    public void setCategoryImage(String mCategoryImage) {
        this.mCategoryImage = mCategoryImage;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
        this.id = dayName+"_"+ categoryId;
    }

    @Override
    public String toString() {
        return " {\n" +
                "                \"category\": \""+ mCategory +"\",\n" +
                "                \"code\": \""+ mCode +"\",\n" +
                "                \"description\": \""+ mDescription +"\",\n" +
                "                \"img\": \""+ mCategoryImage +"\"\n" +
                "            }";
    }
}
