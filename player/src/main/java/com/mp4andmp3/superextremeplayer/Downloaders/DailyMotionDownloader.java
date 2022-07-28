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

public class DailyMotionDownloader {
   
    public long DownLoadID;
   
    public String FinalURL;
   
    public String Quality;
    private String VideoURL;
   
    public Context context;

    private class Data extends AsyncTask<String, String, String> {
        private Data() {
        }

      
        public String doInBackground(String... strArr) {
            String str = "144";
            try {
                StringBuilder sb = new StringBuilder();
                sb.append("https://www.dailymotion.com/embed/video/");
                sb.append(strArr[0]);
                sb.append("?autoplay=1?__a=1");
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(sb.toString()).openConnection();
                httpURLConnection.connect();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                StringBuffer stringBuffer = new StringBuffer();
                while (true) {
                    String readLine = bufferedReader.readLine();
                    if (readLine == null) {
                        return stringBuffer.toString();
                    }
                    if (readLine.contains("var config")) {
                        String substring = readLine.substring(readLine.lastIndexOf("qualities") + 1);
                        String access$100 = DailyMotionDownloader.this.Quality;
                        char c = 65535;
                        switch (access$100.hashCode()) {
                            case 1511455:
                                if (access$100.equals("144p")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case 1541122:
                                if (access$100.equals("240p")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 1574757:
                                if (access$100.equals("380p")) {
                                    c = 2;
                                    break;
                                }
                                break;
                            case 1604548:
                                if (access$100.equals("480p")) {
                                    c = 3;
                                    break;
                                }
                                break;
                            case 1688155:
                                if (access$100.equals("720p")) {
                                    c = 4;
                                    break;
                                }
                                break;
                        }
                        String str2 = "240";
                        String str3 = "\"";
                        String str4 = "https";
                        String str5 = "}";
                        String str6 = "video\\/mp4\"";
                        String str7 = "Video Quality not available, Select another";
                        if (c != 0) {
                            String str8 = "380";
                            if (c != 1) {
                                String str9 = "480";
                                if (c != 2) {
                                    String str10 = "720";
                                    if (c != 3) {
                                        if (c == 4) {
                                            if (substring.contains(str10)) {
                                                String substring2 = substring.substring(substring.indexOf(str10), substring.indexOf("1080"));
                                                String substring3 = substring2.substring(substring2.indexOf(str6), substring2.lastIndexOf(str5));
                                                stringBuffer.append(substring3.substring(substring3.indexOf(str4), substring3.lastIndexOf(str3)));
                                            } else {
                                                Toast.makeText(DailyMotionDownloader.this.context, str7, Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    } else if (substring.contains(str9)) {
                                        String substring4 = substring.substring(substring.indexOf(str9), substring.indexOf(str10));
                                        String substring5 = substring4.substring(substring4.indexOf(str6), substring4.lastIndexOf(str5));
                                        stringBuffer.append(substring5.substring(substring5.indexOf(str4), substring5.lastIndexOf(str3)));
                                    } else {
                                        Toast.makeText(DailyMotionDownloader.this.context, str7, Toast.LENGTH_SHORT).show();
                                    }
                                } else if (substring.contains(str8)) {
                                    String substring6 = substring.substring(substring.indexOf(str8), substring.indexOf(str9));
                                    String substring7 = substring6.substring(substring6.indexOf(str6), substring6.lastIndexOf(str5));
                                    stringBuffer.append(substring7.substring(substring7.indexOf(str4), substring7.lastIndexOf(str3)));
                                } else {
                                    Toast.makeText(DailyMotionDownloader.this.context, str7, Toast.LENGTH_SHORT).show();
                                }
                            } else if (substring.contains(str2)) {
                                String substring8 = substring.substring(substring.indexOf(str2), substring.indexOf(str8));
                                String substring9 = substring8.substring(substring8.indexOf(str6), substring8.lastIndexOf(str5));
                                stringBuffer.append(substring9.substring(substring9.indexOf(str4), substring9.lastIndexOf(str3)));
                            } else {
                                Toast.makeText(DailyMotionDownloader.this.context, str7, Toast.LENGTH_SHORT).show();
                            }
                        } else if (substring.contains(str)) {
                            String substring10 = substring.substring(substring.indexOf(str), substring.indexOf(str2));
                            String substring11 = substring10.substring(substring10.indexOf(str6), substring10.lastIndexOf(str5));
                            stringBuffer.append(substring11.substring(substring11.indexOf(str4), substring11.lastIndexOf(str3)));
                        } else {
                            Toast.makeText(DailyMotionDownloader.this.context, str7, Toast.LENGTH_SHORT).show();
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
            DailyMotionDownloader.this.FinalURL = str;
            try {
                String access$400 = DailyMotionDownloader.this.createDirectory();
                Calendar instance = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                StringBuilder sb = new StringBuilder();
                sb.append("vimeo");
                sb.append(simpleDateFormat.format(instance.getTime()));
                File file = new File(access$400, sb.toString());
                Request request = new Request(Uri.parse(DailyMotionDownloader.this.FinalURL));
                request.allowScanningByMediaScanner();
                request.setDescription("Video Downloading").setAllowedNetworkTypes(1).setAllowedNetworkTypes(2).setDestinationUri(Uri.fromFile(file)).setNotificationVisibility(1).setVisibleInDownloadsUi(true).setTitle("Downloading");
                DailyMotionDownloader.this.DownLoadID = ((DownloadManager) DailyMotionDownloader.
                        this.context.getSystemService("download")).enqueue(request);
            } catch (Exception unused) {
                Toast.makeText(DailyMotionDownloader.this.context, "Video Can't be downloaded!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public DailyMotionDownloader(Context context2, String str, String str2, int i) {
        this.context = context2;
        this.Quality = str;
        this.VideoURL = str2;
        this.DownLoadID = (long) i;
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

    private String getVideoId(String str) {
        String str2;
        String str3 = "?";
        String str4 = "video/";
        if (str.contains(str3)) {
            str2 = str.substring(str.indexOf(str4) + 1, str.indexOf(str3));
        } else {
            str2 = str.substring(str.indexOf(str4) + 1);
        }
        return str2.substring(str2.lastIndexOf("/") + 1);
    }
}
