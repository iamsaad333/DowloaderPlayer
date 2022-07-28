package com.mp4andmp3.superextremeplayer;

import android.app.PictureInPictureParams;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio;
import android.provider.MediaStore.Images.Media;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Rational;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnSystemUiVisibilityChangeListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.mp4andmp3.superextremeplayer.R;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import java.io.IOException;
import java.util.ArrayList;

public class ViewVideo extends AppCompatActivity {
    String adsLoaded;
    ImageView audioBackImage;
    ImageView audioFrontImage;
    AudioManager audioManager;
    ImageView backArrow;
    FrameLayout bottomFrameLayout;
    SeekBar brightbar;
    private int brightness;
    ImageView cropScreen;
    int current = 0;
    long currentPosition;
    TextView currentTime;
    
    public View decorView;
    int dur = 0;
    boolean framesVisibility = false;
    ImageView fullScreen;
    Long lastSeekUpdateTime = null;
    Long lastVolumeUpdateTime = null;
    TextView leftTime;
    String listType;
    boolean lockScreen = false;
    
    public float mCurBrightness = -1.0f;
    
    public int mCurVolume = -1;
    
    public GestureDetector mDetector;
    
//    public InterstitialAd mInterstitialAd;
    
    public int mMaxVolume;
    RelativeLayout mainLayout;
    AudioService maudioService = null;
    ImageView next;
    ImageView pause;
    ImageView play;
    ImageView popupScreen;
    int position;
    ImageView previous;
    ImageView rotateScreen;
    boolean screenLandscape = true;
    ImageView screenLock;
    TextView screenSizeText;
    ImageView screenUnlock;
    SeekBar seekbar;
    ImageView smallScreen;

    TextView textBrightness;
    TextView textPlayPause;
    TextView textSeekbar;
    TextView textVolume;
    FrameLayout topFrameLayout;
    
    public Handler updateHandler = new Handler();
    
    public Runnable updateVideoTime = new Runnable() {
        public void run() {
            ViewVideo viewVideo = ViewVideo.this;
            viewVideo.dur = viewVideo.videoView.getDuration();
            ViewVideo viewVideo2 = ViewVideo.this;
            viewVideo2.currentPosition = (long) viewVideo2.videoView.getCurrentPosition();
            ViewVideo.this.seekbar.setProgress((int) ViewVideo.this.currentPosition);
            ViewVideo.this.updateHandler.postDelayed(this, 100);
            ViewVideo viewVideo3 = ViewVideo.this;
            viewVideo3.current = viewVideo3.videoView.getCurrentPosition();
            int i = (int) ((((double) ViewVideo.this.current) % 3600000.0d) / 60000.0d);
            int i2 = (int) (((((double) ViewVideo.this.current) % 3600000.0d) % 60000.0d) / 1000.0d);
            String str = "%02d:%02d:%02d";
            String str2 = "%02d:%02d";
            if (((int) (((double) ViewVideo.this.current) / 3600000.0d)) > 0) {
                ViewVideo viewVideo4 = ViewVideo.this;
                viewVideo4.videoCurrentTime = String.format(str, new Object[]{Integer.valueOf((int) (((double) viewVideo4.current) / 3600000.0d)), Integer.valueOf(i), Integer.valueOf(i2)});
            } else {
                ViewVideo.this.videoCurrentTime = String.format(str2, new Object[]{Integer.valueOf(i), Integer.valueOf(i2)});
            }
            int i3 = (ViewVideo.this.dur % 3600000) / 60000;
            int i4 = ((ViewVideo.this.dur % 3600000) % 60000) / 1000;
            if (ViewVideo.this.dur / 3600000 > 0) {
                ViewVideo viewVideo5 = ViewVideo.this;
                viewVideo5.videoTotalTime = String.format(str, new Object[]{Integer.valueOf(viewVideo5.dur / 3600000), Integer.valueOf(i3), Integer.valueOf(i4)});
            } else {
                ViewVideo.this.videoTotalTime = String.format(str2, new Object[]{Integer.valueOf(i3), Integer.valueOf(i4)});
            }
            ViewVideo.this.currentTime.setText(ViewVideo.this.videoCurrentTime);
            ViewVideo.this.textSeekbar.setText(ViewVideo.this.videoCurrentTime);
            ViewVideo.this.leftTime.setText(ViewVideo.this.videoTotalTime);

        }
    };
    String videoCurrentTime;
    int videoPauseTime;
    boolean videoPlayed;
    ArrayList<String> videoTitle;
    TextView videoTitleText;
    String videoTotalTime;
    ArrayList<String> videoUri;
    VideoView videoView;
    SeekBar volumebar;

    public static class BlurBuilder {
        private static final float BITMAP_SCALE = 0.1f;
        private static final float BLUR_RADIUS = 9.0f;

        public static Bitmap blur(Context context, Bitmap bitmap) {
            Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(((float) bitmap.getWidth()) * BITMAP_SCALE), Math.round(((float) bitmap.getHeight()) * BITMAP_SCALE), false);
            Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap);
            RenderScript create = RenderScript.create(context);
            ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
            Allocation createFromBitmap = Allocation.createFromBitmap(create, createScaledBitmap);
            Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
            create2.setRadius(BLUR_RADIUS);
            create2.setInput(createFromBitmap);
            create2.forEach(createFromBitmap2);
            createFromBitmap2.copyTo(createBitmap);
            return createBitmap;
        }
    }

    class MyGestureListener extends SimpleOnGestureListener {
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        MyGestureListener() {
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            if (ViewVideo.this.lockScreen) {
                ViewVideo.this.screenUnlock.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.screenUnlock.setVisibility(View.GONE);
                    }
                }, 4000);
            } else if (ViewVideo.this.framesVisibility) {
                ViewVideo.this.topFrameLayout.setVisibility(View.GONE);
                ViewVideo.this.bottomFrameLayout.setVisibility(View.GONE);
                ViewVideo.this.rotateScreen.setVisibility(View.GONE);
                ViewVideo.this.framesVisibility = false;
            } else {
                ViewVideo.this.topFrameLayout.setVisibility(View.VISIBLE);
                ViewVideo.this.bottomFrameLayout.setVisibility(View.VISIBLE);
                if (!ViewVideo.this.listType.equals("audio")) {
                    ViewVideo.this.rotateScreen.setVisibility(View.VISIBLE);
                }
                ViewVideo.this.framesVisibility = true;
            }
            return true;
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            if (ViewVideo.this.videoView.isPlaying()) {
                ViewVideo.this.videoView.pause();
//                ViewVideo.this.nativeAd();
                ViewVideo.this.pause.setVisibility(View.GONE);
                ViewVideo.this.play.setVisibility(View.VISIBLE);
                ViewVideo.this.topFrameLayout.setVisibility(View.VISIBLE);
                ViewVideo.this.bottomFrameLayout.setVisibility(View.VISIBLE);
                if (!ViewVideo.this.listType.equals("audio")) {
                    ViewVideo.this.rotateScreen.setVisibility(View.VISIBLE);
                }
                ViewVideo.this.framesVisibility = true;
                ViewVideo.this.textPlayPause.setText("PAUSE");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.textPlayPause.setText("");
                    }
                }, 1000);
            } else {
                ViewVideo.this.videoView.start();
                ViewVideo.this.play.setVisibility(View.GONE);
                ViewVideo.this.pause.setVisibility(View.VISIBLE);
                ViewVideo.this.textPlayPause.setText("PLAY");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.textPlayPause.setText("");
                    }
                }, 1000);
                if (ViewVideo.this.videoView.isPlaying()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ViewVideo.this.topFrameLayout.setVisibility(View.GONE);
                            ViewVideo.this.bottomFrameLayout.setVisibility(View.GONE);
                            ViewVideo.this.rotateScreen.setVisibility(View.GONE);
                            ViewVideo.this.framesVisibility = false;
                        }
                    }, 1000);
                }
            }
            return true;
        }

        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float rawX = motionEvent.getRawX() - motionEvent2.getRawX();
            float rawY = motionEvent.getRawY() - motionEvent2.getRawY();
            Long valueOf = Long.valueOf(System.currentTimeMillis());
            ViewVideo.this.setGestureListener();
            if (Math.abs(rawX) > Math.abs(rawY)) {
                if (Math.abs(rawX) > 20.0f && valueOf.longValue() >= ViewVideo.this.lastVolumeUpdateTime.longValue() + 1000) {
                    ViewVideo.this.lastSeekUpdateTime = valueOf;
                    ViewVideo.this.onHorizontalScroll(rawX < 0.0f);
                }
            } else if (Math.abs(rawY) > 60.0f && valueOf.longValue() >= ViewVideo.this.lastSeekUpdateTime.longValue() + 1000) {
                if (((double) motionEvent.getX()) < ((double) ViewVideo.getDeviceWidth(ViewVideo.this.getApplicationContext())) * 0.5d) {
                    ViewVideo.this.lastVolumeUpdateTime = valueOf;
                    ViewVideo viewVideo = ViewVideo.this;
                    viewVideo.onVerticalScroll(rawY / ((float) ViewVideo.getDeviceHeight(viewVideo.getApplicationContext())), 1);
                } else if (((double) motionEvent.getX()) > ((double) ViewVideo.getDeviceWidth(ViewVideo.this.getApplicationContext())) * 0.5d) {
                    ViewVideo.this.lastVolumeUpdateTime = valueOf;
                    ViewVideo viewVideo2 = ViewVideo.this;
                    viewVideo2.onVerticalScroll(rawY / ((float) ViewVideo.getDeviceHeight(viewVideo2.getApplicationContext())), 2);
                }
            }
            return true;
        }
    }

    
    public int hideSystemBars() {
        return 5894;
    }

    
    public void onDestroy() {
        super.onDestroy();
        if (this.listType.equals("audio")) {
            stopService(new Intent(this, AudioService.class));
        }
    }

    
    public void onPause() {
        super.onPause();
        this.videoPauseTime = this.videoView.getCurrentPosition();
        if (this.videoView.isPlaying()) {
            this.videoView.pause();
            this.videoPlayed = true;
        }
        if (this.listType.equals("audio")) {
            Intent intent = new Intent(this, AudioService.class);
            intent.putExtra("audioPath", (String) this.videoUri.get(this.position));
            intent.putExtra("audioTime", this.videoPauseTime);
            startService(intent);
        }
    }

    
    public void onResume() {
        super.onResume();
        if (this.videoPlayed) {
            this.videoView.seekTo(this.videoPauseTime);
            this.pause.setVisibility(View.GONE);
            this.play.setVisibility(View.VISIBLE);
        } else {
            this.videoView.seekTo(this.videoPauseTime);
        }
        if (this.listType.equals("audio")) {
            stopService(new Intent(this, AudioService.class));
        }
    }

    public void onBackPressed() {
//        if (this.mInterstitialAd.isLoaded()) {
//            this.mInterstitialAd.show();
//        }
        super.onBackPressed();
    }

    
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_view_video);
//        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        interstitialAd();
//        new Handler().postDelayed(new Runnable() {
//            public void run() {
//                ViewVideo.this.mInterstitialAd.loadAd(new Builder().build());
//            }
//        }, 30000);

        this.videoTitleText = (TextView) findViewById(R.id.videoTitle);
        this.backArrow = (ImageView) findViewById(R.id.backArrow);
        this.videoView = (VideoView) findViewById(R.id.videoView);
        this.screenLock = (ImageView) findViewById(R.id.screenLock);
        this.screenUnlock = (ImageView) findViewById(R.id.screenUnlock);
        this.previous = (ImageView) findViewById(R.id.previous);
        this.next = (ImageView) findViewById(R.id.next);
        this.play = (ImageView) findViewById(R.id.play);
        this.audioBackImage = (ImageView) findViewById(R.id.audioBackImage);
        this.audioFrontImage = (ImageView) findViewById(R.id.audioFrontImage);
        this.textSeekbar = (TextView) findViewById(R.id.textseekbar);
        this.textPlayPause = (TextView) findViewById(R.id.textplay);
        this.pause = (ImageView) findViewById(R.id.pause);
        this.textBrightness = (TextView) findViewById(R.id.textbrightness);
        this.textVolume = (TextView) findViewById(R.id.textvolume);
        this.brightbar = (SeekBar) findViewById(R.id.brightness_seekbar);
        this.volumebar = (SeekBar) findViewById(R.id.volume_seekbar);
        this.popupScreen = (ImageView) findViewById(R.id.popupScreen);
        this.screenSizeText = (TextView) findViewById(R.id.screenSizeText);
        this.currentTime = (TextView) findViewById(R.id.startTime);
        this.leftTime = (TextView) findViewById(R.id.endTime);
        this.fullScreen = (ImageView) findViewById(R.id.fullScreen);
        this.smallScreen = (ImageView) findViewById(R.id.smallScreen);
        this.cropScreen = (ImageView) findViewById(R.id.cropScreen);
        this.rotateScreen = (ImageView) findViewById(R.id.rotateScreen);
        this.topFrameLayout = (FrameLayout) findViewById(R.id.topFrameLayout);
        this.bottomFrameLayout = (FrameLayout) findViewById(R.id.bottomFrameLayout);
        this.mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        this.videoUri = getIntent().getStringArrayListExtra("VideoUri");
        this.videoTitle = getIntent().getStringArrayListExtra("VideoTitle");
        this.position = getIntent().getIntExtra("VideoPosition", 0);
        this.adsLoaded = getIntent().getStringExtra("AdsLoaded");
        this.listType = getIntent().getStringExtra("ListType");
        this.videoTitleText.setText((CharSequence) this.videoTitle.get(this.position));
        this.videoView.setVideoPath((String) this.videoUri.get(this.position));
        this.videoView.start();
        this.audioFrontImage.setVisibility(View.GONE);
        this.audioBackImage.setVisibility(View.GONE);
        this.topFrameLayout.setVisibility(View.GONE);
        this.bottomFrameLayout.setVisibility(View.GONE);
        this.play.setVisibility(View.GONE);
        this.cropScreen.setVisibility(View.GONE);
        this.smallScreen.setVisibility(View.GONE);
        this.rotateScreen.setVisibility(View.GONE);
        this.screenUnlock.setVisibility(View.GONE);
        this.brightbar.setVisibility(View.GONE);
        this.volumebar.setVisibility(View.GONE);
        this.textSeekbar.setVisibility(View.GONE);
        this.textVolume.setVisibility(View.GONE);
        this.textBrightness.setVisibility(View.GONE);
        String str = "audio";
        this.audioManager = (AudioManager) getSystemService(str);
        this.seekbar = (SeekBar) findViewById(R.id.video_seekbar);
        this.mDetector = new GestureDetector(this, new MyGestureListener());
        this.lastSeekUpdateTime = Long.valueOf(System.currentTimeMillis());
        this.lastVolumeUpdateTime = Long.valueOf(System.currentTimeMillis());
        if (this.listType.equals(str)) {
            this.rotateScreen.setVisibility(View.GONE);
            setRequestedOrientation(1);
            this.audioFrontImage.setVisibility(View.VISIBLE);
            this.audioBackImage.setVisibility(View.VISIBLE);
            settingAudioImages(this.position);
        }
        this.brightbar.setMax(100);
        this.volumebar.setMax(this.audioManager.getStreamMaxVolume(3));
        this.volumebar.setProgress(this.audioManager.getStreamVolume(3));
        this.volumebar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                ViewVideo.this.audioManager.setStreamVolume(3, i, 0);
                if (i > 0) {
                    i = (i * 100) / ViewVideo.this.mMaxVolume;
                }
                ViewVideo.this.textVolume.setText(String.valueOf(i));
            }
        });
        this.volumebar.setKeyProgressIncrement(1);
        this.brightbar.setKeyProgressIncrement(1);
        try {
            this.brightness = Settings.System.getInt(getContentResolver(), "screen_brightness");
        } catch (SettingNotFoundException e) {
            Log.e("Error", "Cannot access system brightness");
            e.printStackTrace();
        }
        this.videoView.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                ViewVideo.this.seekbar.setProgress(0);
                ViewVideo.this.seekbar.setMax(ViewVideo.this.videoView.getDuration());
                ViewVideo.this.updateHandler.postDelayed(ViewVideo.this.updateVideoTime, 100);
            }
        });
        this.seekbar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            public void onProgressChanged(SeekBar seekBar, int i, boolean z) {
                if (z) {
                    ViewVideo.this.videoView.seekTo(i);
                }
            }
        });
        this.topFrameLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.bottomFrameLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.previous.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.position--;
                if (ViewVideo.this.position < 0) {
                    ViewVideo viewVideo = ViewVideo.this;
                    viewVideo.position = viewVideo.videoUri.size() - 1;
                }
                ViewVideo.this.positionAdjustment("minus");
                ViewVideo.this.videoTitleText.setText((CharSequence) ViewVideo.this.videoTitle.get(ViewVideo.this.position));
                ViewVideo.this.videoView.setVideoURI(Uri.parse((String) ViewVideo.this.videoUri.get(ViewVideo.this.position)));
                if (!ViewVideo.this.videoView.isPlaying()) {
                    ViewVideo viewVideo2 = ViewVideo.this;
                    viewVideo2.settingAudioImages(viewVideo2.position);
                    ViewVideo.this.videoView.start();
                    ViewVideo.this.pause.setVisibility(View.VISIBLE);
                    ViewVideo.this.play.setVisibility(View.GONE);
                }
            }
        });
        this.next.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.position++;
                ViewVideo.this.positionAdjustment("plus");
                if (ViewVideo.this.position > ViewVideo.this.videoUri.size() - 1) {
                    ViewVideo.this.position = 0;
                }
                ViewVideo.this.videoTitleText.setText((CharSequence) ViewVideo.this.videoTitle.get(ViewVideo.this.position));
                ViewVideo.this.videoView.setVideoURI(Uri.parse((String) ViewVideo.this.videoUri.get(ViewVideo.this.position)));
                if (!ViewVideo.this.videoView.isPlaying()) {
                    ViewVideo viewVideo = ViewVideo.this;
                    viewVideo.settingAudioImages(viewVideo.position);
                    ViewVideo.this.videoView.start();
                    ViewVideo.this.pause.setVisibility(View.VISIBLE);
                    ViewVideo.this.play.setVisibility(View.GONE);
                }
            }
        });
        this.pause.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.videoView.pause();
                ViewVideo.this.pause.setVisibility(View.GONE);
                ViewVideo.this.play.setVisibility(View.VISIBLE);
                ViewVideo.this.topFrameLayout.setVisibility(View.VISIBLE);
                ViewVideo.this.bottomFrameLayout.setVisibility(View.VISIBLE);
                if (!ViewVideo.this.listType.equals("audio")) {
                    ViewVideo.this.rotateScreen.setVisibility(View.VISIBLE);
                }
                ViewVideo.this.framesVisibility = true;
//                ViewVideo.this.nativeAd();
            }
        });
        this.play.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.videoView.start();
                ViewVideo.this.play.setVisibility(View.GONE);
                ViewVideo.this.pause.setVisibility(View.VISIBLE);
                if (ViewVideo.this.videoView.isPlaying()) {
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ViewVideo.this.topFrameLayout.setVisibility(View.GONE);
                            ViewVideo.this.bottomFrameLayout.setVisibility(View.GONE);
                            ViewVideo.this.rotateScreen.setVisibility(View.GONE);
                            ViewVideo.this.framesVisibility = false;
                        }
                    }, 1000);
                }
            }
        });
        this.rotateScreen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (!ViewVideo.this.screenLandscape) {
                    ViewVideo.this.setRequestedOrientation(0);
                    ViewVideo.this.screenLandscape = true;
                    return;
                }
                ViewVideo.this.screenLandscape = false;
                ViewVideo.this.setRequestedOrientation(1);
            }
        });
        this.screenLock.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.lockScreen = true;
                ViewVideo.this.topFrameLayout.setVisibility(View.GONE);
                ViewVideo.this.bottomFrameLayout.setVisibility(View.GONE);
                ViewVideo.this.rotateScreen.setVisibility(View.GONE);
                ViewVideo.this.screenUnlock.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.screenUnlock.setVisibility(View.GONE);
                    }
                }, 4000);
            }
        });
        this.screenUnlock.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.lockScreen = false;
                ViewVideo.this.topFrameLayout.setVisibility(View.VISIBLE);
                ViewVideo.this.bottomFrameLayout.setVisibility(View.VISIBLE);
                ViewVideo.this.rotateScreen.setVisibility(View.VISIBLE);
                ViewVideo.this.screenUnlock.setVisibility(View.GONE);
            }
        });
        this.fullScreen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.fullScreen.setVisibility(View.GONE);
                ViewVideo.this.cropScreen.setVisibility(View.VISIBLE);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ViewVideo.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                LayoutParams layoutParams = (LayoutParams) ViewVideo.this.videoView.getLayoutParams();
                layoutParams.width = displayMetrics.widthPixels;
                layoutParams.height = displayMetrics.heightPixels;
                layoutParams.leftMargin = 0;
                layoutParams.rightMargin = 0;
                layoutParams.topMargin = 0;
                layoutParams.bottomMargin = 0;
                ViewVideo.this.videoView.setLayoutParams(layoutParams);
                ViewVideo.this.screenSizeText.setText("FIT TO SCREEN");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.screenSizeText.setText("");
                    }
                }, 4000);
            }
        });
        this.cropScreen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.cropScreen.setVisibility(View.GONE);
                ViewVideo.this.smallScreen.setVisibility(View.VISIBLE);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ViewVideo.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                LayoutParams layoutParams = (LayoutParams) ViewVideo.this.videoView.getLayoutParams();
                layoutParams.width = (int) (displayMetrics.density * 400.0f);
                layoutParams.height = (int) (displayMetrics.density * 300.0f);
                layoutParams.leftMargin = 150;
                layoutParams.rightMargin = 150;
                layoutParams.topMargin = 150;
                layoutParams.bottomMargin = 150;
                ViewVideo.this.videoView.setLayoutParams(layoutParams);
                ViewVideo.this.screenSizeText.setText("CROP");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.screenSizeText.setText("");
                    }
                }, 4000);
            }
        });
        this.smallScreen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ViewVideo.this.smallScreen.setVisibility(View.GONE);
                ViewVideo.this.fullScreen.setVisibility(View.VISIBLE);
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ViewVideo.this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                LayoutParams layoutParams = (LayoutParams) ViewVideo.this.videoView.getLayoutParams();
                layoutParams.width = (int) (displayMetrics.density * 1000.0f);
                layoutParams.leftMargin = 150;
                layoutParams.rightMargin = 150;
                layoutParams.topMargin = 0;
                layoutParams.bottomMargin = 0;
                ViewVideo.this.videoView.setLayoutParams(layoutParams);
                ViewVideo.this.screenSizeText.setText("100%");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.screenSizeText.setText("");
                    }
                }, 4000);
            }
        });
        this.popupScreen.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (VERSION.SDK_INT >= 26) {
                    PictureInPictureParams.Builder builder = new PictureInPictureParams.Builder();
                    builder.setAspectRatio(new Rational(ViewVideo.this.videoView.getWidth(), ViewVideo.this.videoView.getHeight())).build();
                    ViewVideo.this.enterPictureInPictureMode(builder.build());
                    return;
                }
                Toast.makeText(ViewVideo.this.getApplicationContext(), "Your phone not support this feature", 1).show();
            }
        });
        this.backArrow.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
//                if (ViewVideo.this.mInterstitialAd.isLoaded()) {
//                    ViewVideo.this.mInterstitialAd.show();
//                }
                ViewVideo.this.finish();
            }
        });
        View decorView2 = getWindow().getDecorView();
        this.decorView = decorView2;
        decorView2.setOnSystemUiVisibilityChangeListener(new OnSystemUiVisibilityChangeListener() {
            public void onSystemUiVisibilityChange(int i) {
                ViewVideo.this.decorView.setSystemUiVisibility(ViewVideo.this.hideSystemBars());
            }
        });
        this.mainLayout.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == 1) {
                    ViewVideo.this.mCurVolume = -1;
                    ViewVideo.this.mCurBrightness = -1.0f;
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            ViewVideo.this.volumebar.setVisibility(View.GONE);
                            ViewVideo.this.textVolume.setVisibility(View.GONE);
                            ViewVideo.this.brightbar.setVisibility(View.GONE);
                            ViewVideo.this.textBrightness.setVisibility(View.GONE);
                        }
                    }, 500);
                }
                return ViewVideo.this.mDetector.onTouchEvent(motionEvent);
            }
        });
    }

    private void callBitmapBlurFunction(Bitmap bitmap, Uri uri) {
        ((RequestBuilder) Glide.with((FragmentActivity) this).load(Integer.valueOf(getResources().getIdentifier("cover", "drawable", getPackageName()))).centerInside()).into(this.audioFrontImage);
        try {
            StringBuilder sb = new StringBuilder();
            sb.append("android.resource://");
            sb.append(getPackageName());
            sb.append("/drawable/");
            sb.append(R.drawable.cover);
            bitmap = Media.getBitmap(getContentResolver(), Uri.parse(sb.toString()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.audioBackImage.setBackground(new BitmapDrawable(getResources(), BlurBuilder.blur(this, bitmap)));
    }

    
    public void settingAudioImages(int i) {
        Uri audioAlbumImageContentUri = getAudioAlbumImageContentUri(this, (String) this.videoUri.get(i));
        Bitmap bitmap = null;
        if (audioAlbumImageContentUri == null) {
            callBitmapBlurFunction(null, audioAlbumImageContentUri);
            return;
        }
        ((RequestBuilder) Glide.with((FragmentActivity) this).asBitmap().load(audioAlbumImageContentUri).centerCrop()).into(this.audioFrontImage);
        try {
            bitmap = Media.getBitmap(getContentResolver(), audioAlbumImageContentUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap == null) {
            callBitmapBlurFunction(bitmap, audioAlbumImageContentUri);
            return;
        }
        this.audioBackImage.setBackground(new BitmapDrawable(getResources(), BlurBuilder.blur(this, bitmap)));
    }

    public void setGestureListener() {
        this.mMaxVolume = this.audioManager.getStreamMaxVolume(3);
    }

    public static int getDeviceWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getDeviceHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService("window");
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public void onVerticalScroll(float f, int i) {
        if (i == 1) {
            changeBrightness(f * 2.0f);
        } else {
            changeVolume(f * 2.0f);
        }
    }

    public void onHorizontalScroll(boolean z) {
        this.textSeekbar.setVisibility(View.VISIBLE);
        if ((z && this.videoView.canSeekForward()) || (!z && this.videoView.canSeekBackward())) {
            if (this.bottomFrameLayout.getVisibility() == 8 && !this.lockScreen) {
                this.bottomFrameLayout.setVisibility(View.VISIBLE);
                this.topFrameLayout.setVisibility(View.VISIBLE);
            }
            String str = "ViewGestureListener";
            if (z) {
                Log.i(str, "Forwarding");
                this.currentPosition = (long) this.videoView.getCurrentPosition();
                long currentPosition2 = (long) (this.videoView.getCurrentPosition() + 700);
                this.currentPosition = currentPosition2;
                this.videoView.seekTo((int) currentPosition2);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        ViewVideo.this.textSeekbar.setVisibility(View.GONE);
                        ViewVideo.this.bottomFrameLayout.setVisibility(View.GONE);
                        ViewVideo.this.topFrameLayout.setVisibility(View.GONE);
                        ViewVideo.this.rotateScreen.setVisibility(View.GONE);
                        ViewVideo.this.framesVisibility = false;
                    }
                }, 1200);
                return;
            }
            Log.i(str, "Rewinding");
            this.currentPosition = (long) this.videoView.getCurrentPosition();
            long currentPosition3 = (long) (this.videoView.getCurrentPosition() - 700);
            this.currentPosition = currentPosition3;
            this.videoView.seekTo((int) currentPosition3);
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    ViewVideo.this.textSeekbar.setVisibility(View.GONE);
                    ViewVideo.this.bottomFrameLayout.setVisibility(View.GONE);
                    ViewVideo.this.topFrameLayout.setVisibility(View.GONE);
                    ViewVideo.this.rotateScreen.setVisibility(View.GONE);
                    ViewVideo.this.framesVisibility = false;
                }
            }, 1200);
        }
    }

    private void changeBrightness(float f) {
        this.brightbar.setVisibility(View.VISIBLE);
        this.textBrightness.setVisibility(View.VISIBLE);
        if (this.mCurBrightness == -1.0f) {
            float f2 = getWindow().getAttributes().screenBrightness;
            this.mCurBrightness = f2;
            if (f2 <= 0.01f) {
                this.mCurBrightness = 0.01f;
            }
        }
        this.textBrightness.setVisibility(View.VISIBLE);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.screenBrightness = this.mCurBrightness + f;
        if (attributes.screenBrightness >= 1.0f) {
            attributes.screenBrightness = 1.0f;
        } else if (attributes.screenBrightness <= 0.01f) {
            attributes.screenBrightness = 0.01f;
        }
        getWindow().setAttributes(attributes);
        int i = (int) (attributes.screenBrightness * 100.0f);
        this.brightbar.setProgress(i);
        this.textBrightness.setText(String.valueOf(i));
    }

    private void changeVolume(float f) {
        int i = 0;
        this.volumebar.setVisibility(View.VISIBLE);
        this.textVolume.setVisibility(View.VISIBLE);
        this.volumebar.setVisibility(View.VISIBLE);
        this.textVolume.setVisibility(View.VISIBLE);
        if (this.mCurVolume == -1) {
            int streamVolume = this.audioManager.getStreamVolume(3);
            this.mCurVolume = streamVolume;
            if (((float) streamVolume) < 0.01f) {
                this.mCurVolume = 0;
            }
        }
        int i2 = this.mMaxVolume;
        int i3 = ((int) (((float) i2) * f)) + this.mCurVolume;
        if (i3 <= i2) {
            i2 = i3;
        }
        if (((float) i2) >= 0.01f) {
            i = i2;
        }
        this.volumebar.setProgress(i);
    }

    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        if (z) {
            this.decorView.setSystemUiVisibility(hideSystemBars());
        }
    }

//    private void interstitialAd() {
//        InterstitialAd interstitialAd = new InterstitialAd(this);
//        this.mInterstitialAd = interstitialAd;
//        interstitialAd.setAdUnitId("ca-app-pub-9194328510903334/8430231855");
//    }

    


    
    public void positionAdjustment(String str) {
        if (this.adsLoaded.equals("Yes")) {
            String str2 = "minus";
            if (this.videoTitle.size() > 5) {
                int i = this.position;
                if (i != 4 && i != 13 && i != 22 && i != 31 && i != 40 && i != 49) {
                    return;
                }
                if (str.equals(str2)) {
                    this.position--;
                } else {
                    this.position++;
                }
            } else if (this.position != this.videoTitle.size() - 1) {
            } else {
                if (str.equals(str2)) {
                    this.position--;
                } else {
                    this.position++;
                }
            }
        }
    }

    public Uri getAudioAlbumImageContentUri(Context context, String str) {
        Uri uri = Audio.Media.EXTERNAL_CONTENT_URI;
        String str2 = "album_id";
        String[] strArr = {str2};
        StringBuilder sb = new StringBuilder();
        sb.append("is_music=1 AND _data = '");
        sb.append(str);
        sb.append("'");
        Cursor query = context.getApplicationContext().getContentResolver().query(uri, strArr, sb.toString(), null, null);
        if (query == null || query.getCount() <= 0) {
            return null;
        }
        query.moveToFirst();
        Uri withAppendedId = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.valueOf(query.getLong(query.getColumnIndexOrThrow(str2))).longValue());
        query.close();
        return withAppendedId;
    }
}
