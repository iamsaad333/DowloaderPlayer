package com.mp4andmp3.superextremeplayer.Downloaders;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
import android.webkit.URLUtil;
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

public class TiktokVideoDownloader implements VideoDownloader {
    
    public String VideoTitle;
    private String VideoURL;
    
    public Context context;

    private class Data extends AsyncTask<String, String, String> {

        public String doInBackground(String... strArr) {
            String str = "https";
            String str2 = "#";
            String str3 = "videoData";
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        if (readLine.contains(str3)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
                return sb.toString();
            } catch (IOException unused) {
                return "Invalid Video URL or Check Internet Connection";
            }
        }

       
        public void onPostExecute(String str) {
            super.onPostExecute(str);
            if (URLUtil.isValidUrl(str)) {
                String createDirectory = TiktokVideoDownloader.this.createDirectory();
                Calendar instance = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                TiktokVideoDownloader tiktokVideoDownloader = TiktokVideoDownloader.this;
                StringBuilder sb = new StringBuilder();
                sb.append("tiktok");
                sb.append(simpleDateFormat.format(instance.getTime()));
                tiktokVideoDownloader.VideoTitle = sb.toString();
                File file = new File(createDirectory, TiktokVideoDownloader.this.VideoTitle);
                try {
                    Request request = new Request(Uri.parse(str));
                    request.allowScanningByMediaScanner();
                    request.setDescription(TiktokVideoDownloader.this.VideoTitle).setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Play Downloader");
                    ((DownloadManager) TiktokVideoDownloader.this.context.getSystemService("download")).enqueue(request);
                } catch (Exception unused) {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    Toast.makeText(TiktokVideoDownloader.this.context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } else {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Toast.makeText(TiktokVideoDownloader.this.context, str, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }

    public TiktokVideoDownloader(Context context2, String str) {
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

    public String getVideoId(String str) {
        String str2 = "https";
        return !str.contains(str2) ? str.replace("http", str2) : str;
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
