package com.mp4andmp3.superextremeplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;

public class ContentActivity extends AppCompatActivity {

    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        getExtras();
        openFragment();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }





    private void openFragment() {
        Bundle bundle = new Bundle();
        String str = "ListType";
        if (action.equals("audio")) {
            bundle.putString(str, "audio");
            AudioVideoFoldersFragment audioVideoFoldersFragment2 = new AudioVideoFoldersFragment();
            audioVideoFoldersFragment2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, audioVideoFoldersFragment2).commit();
        } else if (action.equals("video")) {
            bundle.putString(str, "video");
            AudioVideoFoldersFragment audioVideoFoldersFragment3 = new AudioVideoFoldersFragment();
            audioVideoFoldersFragment3.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,audioVideoFoldersFragment3).commit();
        } else {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ExtraFragment()).commit();
        }
    }

    private void getExtras() {
        action = getIntent().getStringExtra("from");
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
