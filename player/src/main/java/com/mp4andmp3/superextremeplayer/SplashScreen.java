package com.mp4andmp3.superextremeplayer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

public class SplashScreen extends AppCompatActivity {
    boolean darkMode;
    private ImageView logo;

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        boolean z = getSharedPreferences("Log", 0).getBoolean("AppDarkMode", false);
        this.darkMode = z;
        if (z) {
            AppCompatDelegate.setDefaultNightMode(2);
        } else {
            AppCompatDelegate.setDefaultNightMode(1);
        }
        setContentView((int) R.layout.activity_splash_screen);
        this.logo = (ImageView) findViewById(R.id.logo);
        this.logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.mysplashanimaation));
        new Handler().postDelayed(new Runnable() {
            public void run() {
                SplashScreen.this.startActivity(new Intent(SplashScreen.this, PlayerMainActivity.class));
                SplashScreen.this.finish();
            }
        }, 2000);
    }
}
