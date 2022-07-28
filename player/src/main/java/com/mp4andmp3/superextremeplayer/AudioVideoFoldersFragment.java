package com.mp4andmp3.superextremeplayer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore.Audio.Media;
import android.provider.MediaStore.Video;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;

public class AudioVideoFoldersFragment extends Fragment {
    AudioVideoFoldersAdapter audioVideoFoldersAdapter = new AudioVideoFoldersAdapter();
    ColumnIndexCache cache = new ColumnIndexCache();
    ArrayList<String> folderName = new ArrayList<>();
    private boolean linearLayout;
    RecyclerView recyclerView;
    ArrayList<Integer> totalAudiosVideos = new ArrayList<>();

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
        View inflate = layoutInflater.inflate(R.layout.fragment_audio_video_folders, viewGroup, false);
        String string = getArguments().getString("ListType");
        this.recyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerView);
        boolean z = getActivity().getSharedPreferences("Log", 0).getBoolean("foldersLinearLayout", true);
        this.linearLayout = z;
        checkLayout(z);
        String str = "video";
        if (string == null || string.equals(str) || string.equals("") || string.equals(null)) {
            this.audioVideoFoldersAdapter.setListType(str);
            fetchFolders(str);
        } else {
            String str2 = "audio";
            this.audioVideoFoldersAdapter.setListType(str2);
            fetchFolders(str2);
        }
        return inflate;
    }

    private ArrayList<String> sortFolderNames(ArrayList<String> arrayList) {
        Collections.sort(arrayList, new Comparator<String>() {
            public int compare(String str, String str2) {
                return str.compareToIgnoreCase(str2);
            }
        });
        return arrayList;
    }

    private void fetchFolders(String str) {
        Uri uri;
        String str2 = "audio";
        if (str.equals(str2)) {
            uri = Media.EXTERNAL_CONTENT_URI;
        } else {
            uri = Video.Media.EXTERNAL_CONTENT_URI;
        }
        Uri uri2 = uri;
        String str3 = "_data";
        Cursor query = getContext().getContentResolver().query(uri2, new String[]{str3}, null, null, null);
        int columnIndex = this.cache.getColumnIndex(query, str3);
        while (query.moveToNext()) {
            String string = query.getString(columnIndex);
            String[] split = string.split("/");
            if (string.endsWith(".mp3") || (string.endsWith(".m4a") && str.equals(str2))) {
                this.folderName.add(split[split.length - 2]);
            } else if (str.equals("video")) {
                this.folderName.add(split[split.length - 2]);
            }
        }
        ArrayList<String> sortFolderNames = sortFolderNames(this.folderName);
        this.folderName = sortFolderNames;
        if (sortFolderNames.size() > 0) {
            ArrayList<String> arrayList = this.folderName;
            this.totalAudiosVideos.add(Integer.valueOf(Collections.frequency(arrayList, arrayList.get(0))));
        }
        for (int i = 1; i < this.folderName.size(); i++) {
            if (!((String) this.folderName.get(i)).equals(this.folderName.get(i - 1))) {
                ArrayList<String> arrayList2 = this.folderName;
                this.totalAudiosVideos.add(Integer.valueOf(Collections.frequency(arrayList2, arrayList2.get(i))));
            }
        }
        ArrayList arrayList3 = new ArrayList(new LinkedHashSet(this.folderName));
        this.audioVideoFoldersAdapter.setValues(arrayList3, this.totalAudiosVideos, getActivity());
//        nativeAd(arrayList3);
    }

    public void countVideos(ArrayList<String> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            String[] strArr = {"_data"};
            StringBuilder sb = new StringBuilder();
            String str = "%";
            sb.append(str);
            sb.append((String) arrayList.get(i));
            sb.append(str);
            String[] strArr2 = {sb.toString()};
            ContentResolver contentResolver = getContext().getContentResolver();
            Uri uri = Video.Media.EXTERNAL_CONTENT_URI;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("datetaken");
            sb2.append(" DESC");
            Cursor query = contentResolver.query(uri, strArr, "_data like?", strArr2, sb2.toString());
            int i2 = 0;
            while (query.moveToNext()) {
                i2++;
            }
            this.totalAudiosVideos.add(Integer.valueOf(i2));
        }
    }

    private void nativeAd(final ArrayList<String> arrayList) {
        MobileAds.initialize((Context) getActivity(), "ca-app-pub-1909992210462949/5960469852");
        new Builder((Context) getActivity(), "ca-app-pub-1909992210462949/6614064211").forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                AudioVideoFoldersFragment.this.audioVideoFoldersAdapter.setValues(arrayList, AudioVideoFoldersFragment.this.totalAudiosVideos, AudioVideoFoldersFragment.this.getActivity(), new NativeTemplateStyle.Builder().build(), unifiedNativeAd);
                AudioVideoFoldersFragment.this.recyclerView.setAdapter(AudioVideoFoldersFragment.this.audioVideoFoldersAdapter);
            }
        }).withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build().loadAd(new AdRequest.Builder().build());
    }

    public void checkLayout(boolean z) {
        this.audioVideoFoldersAdapter.setLayout(z);
        if (z) {
            this.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            this.recyclerView.setAdapter(this.audioVideoFoldersAdapter);
            return;
        }
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 4);
        gridLayoutManager.setSpanSizeLookup(new SpanSizeLookup() {
            public int getSpanSize(int i) {
                return AudioVideoFoldersFragment.this.audioVideoFoldersAdapter.getItemViewType(i) != 1 ? 1 : 4;
            }
        });
        this.recyclerView.setLayoutManager(gridLayoutManager);
        this.recyclerView.setAdapter(this.audioVideoFoldersAdapter);
    }
}
