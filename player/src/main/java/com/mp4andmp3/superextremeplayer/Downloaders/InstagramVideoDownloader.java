package com.mp4andmp3.superextremeplayer.Downloaders;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;
import com.mp4andmp3.superextremeplayer.Interfaces.VideoDownloader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class InstagramVideoDownloader implements VideoDownloader {

    public String VideoTitle;
    private String VideoURL;

    public Context context;

    private class Data extends AsyncTask<String, String, String> {


       
        public String doInBackground(String... strArr) {
            String readLine;
            String str;
            String str2 = "https";
            String str3 = "amp;";
            String str4 = "og:video";
            String str5 = "og:title";
            String str6 = "No URL";
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                do {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        return str6;
                    }
                    str = "\"";
                    if (readLine.contains(str5)) {
                        InstagramVideoDownloader.this.VideoTitle = readLine.substring(readLine.indexOf(str5));
                        InstagramVideoDownloader.this.VideoTitle = readLine.substring(readLine.indexOf("content"));
                        InstagramVideoDownloader.this.VideoTitle = InstagramVideoDownloader.this.VideoTitle.substring(InstagramVideoDownloader.ordinalIndexOf(InstagramVideoDownloader.this.VideoTitle, str, 0) + 1, InstagramVideoDownloader.ordinalIndexOf(InstagramVideoDownloader.this.VideoTitle, str, 1));
                        Log.e("Hello", InstagramVideoDownloader.this.VideoTitle);
                    }
                } while (!readLine.contains(str4));
                String substring = readLine.substring(readLine.indexOf(str4));
                String substring2 = substring.substring(InstagramVideoDownloader.ordinalIndexOf(substring, str, 1) + 1, InstagramVideoDownloader.ordinalIndexOf(substring, str, 2));
                if (substring2.contains(str3)) {
                    substring2 = substring2.replace(str3, "");
                }
                if (!substring2.contains(str2)) {
                    substring2 = substring2.replace("http", str2);
                }
                Log.e("Hello1", substring2);
                return substring2;
            } catch (IOException unused) {
                return str6;
            }
        }

       
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (!str.contains("No URL")) {
                String createDirectory = InstagramVideoDownloader.this.createDirectory();
                Calendar instance = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                InstagramVideoDownloader instagramVideoDownloader = InstagramVideoDownloader.this;
                StringBuilder sb = new StringBuilder();
                sb.append("instagram");
                sb.append(simpleDateFormat.format(instance.getTime()));
                instagramVideoDownloader.VideoTitle = sb.toString();
                File file = new File(createDirectory, InstagramVideoDownloader.this.VideoTitle);
                try {
                    Request request = new Request(Uri.parse(str));
                    request.allowScanningByMediaScanner();
                    request.setDescription(InstagramVideoDownloader.this.VideoTitle).setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Play Downloader");
                    ((DownloadManager) InstagramVideoDownloader.this.context.getSystemService("download")).enqueue(request);
                } catch (Exception unused) {
                    if (Looper.myLooper() == null) {
                        try {
                            Looper.prepare();
                            Looper.loop();
                        } catch (Exception unused2) {
                        }
                    }
                    Toast.makeText(InstagramVideoDownloader.this.context, "Wrong Video URL or Check Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } else {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Looper.loop();
            }
            Toast.makeText(InstagramVideoDownloader.this.context, "Wrong Video URL or Private Video Can't be downloaded", Toast.LENGTH_SHORT).show();
        }
    }

    public String getVideoId(String str) {
        return str;
    }

    public InstagramVideoDownloader(Context context2, String str) {
        this.context = context2;
        this.VideoURL = str;
    }

    public String createDirectory() {
        File file = new File(String.valueOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)));
        if (!file.exists()) {
            file.mkdirs();
        }
        return file.getPath();
    }

    public void DownloadVideo() {
        new Data().execute(new String[]{getVideoId(this.VideoURL)});
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
