package com.mp4andmp3.superextremeplayer;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.material.snackbar.Snackbar;
import com.mp4andmp3.superextremeplayer.Downloaders.DailyMotionDownloader;
import com.mp4andmp3.superextremeplayer.Downloaders.FbVideoDownloader;
import com.mp4andmp3.superextremeplayer.Downloaders.InstagramVideoDownloader;
import com.mp4andmp3.superextremeplayer.Downloaders.TiktokVideoDownloader;
import com.mp4andmp3.superextremeplayer.Downloaders.TopBuzzDownloader;
import com.mp4andmp3.superextremeplayer.Downloaders.TwitterVideoDownloader;
import com.mp4andmp3.superextremeplayer.Downloaders.VimeoVideoDownloader;

public class GetVideoUrlActivity extends AppCompatActivity {
    RadioButton btn = null;
    Button button;
    EditText editText;
    TextView noteText;
    TextView pointOne;
    TextView pointTwo;
    TextView pointsText;
    RadioGroup radioGroup;
    LinearLayout radioGroupLayout;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_get_video_url);
        this.button = (Button) findViewById(R.id.button);
        this.editText = (EditText) findViewById(R.id.editText);
        this.pointOne = (TextView) findViewById(R.id.pointOne);
        this.pointTwo = (TextView) findViewById(R.id.pointTwo);
        this.pointsText = (TextView) findViewById(R.id.pointsText);
        this.noteText = (TextView) findViewById(R.id.noteText);
        this.radioGroupLayout = (LinearLayout) findViewById(R.id.radioGroupLayout);
        this.radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        setTitle("Play Video Downloader");
        internetPermission();
        ((AdView) findViewById(R.id.adView)).loadAd(new Builder().build());
        final String stringExtra = getIntent().getStringExtra("Downloader");
        TextView textView = this.pointsText;
        StringBuilder sb = new StringBuilder();
        sb.append("We can help you to download ");
        sb.append(stringExtra);
        sb.append(" video follow these steps :");
        textView.setText(sb.toString());
        TextView textView2 = this.pointOne;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("1. Open your ");
        sb2.append(stringExtra);
        sb2.append(" app, open and watch wanted video for few seconds then copy video's link or path.");
        textView2.setText(sb2.toString());
        this.pointTwo.setText("2. Come back here, paste the video path or link in the below box and click 'DOWNLOAD VIDEO' button.");
        this.noteText.setText("If video is not downloading watch it for few seconds then copy link.\n\nYour video will automatically download in background. Please wait after clicking 'DOWNLOAD BUTTON'.");
        if (stringExtra.equals("Dailymotion")) {
            this.radioGroupLayout.setVisibility(View.VISIBLE);
        }
        this.button.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (GetVideoUrlActivity.this.isConnectingToInternet()) {
                    String obj = GetVideoUrlActivity.this.editText.getText().toString();
                    String str = "";
                    if (obj.equals(str) || obj.equals(null)) {
                        Snackbar.make(GetVideoUrlActivity.this.findViewById(R.id.mainLayout), (CharSequence) "Sorry, first paste video URL", 0).show();
                        return;
                    }
                    try {
                        if (stringExtra.equals("Facebook")) {
                            new FbVideoDownloader(GetVideoUrlActivity.this, obj).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        } else if (stringExtra.equals("Instagram")) {
                            new InstagramVideoDownloader(GetVideoUrlActivity.this, obj).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        } else if (stringExtra.equals("Dailymotion")) {
                            new DailyMotionDownloader(GetVideoUrlActivity.this, GetVideoUrlActivity.this.getVideoQuality(), obj, 12).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        } else if (stringExtra.equals("Tiktok")) {
                            new TiktokVideoDownloader(GetVideoUrlActivity.this, obj).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        } else if (stringExtra.equals("Twitter")) {
                            new TwitterVideoDownloader(GetVideoUrlActivity.this, obj).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        } else if (stringExtra.equals("Topbuzz")) {
                            new TopBuzzDownloader(GetVideoUrlActivity.this, obj, 12).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        } else if (stringExtra.equals("Vimeo")) {
                            new VimeoVideoDownloader(GetVideoUrlActivity.this, obj).DownloadVideo();
                            GetVideoUrlActivity.this.editText.setText(str);
                        }
                    } catch (Exception e) {
                        View findViewById = GetVideoUrlActivity.this.findViewById(R.id.mainLayout);
                        StringBuilder sb = new StringBuilder();
                        sb.append(str);
                        sb.append(e.getMessage());
                        Snackbar.make(findViewById, (CharSequence) sb.toString(), 0).show();
                    }
                } else {
                    Snackbar.make(GetVideoUrlActivity.this.findViewById(R.id.mainLayout), (CharSequence) "Sorry, no internet", 0).show();
                }
            }
        });
    }

    private void internetPermission() {
        if (VERSION.SDK_INT >= 23) {
            String str = "android.permission.INTERNET";
            if (checkSelfPermission(str) != 0) {
                String str2 = "android.permission.ACCESS_NETWORK_STATE";
                if (checkSelfPermission(str2) != 0) {
                    ActivityCompat.requestPermissions(this, new String[]{str, str2}, 1);
                    recreate();
                }
            }
        }
    }

    
    public boolean isConnectingToInternet() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    
    public String getVideoQuality() {
        if (this.radioGroup.getCheckedRadioButtonId() != -1) {
            this.btn = (RadioButton) findViewById(this.radioGroup.getCheckedRadioButtonId());
        }
        return this.btn.getText().toString();
    }
}
