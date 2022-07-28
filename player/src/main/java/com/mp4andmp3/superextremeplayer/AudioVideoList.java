package com.mp4andmp3.superextremeplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Video;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mp4andmp3.superextremeplayer.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.cast.HlsSegmentFormat;

import java.util.ArrayList;

public class AudioVideoList extends AppCompatActivity {
    AudioVideoListAdapter audioVideoListAdapter = new AudioVideoListAdapter();
    ArrayList<String> audiosVideosDuration = new ArrayList<>();
    ArrayList<String> audiosVideosTitle = new ArrayList<>();
    ArrayList<String> audiosVideosUri = new ArrayList<>();
    ColumnIndexCache cache = new ColumnIndexCache();
    boolean linearLayout;
    private Menu menu;
    RecyclerView recyclerView;
    MediaMetadataRetriever retriever = new MediaMetadataRetriever();

    public class ColumnIndexCache {
        private ArrayMap<String, Integer> mMap = new ArrayMap<>();

        public ColumnIndexCache() {
        }

        public int getColumnIndex(Cursor cursor, String str) {
            if (!this.mMap.containsKey(str)) {
                this.mMap.put(str, Integer.valueOf(cursor.getColumnIndex(str)));
            }
            return ((Integer) this.mMap.get(str)).intValue();
        }

        public void clear() {
            this.mMap.clear();
        }
    }


    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_audio_video_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView2 = (RecyclerView) findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(this));
        String stringExtra = getIntent().getStringExtra("FolderName");
        setTitle(stringExtra);
        this.audioVideoListAdapter.setFolderName(stringExtra);
        this.linearLayout = getSharedPreferences("Log", 0).getBoolean("videosLinearLayout", true);
        String stringExtra2 = getIntent().getStringExtra("ListType");
        if (stringExtra2.equals("video")) {
            this.audioVideoListAdapter.setListType(stringExtra2);
            fetchVideos(stringExtra);
            return;
        }
        fetchAudios(stringExtra);
        this.audioVideoListAdapter.setListType(stringExtra2);
    }

    private void fetchAudios(String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String str2 = "_display_name";
        String str3 = "duration";
        String str4 = "_data";
        String[] strArr = {str4, str3, str2, "_id", str4};
        StringBuilder sb = new StringBuilder();
        String str5 = "%";
        sb.append(str5);
        sb.append(str);
        sb.append(str5);
        Cursor query = getApplicationContext().getContentResolver().query(uri, strArr, "_data like?", new String[]{sb.toString()}, null);
        int columnIndex = this.cache.getColumnIndex(query, str4);
        int columnIndex2 = this.cache.getColumnIndex(query, str2);
        while (query.moveToNext()) {
            String string = query.getString(columnIndex);
            String[] split = string.split("/");
            if (string.endsWith(HlsSegmentFormat.MP3) || (string.endsWith("m4a") && str.equals(split[split.length - 2]))) {
                this.audiosVideosUri.add(query.getString(columnIndex));
                this.audiosVideosTitle.add(query.getString(columnIndex2));
                String string2 = query.getString(this.cache.getColumnIndex(query, str3));
                if (string2 == null) {
                    this.retriever.setDataSource(query.getString(columnIndex));
                    this.audiosVideosDuration.add(this.retriever.extractMetadata(9));
                } else {
                    this.audiosVideosDuration.add(string2);
                }
            }
        }
        this.cache.clear();
        this.retriever.release();
        this.audioVideoListAdapter.setValues(this.audiosVideosUri, this.audiosVideosTitle, this.audiosVideosDuration, this);
        checkLayout();
        nativeAd();
    }

    private void fetchVideos(String str) {
        Uri uri = Video.Media.EXTERNAL_CONTENT_URI;
        String str2 = "duration";
        String str3 = "_display_name";
        String str4 = "_data";
        String[] strArr = {str4, str3, str2};
        StringBuilder sb = new StringBuilder();
        String str5 = "%";
        sb.append(str5);
        sb.append(str);
        sb.append(str5);
        String[] strArr2 = {sb.toString()};
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("datetaken");
        sb2.append(" DESC");
        Cursor query = contentResolver.query(uri, strArr, "_data like?", strArr2, sb2.toString());
        int columnIndex = this.cache.getColumnIndex(query, str4);
        int columnIndex2 = this.cache.getColumnIndex(query, str3);
        while (query.moveToNext()) {
            String[] split = query.getString(columnIndex).split("/");
            if (str.equals(split[split.length - 2])) {
                this.audiosVideosUri.add(query.getString(columnIndex));
                this.audiosVideosTitle.add(query.getString(columnIndex2));
                String string = query.getString(this.cache.getColumnIndex(query, str2));
                if (string != null) {
                    this.audiosVideosDuration.add(string);
                }
            }
        }
        this.cache.clear();
        this.retriever.release();
        this.audioVideoListAdapter.setValues(this.audiosVideosUri, this.audiosVideosTitle, this.audiosVideosDuration, this);
        checkLayout();
        nativeAd();
    }

    private void nativeAd() {
//        MobileAds.initialize((Context) this, "ca-app-pub-7377087786753230/2371906184");
        new Builder((Context) this, "ca-app-pub-7377087786753230/2680502720").forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                NativeTemplateStyle build = new NativeTemplateStyle.Builder().build();
                AudioVideoList.this.addingAdsItems();
                AudioVideoList.this.audioVideoListAdapter.setValues(AudioVideoList.this.audiosVideosUri, AudioVideoList.this.audiosVideosTitle, AudioVideoList.this.audiosVideosDuration, AudioVideoList.this, build, unifiedNativeAd);
                AudioVideoList.this.recyclerView.setAdapter(AudioVideoList.this.audioVideoListAdapter);
            }
        }).withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build().loadAd(new AdRequest.Builder().build());
    }

    /* access modifiers changed from: private */
    public void addingAdsItems() {
        String str = "ca-app-pub-1909992210462949/6614064211";
        if (this.audiosVideosTitle.size() <= 4) {
            this.audiosVideosTitle.add(str);
            this.audiosVideosUri.add(str);
            this.audiosVideosDuration.add(str);
            return;
        }
        this.audiosVideosTitle.add(4, str);
        this.audiosVideosUri.add(4, str);
        this.audiosVideosDuration.add(4, str);
        for (int i = 0; i < this.audiosVideosTitle.size(); i++) {
            if (i == 13 || i == 22 || i == 31 || i == 40 || i == 49) {
                String str2 = "ca-app-pub-1909992210462949/5960469852";
                this.audiosVideosTitle.add(i, str2);
                this.audiosVideosUri.add(i, str2);
                this.audiosVideosDuration.add(i, str2);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu2) {
        getMenuInflater().inflate(R.menu.video_list_toolbar_menu, menu2);
        this.menu = menu2;
        changeLayoutIcon();
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == R.id.folderLayout) {
            checkLayout();
            changeLayoutIcon();
            Editor edit = getSharedPreferences("Log", 0).edit();
            String str = "videosLinearLayout";
            if (this.linearLayout) {
                edit.putBoolean(str, false);
            } else {
                edit.putBoolean(str, true);
            }
            edit.commit();
        } else {

            finish();

        }
        return true;
    }

    private void changeLayoutIcon() {
        if (this.linearLayout) {
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_linear_black_24dp));
        } else {
            this.menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_grid_black_24dp));
        }
    }

    public void checkLayout() {
        this.audioVideoListAdapter.setLayout(this.linearLayout);
        if (this.linearLayout) {
            this.recyclerView.setLayoutManager(new LinearLayoutManager(this));
            this.recyclerView.setAdapter(this.audioVideoListAdapter);
        } else {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
            gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
                public int getSpanSize(int i) {
                    int itemViewType = AudioVideoList.this.audioVideoListAdapter.getItemViewType(i);
                    return (itemViewType == 1 || itemViewType == 2) ? 2 : 1;
                }
            });
            this.recyclerView.setLayoutManager(gridLayoutManager);
            this.recyclerView.setAdapter(this.audioVideoListAdapter);
        }
        this.linearLayout = !this.linearLayout;
    }

}
