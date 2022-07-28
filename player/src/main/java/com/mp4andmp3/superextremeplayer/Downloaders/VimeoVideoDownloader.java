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
import com.google.android.gms.common.internal.ImagesContract;
import com.mp4andmp3.superextremeplayer.Interfaces.VideoDownloader;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class VimeoVideoDownloader implements VideoDownloader {
    
    public String VideoTitle;
    private String VideoURL;
    
    public Context context;

    private class Data extends AsyncTask<String, String, String> {

        public String doInBackground(String... strArr) {
            String str = "video/mp4";
            String str2 = "https";
            String str3 = "og:video:url";
            String str4 = "og:title";
            try {
                int i = 0;
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(strArr[0]).openConnection();
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                String str5 = "No URL";
                String str6 = "Wrong Video URL";
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        break;
                    }
                    String str7 = "\"";
                    if (readLine.contains(str4)) {
                        VimeoVideoDownloader.this.VideoTitle = readLine.substring(readLine.indexOf(str4));
                        VimeoVideoDownloader.this.VideoTitle = VimeoVideoDownloader.this.VideoTitle.substring(VimeoVideoDownloader.ordinalIndexOf(VimeoVideoDownloader.this.VideoTitle, str7, 1) + 1, VimeoVideoDownloader.ordinalIndexOf(VimeoVideoDownloader.this.VideoTitle, str7, 2));
                    }
                    if (readLine.contains(str3)) {
                        String substring = readLine.substring(readLine.indexOf(str3));
                        String substring2 = substring.substring(VimeoVideoDownloader.ordinalIndexOf(substring, str7, 1) + 1, VimeoVideoDownloader.ordinalIndexOf(substring, str7, 2));
                        if (!substring2.contains(str2)) {
                            substring2 = substring2.replace("http", str2);
                        }
                        if (URLUtil.isValidUrl(substring2)) {
                            try {
                                HttpURLConnection httpURLConnection2 = (HttpURLConnection) new URL(substring2).openConnection();
                                httpURLConnection2.connect();
                                BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(httpURLConnection2.getInputStream()));
                                StringBuilder sb = new StringBuilder();
                                while (true) {
                                    String readLine2 = bufferedReader2.readLine();
                                    if (readLine2 == null) {
                                        break;
                                    }
                                    sb.append(readLine2);
                                }
                                String sb2 = sb.toString();
                                int access$300 = VimeoVideoDownloader.countOccurences(sb2, str);
                                if (access$300 > 0) {
                                    while (true) {
                                        if (i >= access$300) {
                                            break;
                                        }
                                        String substring3 = sb2.substring(VimeoVideoDownloader.ordinalIndexOf(sb2, str, i));
                                        String substring4 = substring3.substring(substring3.indexOf(ImagesContract.URL) + 1);
                                        String substring5 = substring4.substring(substring4.indexOf(str7) + 1, substring4.indexOf("}"));
                                        if (substring5.contains("360p")) {
                                            substring2 = substring5.substring(substring5.indexOf(str7) + 1, VimeoVideoDownloader.ordinalIndexOf(substring5, str7, 1));
                                            break;
                                        }
                                        substring2 = substring5.substring(substring5.indexOf(str7) + 1, VimeoVideoDownloader.ordinalIndexOf(substring5, str7, 1));
                                        i++;
                                    }
                                }
                            } catch (IOException unused) {
                            }
                        }
                        str6 = substring2;
                        str5 = str6;
                    } else {
                        str5 = str6;
                    }
                }
                return str5;
            } catch (IOException unused2) {
                return "Invalid Video URL or Check Internet Connection";
            }
        }

       
        public void onPostExecute(String str) {
            if (URLUtil.isValidUrl(str)) {
                String createDirectory = VimeoVideoDownloader.this.createDirectory();
                Calendar instance = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                VimeoVideoDownloader vimeoVideoDownloader = VimeoVideoDownloader.this;
                StringBuilder sb = new StringBuilder();
                sb.append("vimeo");
                sb.append(simpleDateFormat.format(instance.getTime()));
                vimeoVideoDownloader.VideoTitle = sb.toString();
                File file = new File(createDirectory, VimeoVideoDownloader.this.VideoTitle);
                try {
                    Request request = new Request(Uri.parse(str));
                    request.allowScanningByMediaScanner();
                    request.setDescription(VimeoVideoDownloader.this.VideoTitle).setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Play Downloader");
                    ((DownloadManager) VimeoVideoDownloader.this.context.getSystemService("download")).enqueue(request);
                } catch (Exception unused) {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    Toast.makeText(VimeoVideoDownloader.this.context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            } else {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Toast.makeText(VimeoVideoDownloader.this.context, str, Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }
    }

    public String getVideoId(String str) {
        return str;
    }

    public VimeoVideoDownloader(Context context2, String str) {
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

    
    public static int countOccurences(String str, String str2) {
        String[] split = str.split("\"");
        int i = 0;
        for (String equals : split) {
            if (str2.equals(equals)) {
                i++;
            }
        }
        return i;
    }
}
