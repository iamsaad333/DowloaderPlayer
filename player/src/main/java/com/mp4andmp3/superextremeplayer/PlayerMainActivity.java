package com.mp4andmp3.superextremeplayer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class PlayerMainActivity extends AppCompatActivity {
    private CardView cvFolders;
    private CardView cvVideos;
    private CardView cvAudios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_player);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getPermissions();


    }

    private void getPermissions() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            bindViews();
            clicks();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
        }
    }

    private void clicks() {
        cvFolders.setOnClickListener(v -> navigateToContent("folder"));
        cvAudios.setOnClickListener(v -> navigateToContent("audio"));
        cvVideos.setOnClickListener(v -> navigateToContent("video"));
    }

    private void navigateToContent(String extra) {
        Intent intent = new Intent(PlayerMainActivity.this, ContentActivity.class);
        intent.putExtra("from", extra);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if ((requestCode == 123) && (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            bindViews();
            clicks();
        } else {
            Toast.makeText(PlayerMainActivity.this,"Please grant permissions to use the application",Toast.LENGTH_LONG).show();
            Intent intent = new  Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
            Uri uri = Uri.fromParts("package", getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }
    }

    private void bindViews() {
        cvFolders = (CardView) findViewById(R.id.cvFolders);
        cvVideos = (CardView) findViewById(R.id.cvVideos);
        cvAudios = (CardView) findViewById(R.id.cvAudios);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}