package com.swipecrafts.school.ui.dashboard.assignment.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Assignment {
//    {
//        "aid": "6",
//            "atitle": "hh",
//            "filetype": "JPG",
//            "afile": "http://localhost/sms_app/uploads/afiles/",
//            "uploaded_at": "2018-04-05 17:16:25",
//            "subject": "English"
//    }

    @SerializedName("aid")
    @Expose
    private long id;

    @SerializedName("atitle")
    @Expose
    private String title;

    @SerializedName("filetype")
    @Expose
    private String fileType;

    @SerializedName("afile")
    @Expose
    private String fileUrl;

    @SerializedName("uploaded_at")
    @Expose
    private String uploadedAt;

    @SerializedName("subject")
    @Expose
    private String subjectName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(String uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }
}
