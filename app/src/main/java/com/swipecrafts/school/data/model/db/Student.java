
package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(tableName = "students")
public class Student {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("stdid")
    @Expose
    private long studentId;

    @ColumnInfo(name = "registration_id")
    @SerializedName("regno")
    @Expose
    private String registrationNo;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    @Expose
    private String name = "";

    @Ignore
    @SerializedName("acad_id")
    @Expose
    private long acadId;

    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;

    @ColumnInfo(name = "phone")
    @SerializedName("contact")
    @Expose
    private String phone = "";

    @ColumnInfo(name = "gender")
    @SerializedName("gender")
    @Expose
    private String gender = "";

    @ColumnInfo(name = "date_of_birth")
    @SerializedName("dob")
    @Expose
    private String dateOfBirth ="";

    @ColumnInfo(name = "blood_group")
    @SerializedName("bg_type")
    @Expose
    private String bloodGroup ="";

    @ColumnInfo(name = "religion")
    @SerializedName("religion")
    @Expose
    private String religion = "";

    @ColumnInfo(name = "caste")
    @SerializedName("caste")
    @Expose
    private String caste = "";

    @ColumnInfo(name = "ethnics_group")
    @SerializedName("ethnics_group")
    @Expose
    private String ethnicsGroup = "";

    @ColumnInfo(name = "category")
    @SerializedName("category")
    @Expose
    private String category = "";

    @ColumnInfo(name = "academic_year")
    @SerializedName("academic_year")
    @Expose
    private String academicYear = "";

    @ColumnInfo(name = "class_name")
    @SerializedName("grade")
    @Expose
    private String className;

    @ColumnInfo(name = "section")
    @SerializedName("section")
    @Expose
    private String section = "";

    @ColumnInfo(name = "profile_img")
    @SerializedName("profile_img")
    @Expose
    private String profileImg = "";

    @ColumnInfo(name = "user_id")
    @ForeignKey(entity = User.class, parentColumns = "id", childColumns = "user_id", onDelete = CASCADE)
    private long userId;

    @Ignore
    @SerializedName("parents")
    @Expose
    private List<Parent> parents = null;

    @NonNull
    public long getStudentId() {
        return studentId;
    }

    public void setStudentId(@NonNull long studentId) {
        this.studentId = studentId;
    }

    public long getAcadId() {
        return acadId;
    }

    public void setAcadId(long acadId) {
        this.acadId = acadId;
    }

    public String getRegistrationNo() {
        return registrationNo;
    }

    public void setRegistrationNo(String registrationNo) {
        this.registrationNo = registrationNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getEthnicsGroup() {
        return ethnicsGroup;
    }

    public void setEthnicsGroup(String ethnicsGroup) {
        this.ethnicsGroup = ethnicsGroup;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getProfileImg() {
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public List<Parent> getParents() {
        return parents;
    }

    public void setParents(List<Parent> parents) {
        this.parents = parents;
    }
}
