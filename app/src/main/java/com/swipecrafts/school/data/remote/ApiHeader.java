package com.swipecrafts.school.data.remote;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.swipecrafts.school.data.DataManager;
import com.swipecrafts.school.di.ApiInfo;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by Madhusudan Sapkota on 2/22/2018.
 */

@Singleton
public class ApiHeader {

    private ProtectedApiHeader mProtectedApiHeader;

    private PublicApiHeader mPublicApiHeader;

    private DataManager.LoggedInMode mLoggedInMode;

    private String mSchoolId;

    @Inject
    public ApiHeader(PublicApiHeader publicApiHeader, String schoolId, ProtectedApiHeader protectedApiHeader, DataManager.LoggedInMode loggedInMode) {
        this.mPublicApiHeader = publicApiHeader;
        this.mSchoolId = schoolId;
        this.mProtectedApiHeader = protectedApiHeader;
        this.mLoggedInMode = loggedInMode;
    }

    public ProtectedApiHeader getProtectedApiHeader() {
        return mProtectedApiHeader;
    }

    public void setProtectedApiHeader(ProtectedApiHeader mProtectedApiHeader) {
        this.mProtectedApiHeader = mProtectedApiHeader;
    }

    public PublicApiHeader getPublicApiHeader() {
        return mPublicApiHeader;
    }

    public DataManager.LoggedInMode getLoggedInMode() {
        return mLoggedInMode;
    }

    public String getSchoolId() {
        return mSchoolId;
    }

    public void setSchoolId(String mSchoolId) {
        this.mSchoolId = mSchoolId;
    }

    public void setLoggedInMode(DataManager.LoggedInMode loggedInMode) {
        this.mLoggedInMode = loggedInMode;
    }

    public static final class ProtectedApiHeader {

        @Expose
        @SerializedName("api_key")
        private String mApiKey;

        @Expose
        @SerializedName("sshkey")
        private String mSSHKey;

        @Expose
        @SerializedName("seskey")
        private String mSESKey;

        @Expose
        @SerializedName("sesnid")
        private String mSESNId;


        @Expose
        @SerializedName("sesuid")
        private long mSESUId;

        public ProtectedApiHeader(String mApiKey, String mSSHKey, String mSESKey, String mSESNId, long mSESUId) {
            this.mApiKey = mApiKey;
            this.mSSHKey = mSSHKey;
            this.mSESKey = mSESKey;
            this.mSESNId = mSESNId;
            this.mSESUId = mSESUId;
        }

        public String getApiKey() {
            return mApiKey;
        }

        public void setApiKey(String mApiKey) {
            this.mApiKey = mApiKey;
        }

        public String getSSHKey() {
            return mSSHKey;
        }

        public void setSSHKey(String mSSHKey) {
            this.mSSHKey = mSSHKey;
        }

        public String getSESKey() {
            return mSESKey;
        }

        public void setSESKey(String mSESKey) {
            this.mSESKey = mSESKey;
        }

        public String getSESNId() {
            return mSESNId;
        }

        public void setSESNId(String mSESNId) {
            this.mSESNId = mSESNId;
        }

        public long getSESUId() {
            return mSESUId;
        }

        public void setSESUId(long mSESUId) {
            this.mSESUId = mSESUId;
        }
    }

    public static final class PublicApiHeader {

        @Expose
        @SerializedName("api_key")
        private String mApiKey;

        @Inject
        public PublicApiHeader(@ApiInfo String apiKey) {
            mApiKey = apiKey;
        }

        public String getApiKey() {
            return mApiKey;
        }

        public void setApiKey(String apiKey) {
            mApiKey = apiKey;
        }
    }

}
