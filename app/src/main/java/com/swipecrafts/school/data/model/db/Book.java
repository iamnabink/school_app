
package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "books")
public class Book {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("bid")
    @Expose
    private long mBookId;

    @ColumnInfo(name = "book_title")
    @SerializedName("btitle")
    @Expose
    private String mBookTitle;

    @ColumnInfo(name = "book_image_url")
    @SerializedName("bimg")
    @Expose
    private String mBookImgUrl;

    @ColumnInfo(name = "book_price")
    @SerializedName("bprice")
    @Expose
    private String mBookPrice;

    @ColumnInfo(name = "book_publication")
    @SerializedName("bpub")
    @Expose
    private String mBookPub;

    @ColumnInfo(name = "book_writer")
    @SerializedName("bwriter")
    @Expose
    private String mBookWriter;

    @ColumnInfo(name = "class_id")
    @SerializedName("cid")
    @Expose
    private long mClassId;

    @ColumnInfo(name = "class_name")
    @SerializedName("cname")
    @Expose
    private String mClassName;

    @ColumnInfo(name = "subject_id")
    @SerializedName("sbid")
    @Expose
    private long mSubjectId;

    @ColumnInfo(name = "subject_name")
    @SerializedName("sbname")
    private String mSubjectName;


    @NonNull
    public long getBookId() {
        return mBookId;
    }

    public void setBookId(@NonNull long mBookId) {
        this.mBookId = mBookId;
    }

    public String getBookTitle() {
        return mBookTitle;
    }

    public void setBookTitle(String mBookTitle) {
        this.mBookTitle = mBookTitle;
    }

    public String getBookImgUrl() {
        return mBookImgUrl;
    }

    public void setBookImgUrl(String mBookImgUrl) {
        this.mBookImgUrl = mBookImgUrl;
    }

    public String getBookPrice() {
        return mBookPrice;
    }

    public void setBookPrice(String mBookPrice) {
        this.mBookPrice = mBookPrice;
    }

    public String getBookPub() {
        return mBookPub;
    }

    public void setBookPub(String mBookPub) {
        this.mBookPub = mBookPub;
    }

    public String getBookWriter() {
        return mBookWriter;
    }

    public void setBookWriter(String mBookWriter) {
        this.mBookWriter = mBookWriter;
    }

    public long getClassId() {
        return mClassId;
    }

    public void setClassId(long mClassId) {
        this.mClassId = mClassId;
    }

    public String getClassName() {
        return mClassName;
    }

    public void setClassName(String mClassName) {
        this.mClassName = mClassName;
    }

    public long getSubjectId() {
        return mSubjectId;
    }

    public void setSubjectId(long mSubjectId) {
        this.mSubjectId = mSubjectId;
    }

    public String getSubjectName() {
        return mSubjectName;
    }

    public void setSubjectName(String mSubjectName) {
        this.mSubjectName = mSubjectName;
    }

    @Override
    public String toString() {
        return "{\n" +
                "        \"bid\": "+ mBookId +",\n" +
                "        \"btitle\": "+ mBookTitle +" ,\n" +
                "        \"bwriter\": "+ mBookWriter +" ,\n" +
                "        \"bimg\":  "+ mBookImgUrl +",\n" +
                "        \"sbid\": "+ mSubjectId +" ,\n" +
                "        \"sbname\":  "+ mSubjectName +",\n" +
                "        \"cid\": "+ mClassId +",\n" +
                "        \"cname\": "+ mClassName +",\n" +
                "        \"bpub\": "+ mBookPub +",\n" +
                "        \"bprice\": "+ mBookPrice +"\n" +
                "    }\n";
    }
}
