package com.swipecrafts.school.utils.fcm;

import android.text.Html;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

/**
 * Created by Madhusudan Sapkota on 3/28/2018.
 */
public class NotificationRes {

    @SerializedName("message")
    @Expose
    private NotificationData notificationData;

    public void setNotificationData(NotificationData notificationData) {
        this.notificationData = notificationData;
    }

    public NotificationData getNotificationData() {
        return notificationData;
    }

    public class NotificationData {

        @SerializedName("title")
        @Expose
        private String title;

        @SerializedName("desc")
        @Expose
        private String message;

        @SerializedName("img_url")
        @Expose
        private String imageUrl;

        @SerializedName("timestamp_ms")
        @Expose
        private String timestamp;

        @SerializedName("payload")
        @Expose
        private JSONObject payload;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getMessage() {
            return Html.fromHtml(message).toString();
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public JSONObject getPayload() {
            return payload;
        }

        public void setPayload(JSONObject payload) {
            this.payload = payload;
        }
    }
}
