package com.mp4andmp3.superextremeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;

public class DownloadVideos extends AppCompatActivity {
    ImageView dailymotionButton;
    ImageView facebookButton;
    ImageView instagramButton;
    ImageView tiktokButton;
    ImageView topbuzzButton;
    ImageView twitterButton;
    ImageView vimeoButton;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_download_videos);
        ((AdView) findViewById(R.id.adView)).loadAd(new Builder().build());
        setTitle("Download Videos");
        this.facebookButton = (ImageView) findViewById(R.id.facebookButton);
        this.instagramButton = (ImageView) findViewById(R.id.instagramButton);
        this.twitterButton = (ImageView) findViewById(R.id.twitterButton);
        this.dailymotionButton = (ImageView) findViewById(R.id.dailymotionButton);
        this.tiktokButton = (ImageView) findViewById(R.id.tiktokButton);
        this.vimeoButton = (ImageView) findViewById(R.id.vimeoButton);
        this.topbuzzButton = (ImageView) findViewById(R.id.topbuzzButton);
        final Intent intent = new Intent(this, GetVideoUrlActivity.class);
        this.facebookButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Facebook");
                DownloadVideos.this.startActivity(intent);
            }
        });
        this.instagramButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Instagram");
                DownloadVideos.this.startActivity(intent);
            }
        });
        this.dailymotionButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Dailymotion");
                DownloadVideos.this.startActivity(intent);
            }
        });
        this.twitterButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Twitter");
                DownloadVideos.this.startActivity(intent);
            }
        });
        this.topbuzzButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Topbuzz");
                DownloadVideos.this.startActivity(intent);
            }
        });
        this.tiktokButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Tiktok");
                DownloadVideos.this.startActivity(intent);
            }
        });
        this.vimeoButton.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                intent.putExtra("Downloader", "Vimeo");
                DownloadVideos.this.startActivity(intent);
            }
        });
    }
}
