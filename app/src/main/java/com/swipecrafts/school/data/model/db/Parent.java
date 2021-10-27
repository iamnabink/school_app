
package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "parents")
public class Parent {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("std_parent_id")
    @Expose
    private long parentId;

    @ColumnInfo(name = "first_name")
    @SerializedName("pfname")
    @Expose
    private String firstName = "";

    @ColumnInfo(name = "middle_name")
    @SerializedName("pmname")
    @Expose
    private String middleName = "";

    @ColumnInfo(name = "last_name")
    @SerializedName("plname")
    @Expose
    private String lastName = "";

    @ColumnInfo(name = "phone")
    @SerializedName("contact")
    @Expose
    private String phone;

    @ColumnInfo(name = "address")
    @SerializedName("address")
    @Expose
    private String address;

    @ColumnInfo(name = "occupation")
    @SerializedName("occupation")
    @Expose
    private String occupation;

    @ColumnInfo(name = "office")
    @SerializedName("office")
    private String office;

    @ColumnInfo(name = "relation")
    @SerializedName("relation")
    @Expose
    private String relation;

    @ColumnInfo(name = "type")
    @SerializedName("type")
    @Expose
    private String type;

    @ColumnInfo(name = "qualification")
    @SerializedName("qualification")
    @Expose
    private String qualification;

    @NonNull
    public long getParentId() {
        return parentId;
    }

    public void setParentId(@NonNull long parentId) {
        this.parentId = parentId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName == null ? "" : middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }
}
