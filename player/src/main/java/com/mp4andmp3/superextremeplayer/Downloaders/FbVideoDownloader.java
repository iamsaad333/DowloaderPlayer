package com.mp4andmp3.superextremeplayer.Downloaders;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Looper;
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

public class FbVideoDownloader implements VideoDownloader {

    public long DownLoadID;

    public String VideoTitle;
    private String VideoURL;

    public Context context;

    private class Data extends AsyncTask<String, String, String> {


      
        public String doInBackground(String... strArr) {
            String readLine;
            String str = "https";
            String str2 = "amp;";
            String str3 = "og:title";
            String str4 = "og:video:url";
            String str5 = "No URL";
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                do {
                    readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        return str5;
                    }
                } while (!readLine.contains(str4));
                String substring = readLine.substring(readLine.indexOf(str4));
                String str6 = "\"";
                if (substring.contains(str3)) {
                    FbVideoDownloader.this.VideoTitle = substring.substring(substring.indexOf(str3));
                    FbVideoDownloader.this.VideoTitle = FbVideoDownloader.this.VideoTitle.substring(FbVideoDownloader.ordinalIndexOf(FbVideoDownloader.this.VideoTitle, str6, 1) + 1, FbVideoDownloader.ordinalIndexOf(FbVideoDownloader.this.VideoTitle, str6, 2));
                }
                String substring2 = substring.substring(FbVideoDownloader.ordinalIndexOf(substring, str6, 1) + 1, FbVideoDownloader.ordinalIndexOf(substring, str6, 2));
                if (substring2.contains(str2)) {
                    substring2 = substring2.replace(str2, "");
                }
                if (!substring2.contains(str)) {
                    substring2 = substring2.replace("http", str);
                }
                return substring2;
            } catch (IOException unused) {
                return str5;
            }
        }

      
        public void onPostExecute(String str) {
            String str2 = "Video Can't be downloaded! Try Again";
            if (!str.contains("No URL")) {
                String createDirectory = FbVideoDownloader.this.createDirectory();
                Calendar instance = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                FbVideoDownloader fbVideoDownloader = FbVideoDownloader.this;
                StringBuilder sb = new StringBuilder();
                sb.append("facebook");
                sb.append(simpleDateFormat.format(instance.getTime()));
                fbVideoDownloader.VideoTitle = sb.toString();
                File file = new File(createDirectory, FbVideoDownloader.this.VideoTitle);
                try {
                    Request request = new Request(Uri.parse(str));
                    request.allowScanningByMediaScanner();
                    request.setDescription(FbVideoDownloader.this.VideoTitle).setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Play Downloader");
                    FbVideoDownloader.this.DownLoadID = ((DownloadManager) FbVideoDownloader.this.context.getSystemService("download")).enqueue(request);
                } catch (Exception unused) {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                        Looper.loop();
                        Toast.makeText(FbVideoDownloader.this.context, str2, Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                    Looper.loop();
                }
                Toast.makeText(FbVideoDownloader.this.context, str2, Toast.LENGTH_SHORT).show();
            }
        }

      
        public void onCancelled() {
            super.onCancelled();
            if (Looper.myLooper() == null) {
                Looper.prepare();
                Looper.loop();
                Toast.makeText(FbVideoDownloader.this.context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getVideoId(String str) {
        return str;
    }

    public FbVideoDownloader(Context context2, String str) {
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
