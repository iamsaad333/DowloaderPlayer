

package com.smartsocial.hdvideodownloader.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.smartsocial.hdvideodownloader.R;
import com.smartsocial.hdvideodownloader.VDApp;
import com.smartsocial.hdvideodownloader.model.WebConnect;
import com.smartsocial.hdvideodownloader.download.frag.DownloadsC;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.smartsocial.hdvideodownloader.browser.BrowserManager;
import com.smartsocial.hdvideodownloader.download.frag.Downloads;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final String DOWNLOAD = "Downloads";
    private static final String HISTORY = "History";
    private static final String SETTING = "Settings";
    private EditText searchTextBar;
    private ImageView btnSearchCancel;
    private BrowserManager browserManager;
    private Uri appLinkData;
    private FragmentManager manager;
    private BottomNavigationView navView;
    private AdView adView;
    private InterstitialAd mInterstitialAd;
    private Boolean isInterShowing = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }


        // ATTENTION: This was auto-generated to handle app links.
        Intent appLinkIntent = getIntent();
        appLinkData = appLinkIntent.getData();

        manager = this.getSupportFragmentManager();
        // This is for creating browser manager fragment
        if ((browserManager = (BrowserManager) this.getSupportFragmentManager().findFragmentByTag("BM")) == null) {
            browserManager = new BrowserManager(MainActivity.this);

            manager.beginTransaction()
                    .add(browserManager, "BM").commit();

        }

        // Bottom navigation
        navView =(BottomNavigationView) findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        ImageView instagram = findViewById(R.id.instagramButton);
        ImageView facebook = findViewById(R.id.facebookButton);
        ImageView twitter = findViewById(R.id.twitterButton);
//        ImageView reddit = findViewById(R.id.reddit_btn);
//        ImageView tumblr = findViewById(R.id.tumblr_btn);
//        ImageView tiktok = findViewById(R.id.tiktok_btn);
        ImageView vimeo = findViewById(R.id.vimeoButton);
        ImageView dailymotion = findViewById(R.id.dailymotionButton);


        vimeo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserManager.newWindow("https://m.vlive.tv");
            }
        });

        dailymotion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserManager.newWindow("https://www.dailymotion.com");
            }
        });

        instagram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserManager.newWindow("https://www.instagram.com/");
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserManager.newWindow("https://www.facebook.com/");
            }
        });

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                browserManager.newWindow("https://www.twitter.com/");
            }
        });

//        reddit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                browserManager.newWindow("https://www.reddit.com/");
//            }
//        });
//
//        tumblr.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                browserManager.newWindow("https://www.tumblr.com/");
//            }
//        });
//
//        tiktok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                browserManager.newWindow("https://www.tiktok.com/");
//            }
//        });

//        Button guide = findViewById(R.id.start_guide);
//        guide.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(MainActivity.this, GuideActivity.class);
//                startActivity(intent);
//            }
//        });

        setUPBrowserToolbarView();
        MobileAds.initialize(this);
        loadBanner();
        loadInter();
    }

    public void loadInter() {
        if (!isInterShowing) {
            AdRequest adRequest = new AdRequest.Builder().build();
            isInterShowing = true;
            InterstitialAd.load(this, getString(R.string.intersititial), adRequest, new InterstitialAdLoadCallback() {
                @Override
                public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd;
                    mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                                                                     @Override
                                                                     public void onAdDismissedFullScreenContent() {
                                                                         // Called when fullscreen content is dismissed.
                                                                         Log.d("TAG", "The ad was dismissed.");
                                                                         isInterShowing = false;
                                                                     }

                                                                     @Override
                                                                     public void onAdFailedToShowFullScreenContent(AdError adError) {
                                                                         // Called when fullscreen content failed to show.
                                                                         Log.d("TAG", "The ad failed to show.");
                                                                         isInterShowing = false;
                                                                     }

                                                                     @Override
                                                                     public void onAdShowedFullScreenContent() {
                                                                         // Called when fullscreen content is shown.
                                                                         // Make sure to set your reference to null so you don't
                                                                         // show it a second time.
                                                                         mInterstitialAd = null;
                                                                         Log.d("TAG", "The ad was shown.");
                                                                     }
                                                                 }
                    );
                    mInterstitialAd.show(MainActivity.this);
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    // Handle the error
                    mInterstitialAd = null;
                    isInterShowing = false;
                }
            });
        }

    }


    private void loadBanner() {
        adView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder().build();
        adView.loadAd(request);
    }

    private void setUPBrowserToolbarView() {

        // Toolbar search
        btnSearchCancel = findViewById(R.id.btn_search_cancel);
        btnSearchCancel.setOnClickListener(this);
        ImageView btnSearch = findViewById(R.id.btn_search);
        searchTextBar = findViewById(R.id.et_search_bar);

        /*hide/show clear button in search view*/
        TextWatcher searchViewTextWatcher = new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == 0) {
                    btnSearchCancel.setVisibility(View.GONE);
                } else {
                    btnSearchCancel.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //nada
            }

            @Override
            public void afterTextChanged(Editable s) {
                //nada
            }
        };
        searchTextBar.addTextChangedListener(searchViewTextWatcher);
        searchTextBar.setOnEditorActionListener(this);
        btnSearch.setOnClickListener(this);

        //Toolbar home button
        ImageView toolbarHome = findViewById(R.id.btn_home);
        toolbarHome.setOnClickListener(this);

        //Settings
        ImageView settings = findViewById(R.id.btn_settings);
        settings.setOnClickListener(this);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_home) {
                homeClicked();
                return true;
            } else if (itemId == R.id.navigation_downloads) {
                downloadClicked();
                return true;
            } else if (itemId == R.id.navigation_history) {
                loadInter();
                historyClicked();
                return true;
            }
            return false;
        }
    };

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_search_cancel) {
            searchTextBar.getText().clear();
        } else if (id == R.id.btn_home) {
            goBack();
        } else if (id == R.id.btn_settings) {
            settingsClicked();
        } else if (id == R.id.btn_search) {
            new WebConnect(searchTextBar, this).connect();
        }
    }

    @Override
    public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_GO) {
            new WebConnect(searchTextBar, this).connect();
        }
        return handled;
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    private void goBack() {
        if (manager.findFragmentByTag(DOWNLOAD) != null ||
                manager.findFragmentByTag(HISTORY) != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            browserManager.resumeCurrentWindow();
            navView.setSelectedItemId(R.id.navigation_home);
            loadInter();
        } else if (manager.findFragmentByTag(SETTING) != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            browserManager.resumeCurrentWindow();
            navView.setVisibility(View.VISIBLE);
            loadInter();
        } else if (VDApp.getInstance().getOnBackPressedListener() != null) {
            VDApp.getInstance().getOnBackPressedListener().onBackpressed();
            loadInter();
        } else {
            MainActivity.super.onBackPressed();
        }
    }

    public BrowserManager getBrowserManager() {
        return browserManager;
    }

    public interface OnBackPressedListener {
        void onBackpressed();
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        VDApp.getInstance().setOnBackPressedListener(onBackPressedListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (appLinkData != null) {
            browserManager.newWindow(appLinkData.toString());
        }
    }

    public void browserClicked() {
        browserManager.unhideCurrentWindow();
    }

    public void downloadClicked() {
        closeHistory();
        if (manager.findFragmentByTag(DOWNLOAD) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.main_content, new DownloadsC(), DOWNLOAD).commit();
        }
    }


    public void historyClicked() {
        closeDownloads();
        if (manager.findFragmentByTag(HISTORY) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            manager.beginTransaction().add(R.id.main_content, new Downloads(MainActivity.this), HISTORY).commit();
        }
    }

    private void settingsClicked() {
        if (manager.findFragmentByTag(SETTING) == null) {
            browserManager.hideCurrentWindow();
            browserManager.pauseCurrentWindow();
            navView.setVisibility(View.GONE);
            manager.beginTransaction().add(R.id.main_content, new Settings(), SETTING).commit();
        }
    }

    public void homeClicked() {
        browserManager.unhideCurrentWindow();
        browserManager.resumeCurrentWindow();
        closeDownloads();
        closeHistory();
    }

    private void closeDownloads() {
        Fragment fragment = manager.findFragmentByTag(DOWNLOAD);
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    private void closeHistory() {
        Fragment fragment = manager.findFragmentByTag(HISTORY);
        if (fragment != null) {
            manager.beginTransaction().remove(fragment).commit();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        onRequestPermissionsResultCallback.onRequestPermissionsResult(requestCode, permissions,
                grantResults);
    }

    private ActivityCompat.OnRequestPermissionsResultCallback onRequestPermissionsResultCallback;

    public void setOnRequestPermissionsResultListener(ActivityCompat
                                                              .OnRequestPermissionsResultCallback
                                                              onRequestPermissionsResultCallback) {
        this.onRequestPermissionsResultCallback = onRequestPermissionsResultCallback;
    }

}
