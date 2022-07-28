package com.mp4andmp3.superextremeplayer;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class AudioVideoFoldersAdapter extends Adapter<AudioVideoFoldersAdapter.ViewHolder> {
    Activity activity;
    ArrayList<String> folderName;
    boolean linearLayout;
    String listType;
    NativeTemplateStyle styles;
    ArrayList<Integer> totalVideo;
//    UnifiedNativeAd unifiedNativeAd;

    public class NativeAdViewHolder extends ViewHolder {
        TemplateView template;

        public NativeAdViewHolder(View view) {
            super(view);
            this.template = (TemplateView) view.findViewById(R.id.my_template);
        }
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        TextView folderName;
        RelativeLayout relativeLayout;
        TextView totalVideo;

        public ViewHolder(View view) {
            super(view);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            this.folderName = (TextView) view.findViewById(R.id.folderName);
            this.totalVideo = (TextView) view.findViewById(R.id.totalVideo);
        }
    }

    public void setValues(ArrayList<String> arrayList, ArrayList<Integer> arrayList2, Activity activity2) {
        this.folderName = arrayList;
        this.totalVideo = arrayList2;
        this.activity = activity2;
    }

    public void setValues(ArrayList<String> arrayList, ArrayList<Integer> arrayList2, Activity activity2, NativeTemplateStyle nativeTemplateStyle, UnifiedNativeAd unifiedNativeAd2) {
        this.folderName = arrayList;
        this.totalVideo = arrayList2;
        this.activity = activity2;
        this.styles = nativeTemplateStyle;
//        this.unifiedNativeAd = unifiedNativeAd2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
            return new NativeAdViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.native_ad_layout, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.linearLayout ? R.layout.audio_video_folders_items :
                R.layout.grid_audio_video_folders_items, viewGroup, false));
    }

    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
//        if (getItemViewType(i) == 1) {
//            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) viewHolder;
//            nativeAdViewHolder.template.setStyles(this.styles);
//            nativeAdViewHolder.template.setNativeAd(this.unifiedNativeAd);
//            return;
//        }
        String str = " Videos";
        String str2 = " ";
        if (i <= 4 || this.styles == null) {
            try {
                if (!this.linearLayout) {
                    viewHolder.folderName.setText(new StringTokenizer((String) this.folderName.get(i), str2).nextToken());
                } else {
                    viewHolder.folderName.setText((CharSequence) this.folderName.get(i));
                }
                if (this.listType.equals("video")) {
                    TextView textView = viewHolder.totalVideo;
                    StringBuilder sb = new StringBuilder();
                    sb.append(this.totalVideo.get(i));
                    sb.append(str);
                    textView.setText(sb.toString());
                } else {
                    TextView textView2 = viewHolder.totalVideo;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.totalVideo.get(i));
                    sb2.append(" Audios");
                    textView2.setText(sb2.toString());
                }
                viewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(AudioVideoFoldersAdapter.this.activity, AudioVideoList.class);
                        intent.putExtra("ListType", AudioVideoFoldersAdapter.this.listType);
                        intent.putExtra("FolderName", (String) AudioVideoFoldersAdapter.this.folderName.get(i));
                        AudioVideoFoldersAdapter.this.activity.startActivity(intent);
                    }
                });
            } catch (Exception unused) {
            }
        } else {
            if (!this.linearLayout) {
                viewHolder.folderName.setText(new StringTokenizer((String) this.folderName.get(i - 1), str2).nextToken());
            } else {
                viewHolder.folderName.setText((CharSequence) this.folderName.get(i - 1));
            }
            TextView textView3 = viewHolder.totalVideo;
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.totalVideo.get(i - 1));
            sb3.append(str);
            textView3.setText(sb3.toString());
            viewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Intent intent = new Intent(AudioVideoFoldersAdapter.this.activity, AudioVideoList.class);
                    intent.putExtra("ListType", AudioVideoFoldersAdapter.this.listType);
                    intent.putExtra("FolderName", (String) AudioVideoFoldersAdapter.this.folderName.get(i - 1));
                    AudioVideoFoldersAdapter.this.activity.startActivity(intent);
                }
            });
        }
    }

    public int getItemCount() {
        if (this.styles != null) {
            return this.folderName.size() + 1;
        }
        return this.folderName.size();
    }

    public int getItemViewType(int i) {
        if (this.styles != null) {
            if (this.folderName.size() <= 4) {
                if (i == this.folderName.size()) {
                    return 1;
                }
                return 0;
            } else if (i == 4) {
                return 1;
            }
        }
        return 0;
    }

    public void setLayout(boolean z) {
        this.linearLayout = z;
    }

    public void setListType(String str) {
        this.listType = str;
    }
}
