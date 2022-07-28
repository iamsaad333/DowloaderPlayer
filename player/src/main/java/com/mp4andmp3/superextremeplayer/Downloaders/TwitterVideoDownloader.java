package com.mp4andmp3.superextremeplayer.Downloaders;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.webkit.URLUtil;
import android.widget.Toast;
import androidx.core.app.NotificationCompat;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.google.android.gms.common.internal.ImagesContract;
import com.mp4andmp3.superextremeplayer.Interfaces.VideoDownloader;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import org.json.JSONObject;

public class TwitterVideoDownloader implements VideoDownloader {
    
    public String VideoTitle;
    private String VideoURL;
    
    public Context context;

    public TwitterVideoDownloader(Context context2, String str) {
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
        String str2 = "?";
        boolean contains = str.contains(str2);
        String str3 = "/";
        String str4 = NotificationCompat.CATEGORY_STATUS;
        if (contains) {
            String substring = str.substring(str.indexOf(str4));
            return substring.substring(substring.indexOf(str3) + 1, substring.indexOf(str2));
        }
        String substring2 = str.substring(str.indexOf(str4));
        return substring2.substring(substring2.indexOf(str3) + 1);
    }

    public void DownloadVideo() {
        AndroidNetworking.post("https://twittervideodownloaderpro.com/twittervideodownloadv2/index.php").addBodyParameter("id", getVideoId(this.VideoURL)).setPriority(Priority.HIGH).build().getAsJSONObject(new JSONObjectRequestListener() {
            static final  boolean $assertionsDisabled = false;



            public void onResponse(JSONObject jSONObject) {
                String jSONObject2 = jSONObject.toString();
                String str = ImagesContract.URL;
                String str2 = "No Video Found";
                if (jSONObject2.contains(str)) {
                    String substring = jSONObject2.substring(jSONObject2.indexOf(str));
                    String str3 = "\"";
                    String substring2 = substring.substring(TwitterVideoDownloader.ordinalIndexOf(substring, str3, 1) + 1, TwitterVideoDownloader.ordinalIndexOf(substring, str3, 2));
                    String str4 = "\\";
                    if (substring2.contains(str4)) {
                        substring2 = substring2.replace(str4, "");
                    }
                    if (URLUtil.isValidUrl(substring2)) {
                        String createDirectory = TwitterVideoDownloader.this.createDirectory();
                        Calendar instance = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                        TwitterVideoDownloader twitterVideoDownloader = TwitterVideoDownloader.this;
                        StringBuilder sb = new StringBuilder();
                        sb.append("twitter");
                        sb.append(simpleDateFormat.format(instance.getTime()));
                        twitterVideoDownloader.VideoTitle = sb.toString();
                        File file = new File(createDirectory, TwitterVideoDownloader.this.VideoTitle);
                        try {
                            Request request = new Request(Uri.parse(substring2));
                            request.allowScanningByMediaScanner();
                            request.setDescription(TwitterVideoDownloader.this.VideoTitle).setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Play Downloader");
                            ((DownloadManager) TwitterVideoDownloader.this.context.getSystemService("download")).enqueue(request);
                        } catch (Exception unused) {
                            if (Looper.myLooper() == null) {
                                Looper.prepare();
                            }
                            Toast.makeText(TwitterVideoDownloader.this.context, "Video Can't be downloaded! Try Again", Toast.LENGTH_SHORT).show();
                            Looper.loop();
                        }
                    } else {
                        if (Looper.myLooper() == null) {
                            Looper.prepare();
                        }
                        Toast.makeText(TwitterVideoDownloader.this.context, str2, Toast.LENGTH_SHORT).show();
                        Looper.loop();
                    }
                } else {
                    if (Looper.myLooper() == null) {
                        Looper.prepare();
                    }
                    Toast.makeText(TwitterVideoDownloader.this.context, str2, Toast.LENGTH_SHORT).show();
                    Looper.loop();
                }
            }

            public void onError(ANError aNError) {
                if (Looper.myLooper() == null) {
                    Looper.prepare();
                }
                Toast.makeText(TwitterVideoDownloader.this.context, "Invalid Video URL", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        });
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
