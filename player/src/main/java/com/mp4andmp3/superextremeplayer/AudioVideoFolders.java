package com.mp4andmp3.superextremeplayer;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;

public class AudioVideoFolders extends AppCompatActivity implements OnNavigationItemSelectedListener {
    AudioVideoFoldersFragment audioVideoFoldersFragment = new AudioVideoFoldersFragment();
    private boolean linearLayout;
    private Menu menu;
    protected BottomNavigationView navigationView;
    AudioVideoFoldersFragment selectedFragment = null;

   
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_audio_video_folders);
        if (VERSION.SDK_INT >= 23) {
            String str = "android.permission.READ_EXTERNAL_STORAGE";
            if (checkSelfPermission(str) != 0) {
                String str2 = "android.permission.WRITE_EXTERNAL_STORAGE";
                if (checkSelfPermission(str2) != 0) {
                    ActivityCompat.requestPermissions(this, new String[]{str, str2}, 1);
                    recreate();
                    return;
                }
            }
        }
        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        this.navigationView = bottomNavigationView;
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Bundle bundle2 = new Bundle();
        this.linearLayout = getSharedPreferences("Log", 0).getBoolean("foldersLinearLayout", true);
        bundle2.putString("ListType", "video");
        this.audioVideoFoldersFragment.setArguments(bundle2);
        this.navigationView.post(new Runnable() {
            public void run() {
                AudioVideoFolders.this.navigationView.getMenu().findItem(R.id.video_fragment).setChecked(true);
            }
        });
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, this.audioVideoFoldersFragment).commit();
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.menu_main, menu2);
        this.menu = menu2;
        changeLayoutIcon();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.folderLayout) {
            boolean z = !this.linearLayout;
            this.linearLayout = z;
            this.audioVideoFoldersFragment.checkLayout(z);
            AudioVideoFoldersFragment audioVideoFoldersFragment2 = this.selectedFragment;
            if (audioVideoFoldersFragment2 != null) {
                audioVideoFoldersFragment2.checkLayout(this.linearLayout);
            }
            changeLayoutIcon();
            Editor edit = getSharedPreferences("Log", 0).edit();
            boolean z2 = this.linearLayout;
            String str = "foldersLinearLayout";
            if (z2) {
                edit.putBoolean(str, z2);
            } else {
                edit.putBoolean(str, z2);
            }
            edit.commit();
        }
        return true;
    }

    private void changeLayoutIcon() {
        if (!this.linearLayout) {
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_linear_black_24dp));
        } else {
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_grid_black_24dp));
        }
    }

    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        Bundle bundle = new Bundle();
        String str = "ListType";
        if (itemId == R.id.audio_fragment) {
            bundle.putString(str, "audio");
            AudioVideoFoldersFragment audioVideoFoldersFragment2 = new AudioVideoFoldersFragment();
            this.selectedFragment = audioVideoFoldersFragment2;
            audioVideoFoldersFragment2.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, this.selectedFragment).commit();
        } else if (itemId == R.id.video_fragment) {
            bundle.putString(str, "video");
            AudioVideoFoldersFragment audioVideoFoldersFragment3 = new AudioVideoFoldersFragment();
            this.selectedFragment = audioVideoFoldersFragment3;
            audioVideoFoldersFragment3.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, this.selectedFragment).commit();
        } else if (itemId == R.id.other_fragment) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ExtraFragment()).commit();
        }
        return true;
    }
}
