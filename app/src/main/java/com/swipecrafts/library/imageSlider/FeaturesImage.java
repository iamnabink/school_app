package com.swipecrafts.library.imageSlider;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import static java.lang.Boolean.TRUE;

/**
 * Created by Madhusudan Sapkota on 3/13/2018.
 */

@Entity(tableName = "features_images")
public class FeaturesImage {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;

    @SerializedName("featured_img")
    @Expose
    @ColumnInfo(name = "url")
    private String url;

    public FeaturesImage(String url) {
        this.url = url;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "{ \"featured_img\" : \""+ url +"\" }";
    }
}
