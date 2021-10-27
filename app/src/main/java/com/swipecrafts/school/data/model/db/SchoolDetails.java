
package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


@Entity(tableName = "school_profile")
public class SchoolDetails {

    @ColumnInfo(name = "id")
    @PrimaryKey
    @NonNull
    private long id;

    @ColumnInfo(name = "school_name")
    @SerializedName("name")
    @Expose
    private String mName;

    @ColumnInfo(name = "etd_date")
    @SerializedName("est_date")
    @Expose
    private String mEstDate;

    @ColumnInfo(name = "name")
    @SerializedName("address")
    @Expose
    private String mAddress;

    @ColumnInfo(name = "email")
    @SerializedName("email")
    @Expose
    private String mEmail;

    @ColumnInfo(name = "website")
    @SerializedName("website")
    @Expose
    private String mWebsite;

    @ColumnInfo(name = "fax")
    @SerializedName("fax")
    @Expose
    private String mFax;

    @ColumnInfo(name = "panno")
    @SerializedName("panno")
    @Expose
    private String mPanno;

    @ColumnInfo(name = "rules")
    @SerializedName("rules")
    @Expose
    private String mRules;

    @ColumnInfo(name = "description")
    @SerializedName("desc")
    @Expose
    private String mDesc;


    @ColumnInfo(name = "school_description")
    @SerializedName("school_description")
    @Expose
    private String mSchoolDescription;

    @ColumnInfo(name = "message")
    @SerializedName("msg")
    @Expose
    private String mMsg;

    @ColumnInfo(name = "feature_image")
    @SerializedName("bgimage")
    @Expose
    private String mBackgroundImage;

    @ColumnInfo(name = "latitude")
    @SerializedName("latitude")
    @Expose
    private String mLatitude;

    @ColumnInfo(name = "longitude")
    @SerializedName("longitude")
    @Expose
    private String mLongitude;

    @ColumnInfo(name = "logo" +
            "")
    @SerializedName("logo")
    @Expose
    private String mLogo;

    @NonNull
    public long getId() {
        return id;
    }

    public void setId(@NonNull long id) {
        this.id = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public String  getEstDate() {
        return mEstDate;
    }

    public void setEstDate(String  mEstDate) {
        this.mEstDate = mEstDate;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public void setWebsite(String mWebsite) {
        this.mWebsite = mWebsite;
    }

    public String getFax() {
        return mFax;
    }

    public void setFax(String mFax) {
        this.mFax = mFax;
    }

    public String getPanno() {
        return mPanno;
    }

    public void setPanno(String mPanno) {
        this.mPanno = mPanno;
    }

    public String getRules() {
        return mRules;
    }

    public void setRules(String mRules) {
        this.mRules = mRules;
    }

    public String getDesc() {
        return mDesc;
    }

    public void setDesc(String mDesc) {
        this.mDesc = mDesc;
    }

    public String getSchoolDescription() {
        return mSchoolDescription == null ? "" : mSchoolDescription;
    }

    public void setSchoolDescription(String schoolDescription) {
        this.mSchoolDescription = schoolDescription;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String mMsg) {
        this.mMsg = mMsg;
    }

    public String getBackgroundImage() {
        return mBackgroundImage;
    }

    public void setBackgroundImage(String mBackgroundImage) {
        this.mBackgroundImage = mBackgroundImage;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String mLatitude) {
        this.mLatitude = mLatitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String mLongitude) {
        this.mLongitude = mLongitude;
    }

    public String getLogo() {
        return mLogo;
    }

    public void setLogo(String mLogo) {
        this.mLogo = mLogo;
    }
}
