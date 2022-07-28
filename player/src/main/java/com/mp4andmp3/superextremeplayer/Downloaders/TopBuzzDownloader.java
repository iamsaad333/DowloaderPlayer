package com.mp4andmp3.superextremeplayer.Downloaders;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TopBuzzDownloader {
    
    public long DownLoadID;
    
    public String FinalURL;
    
    public String VideoTitle;
    private String VideoURL;
    
    public Context context;

    private class Data extends AsyncTask<String, String, String> {
        private Data() {
        }

        
        public String doInBackground(String... strArr) {
            String str = "http";
            String str2 = "480p";
            String str3 = "\"";
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        return stringBuffer.toString();
                    }
                    if (readLine.contains("INITIAL_STATE") && readLine.contains("title")) {
                        String substring = readLine.substring(readLine.indexOf("videoTitle"));
                        TopBuzzDownloader.this.VideoTitle = substring.substring(TopBuzzDownloader.ordinalIndexOf(substring, str3, 1) + 1, TopBuzzDownloader.ordinalIndexOf(substring, "\\", 2));
                        if (substring.contains(str2)) {
                            String substring2 = substring.substring(substring.indexOf(str2));
                            String substring3 = substring2.substring(substring2.indexOf("main_url"));
                            String replace = substring3.substring(TopBuzzDownloader.ordinalIndexOf(substring3, str3, 1) + 1, TopBuzzDownloader.ordinalIndexOf(substring3, str3, 2)).replace("u002F", "");
                            if (replace.contains(str)) {
                                replace = replace.replace(str, "https");
                            }
                            stringBuffer.append(replace);
                        } else {
                            stringBuffer.append("No URL");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            TopBuzzDownloader.this.FinalURL = str;
            String str2 = "Video Can't be downloaded!";
            if (TopBuzzDownloader.this.FinalURL == null && TopBuzzDownloader.this.FinalURL == "No URL") {
                Toast.makeText(TopBuzzDownloader.this.context, str2, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                String access$400 = TopBuzzDownloader.this.createDirectory();
                Calendar instance = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                TopBuzzDownloader topBuzzDownloader = TopBuzzDownloader.this;
                StringBuilder sb = new StringBuilder();
                sb.append("topbuzz");
                sb.append(simpleDateFormat.format(instance.getTime()));
                topBuzzDownloader.VideoTitle = sb.toString();
                File file = new File(access$400, TopBuzzDownloader.this.VideoTitle);
                Request request = new Request(Uri.parse(TopBuzzDownloader.this.FinalURL));
                request.allowScanningByMediaScanner();
                request.setDescription(TopBuzzDownloader.this.VideoTitle).setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Play Downloader");
                TopBuzzDownloader.this.DownLoadID = ((DownloadManager) TopBuzzDownloader.this.context.getSystemService("download")).enqueue(request);
            } catch (Exception unused) {
                Toast.makeText(TopBuzzDownloader.this.context, str2, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getVideoId(String str) {
        return str;
    }

    public TopBuzzDownloader(Context context2, String str, long j) {
        this.context = context2;
        this.VideoURL = str;
        this.DownLoadID = j;
    }

    public void DownloadVideo() {
        new Data().execute(new String[]{getVideoId(this.VideoURL)});
    }

    
    public String createDirectory() {
        File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    
    public static int ordinalIndexOf(String str, String str2, int i) {
        int i2 = -1;
        while (true) {
            i2 = str.indexOf(str2, i2 + 1);
            int i3 = i - 1;
            if (i <= 0 || i2 == -1) {
                return i2;
            }
            i = i3;
        }
    }
}
