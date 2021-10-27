package com.swipecrafts.school.data.model.db;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Madhusudan Sapkota on 2/11/2018.
 */

@Entity(tableName = "users")
public class User {


//        "sesnid": "24",
//         "seskey": "$2y$10$Rxbiv6A4eQ5BQY808qxWpeyxk6FVtZLwKvt0qXiNs5rvevQ6qZXcW",
//         "sesuid": "1",
//         "sshkey": "$2y$10$IFt9urqVrmYTx5VL0VL6j.R6uDZoLHsota.hL4vPgqibXcncPBM2q"
//         "sesusr": "parent"


    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("sesuid")
    @Expose
    private long sessionUId;

    @ColumnInfo(name = "user_type")
    @SerializedName("sesusr")
    @Expose
    private String userType;

    @ColumnInfo(name = "session_id")
    @SerializedName("sesnid")
    @Expose
    private String sessionID;

    @ColumnInfo(name = "session_key")
    @NonNull
    @SerializedName("seskey")
    @Expose
    private String sessionKey;

    @ColumnInfo(name = "ssh_key")
    @NonNull
    @SerializedName("sshkey")
    @Expose
    private String sshKey;

    @ColumnInfo(name = "name")
    @Nullable
    private String name;

    @ColumnInfo(name = "remote_img_url")
    @Nullable
    private String remoteImgUrl;

    @ColumnInfo(name = "active_user")
    @NonNull
    private int isActive;

    @NonNull
    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(@NonNull String sessionID) {
        this.sessionID = sessionID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @NonNull
    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(@NonNull String sessionKey) {
        this.sessionKey = sessionKey;
    }

    @NonNull
    public long getSessionUId() {
        return sessionUId;
    }

    public void setSessionUId(@NonNull long sessionUId) {
        this.sessionUId = sessionUId;
    }

    @NonNull
    public String getSshKey() {
        return sshKey;
    }

    public void setSshKey(@NonNull String sshKey) {
        this.sshKey = sshKey;
    }

    @Nullable
    public String getName() {
        return name;
    }

    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Nullable
    public String getRemoteImgUrl() {
        return remoteImgUrl;
    }

    public void setRemoteImgUrl(@Nullable String remoteImgUrl) {
        this.remoteImgUrl = remoteImgUrl;
    }

    @NonNull
    public int isActive() {
        return isActive;
    }

    public void isActive(@NonNull int isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "{\n" +
                "        \"sesnid\": "+ sessionID +",\n" +
                "        \"seskey\": "+ sessionKey +",\n" +
                "        \"sesuid\": "+ sessionUId +",\n" +
                "        \"sshkey\":  "+ sshKey +"\n" +
                "        \"sesusr\":  "+ userType +"\n" +
                "    ";
    }

    public static class SubVideo {
        private String fullvideo;
        private  String title;
        private  String page;

        public SubVideo(String fullvideo, String title, String page) {
            this.fullvideo = fullvideo;
            this.title = title;
            this.page = page;
        }

        public String getFullvideo() {
            return fullvideo;
        }

        public void setFullvideo(String fullvideo) {
            this.fullvideo = fullvideo;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getPage() {
            return page;
        }

        public void setPage(String page) {
            this.page = page;
        }
    }
}
