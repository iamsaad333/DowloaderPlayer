package com.mp4andmp3.superextremeplayer.Interfaces;

public interface VideoDownloader {
    void DownloadVideo();

    String createDirectory();

    String getVideoId(String str);
}
