package com.swipecrafts.school.utils.player;

/**
 * Created by Madhusudan Sapkota on 6/29/2018.
 */
public enum DownloadStatus {

    DOWNLOADED(1),
    DOWNLOADING(2),
    NOT_DOWNLOADED(3);

    public final int value;

    DownloadStatus(int value) {
        this.value = value;
    }
}
