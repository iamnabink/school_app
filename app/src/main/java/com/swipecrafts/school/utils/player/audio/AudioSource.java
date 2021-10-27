package com.swipecrafts.school.utils.player.audio;

import com.swipecrafts.school.utils.player.DownloadStatus;

/**
 * Created by Madhusudan Sapkota on 6/29/2018.
 */
public class AudioSource {

    private long identifier;
    private String audioTitle;
    private String remoteUrl;
    private String localUrl;
    private long progress = 0;
    private DownloadStatus downloadStatus = DownloadStatus.NOT_DOWNLOADED;

    public AudioSource(long identifier, String audioTitle, String remoteUrl) {
        this.identifier = identifier;
        this.audioTitle = audioTitle;
        this.remoteUrl = remoteUrl;
    }

    public AudioSource(String audioTitle, String remoteUrl) {
        this.audioTitle = audioTitle;
        this.remoteUrl = remoteUrl;
    }

    public long getIdentifier() {
        return identifier;
    }

    public void setIdentifier(long identifier) {
        this.identifier = identifier;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getRemoteUrl() {
        return remoteUrl;
    }

    public void setRemoteUrl(String remoteUrl) {
        this.remoteUrl = remoteUrl;
    }

    public String getLocalUrl() {
        return localUrl;
    }

    public void setLocalUrl(String localUrl) {
        this.localUrl = localUrl;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    public DownloadStatus getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(DownloadStatus downloadStatus) {
        this.downloadStatus = downloadStatus;
    }
}
