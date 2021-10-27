package com.swipecrafts.school.ui.dashboard.attendance.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class StudentAttendance {

    @SerializedName("stdid")
    @Expose
    private long studentId; // response in student attendance/ students list

    @SerializedName("name")
    @Expose
    private String name; // response in student attendance/ students list

    @SerializedName("status")
    @Expose
    private int mStatus = -1; // response in student attendance

    @SerializedName("acad_id")
    @Expose
    private long acadId;  // response in student list

    @SerializedName("class_id")
    private long mClassId;

    @SerializedName("section_id")
    private long mSectionId;

    @SerializedName("school_id")
    private String schoolId;

    @SerializedName("attendant_id")
    private long mAttendantId;

    @SerializedName("date")
    private Date attendanceDate;


    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setStatus(int mStatus) {
        this.mStatus = mStatus;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public Long getmClassId() {
        return mClassId;
    }

    public void setmClassId(Long mClassId) {
        this.mClassId = mClassId;
    }

    public Long getmSectionId() {
        return mSectionId;
    }

    public void setmSectionId(Long mSectionId) {
        this.mSectionId = mSectionId;
    }

    public long getAcadId() {
        return acadId;
    }

    public void setAcadId(long acadId) {
        this.acadId = acadId;
    }

    public Long getmAttendantId() {
        return mAttendantId;
    }

    public Date getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(Date attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public void setmAttendantId(Long mAttendantId) {
        this.mAttendantId = mAttendantId;
    }

    @Override
    public String toString() {
        return " {\n" +
                "      \"acad_id\":" + acadId + ",\n" +
                "      \"school_id\":" + schoolId + ",\n" +
                "      \"class_id\":" + mClassId + ",\n" +
                "      \"section_id\":" + (mSectionId == -1 ? null : mSectionId) + ",\n" +
                "      \"stdid\":" + studentId + ",\n" +
                "      \"status\":" + mStatus + ",\n" +
                "      \"attendant_id\":" + mAttendantId + "\n" +
                "   }";
    }
}
