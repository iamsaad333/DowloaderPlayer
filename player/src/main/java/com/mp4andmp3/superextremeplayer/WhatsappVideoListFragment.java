package com.mp4andmp3.superextremeplayer;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import java.io.File;
import java.util.ArrayList;

public class WhatsappVideoListFragment extends Fragment {
    AudioVideoListAdapter audioVideoListAdapter = new AudioVideoListAdapter();
    ArrayList<String> audiosVideosDuration = new ArrayList<>();
    ArrayList<String> audiosVideosTitle = new ArrayList<>();
    ArrayList<String> audiosVideosUri = new ArrayList<>();
    boolean linearLayout = false;
    RecyclerView recyclerView;
    LinearLayout textLayout;

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

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_whatsapp_video_list, viewGroup, false);
        this.textLayout = (LinearLayout) inflate.findViewById(R.id.textLayout);
        RecyclerView recyclerView2 = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        this.recyclerView = recyclerView2;
        recyclerView2.setLayoutManager(new LinearLayoutManager(getActivity()));
        this.audioVideoListAdapter.setListType("video");
        mainFunction();
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.pullToRefresh);
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                WhatsappVideoListFragment.this.audiosVideosUri.clear();
                WhatsappVideoListFragment.this.audiosVideosTitle.clear();
                WhatsappVideoListFragment.this.audiosVideosDuration.clear();
                WhatsappVideoListFragment.this.mainFunction();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        return inflate;
    }

    /* access modifiers changed from: private */
    public void mainFunction() {
        if (getArguments().getString("StatusType").equals("Seen")) {
            this.audioVideoListAdapter.setFolderName("Seen Status of WhatsApp Play Folder");
            fetchWhatsAppVideos("/storage/emulated/0/WhatsApp/Media/.Statuses");
            return;
        }
        this.audioVideoListAdapter.setFolderName("Saved Status of WhatsApp Play Folder");
        fetchWhatsAppVideos("/storage/emulated/0/Play/WhatsApp Status Download");
    }

    public void fetchWhatsAppVideos(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        File[] listFiles = new File(str).listFiles();
        if (listFiles != null) {
            for (File file : listFiles) {
                if (file.getName().endsWith(".mp4")) {
                    this.audiosVideosUri.add(file.getAbsolutePath());
                    mediaMetadataRetriever.setDataSource(file.getAbsolutePath());
                    this.audiosVideosDuration.add(mediaMetadataRetriever.extractMetadata(9));
                    this.audiosVideosTitle.add(file.getName());
                }
            }
        }
        if (this.audiosVideosUri.size() == 0) {
            this.textLayout.setVisibility(View.VISIBLE);
        } else {
            this.textLayout.setVisibility(View.GONE);
        }
        mediaMetadataRetriever.release();
        this.audioVideoListAdapter.setValues(this.audiosVideosUri, this.audiosVideosTitle, this.audiosVideosDuration, getActivity());
        checkLayout();
//        nativeAd();
    }

    private void nativeAd() {
        MobileAds.initialize((Context) getActivity(), "ca-app-pub-1909992210462949/5960469852");
        new Builder((Context) getActivity(), "ca-app-pub-1909992210462949/6614064211").forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                NativeTemplateStyle build = new NativeTemplateStyle.Builder().build();
//                WhatsappVideoListFragment.this.addingAdsItems();
                WhatsappVideoListFragment.this.audioVideoListAdapter.setValues(WhatsappVideoListFragment.this.audiosVideosUri, WhatsappVideoListFragment.this.audiosVideosTitle, WhatsappVideoListFragment.this.audiosVideosDuration, WhatsappVideoListFragment.this.getActivity(), build, unifiedNativeAd);
                WhatsappVideoListFragment.this.recyclerView.setAdapter(WhatsappVideoListFragment.this.audioVideoListAdapter);
            }
        }).withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build().loadAd(new AdRequest.Builder().build());
    }

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

    public void checkLayout() {
        this.audioVideoListAdapter.setLayout(this.linearLayout);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int i) {
                int itemViewType = WhatsappVideoListFragment.this.audioVideoListAdapter.getItemViewType(i);
                return (itemViewType == 1 || itemViewType == 2) ? 2 : 1;
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(this.audioVideoListAdapter);
    }
}
