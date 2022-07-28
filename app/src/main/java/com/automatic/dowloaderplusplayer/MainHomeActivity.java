package com.automatic.dowloaderplusplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class MainHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        findViewById(R.id.cvDownloader).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDownloader();
            }
        });
        findViewById(R.id.cvPlayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayer();
            }
        });

    }

    private void openPlayer() {
        try {
            startActivity(new Intent(this,Class.forName("com.mp4andmp3.superextremeplayer.PlayerMainActivity")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void openDownloader() {
        try {
            startActivity(new Intent(this,Class.forName("com.smartsocial.hdvideodownloader.activity.MainActivity")));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}