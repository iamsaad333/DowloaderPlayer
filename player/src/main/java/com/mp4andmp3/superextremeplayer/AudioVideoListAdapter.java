package com.mp4andmp3.superextremeplayer;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.OnScanCompletedListener;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Environment;
import android.provider.MediaStore.Audio.Media;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ShareCompat.IntentBuilder;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class AudioVideoListAdapter extends Adapter<AudioVideoListAdapter.ViewHolder> {
    Activity activity;
    String folderName;
    boolean linearLayout;
    String listType;
    NativeTemplateStyle styles;
//    UnifiedNativeAd unifiedNativeAd;
    ArrayList<String> videosDuration;
    ArrayList<String> videosTitle;
    ArrayList<String> videosUri;

    public class BannerAdViewHolder extends ViewHolder {
//        AdView mAdView;

        public BannerAdViewHolder(View view) {
            super(view);
//            this.mAdView = (AdView) view.findViewById(R.id.adView);
        }
    }

    public class NativeAdViewHolder extends ViewHolder {
        TemplateView template;

        public NativeAdViewHolder(View view) {
            super(view);
            this.template = (TemplateView) view.findViewById(R.id.my_template);
        }
    }

    public class ViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        ImageView imageView;
        ImageView moreOptions;
        RelativeLayout relativeLayout;
        Button save;
        TextView videoDescription;
        TextView videoName;

        public ViewHolder(View view) {
            super(view);
            this.relativeLayout = (RelativeLayout) view.findViewById(R.id.relativeLayout);
            this.imageView = (ImageView) view.findViewById(R.id.videoThumbnail);
            this.save = (Button) view.findViewById(R.id.save);
            this.videoName = (TextView) view.findViewById(R.id.videoName);
            this.moreOptions = (ImageView) view.findViewById(R.id.moreOptions);
            if (AudioVideoListAdapter.this.folderName.contains("Status of WhatsApp Play Folder")) {
                ImageView imageView2 = this.moreOptions;
                if (imageView2 != null) {
                    imageView2.setVisibility(View.GONE);
                    this.videoName.setVisibility(View.GONE);
                    if (AudioVideoListAdapter.this.folderName.equals("Seen Status of WhatsApp Play Folder")) {
                        this.save.setVisibility(View.VISIBLE);
                    } else {
                        this.moreOptions.setVisibility(View.VISIBLE);
                        this.videoName.setVisibility(View.VISIBLE);
                    }
                }
            }
            this.videoDescription = (TextView) view.findViewById(R.id.videoDescription);
        }
    }

    public void setValues(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, Activity activity2) {
        this.videosUri = arrayList;
        this.videosTitle = arrayList2;
        this.videosDuration = arrayList3;
        this.activity = activity2;
    }

    public void setValues(ArrayList<String> arrayList, ArrayList<String> arrayList2, ArrayList<String> arrayList3, Activity activity2, NativeTemplateStyle nativeTemplateStyle, UnifiedNativeAd unifiedNativeAd2) {
        this.videosUri = arrayList;
        this.videosTitle = arrayList2;
        this.videosDuration = arrayList3;
        this.activity = activity2;
        this.styles = nativeTemplateStyle;
//        this.unifiedNativeAd = unifiedNativeAd2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i == 1) {
//            return new NativeAdViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.native_ad_layout, viewGroup, false));
        }
        if (i == 2) {
//            return new BannerAdViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.banner_ad_layout, viewGroup, false));
        }
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.linearLayout ? R.layout.audio_video_list_item : R.layout.grid_audio_video_list_items, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if (getItemViewType(i) == 1) {
//            NativeAdViewHolder nativeAdViewHolder = (NativeAdViewHolder) viewHolder;
//            nativeAdViewHolder.template.setStyles(this.styles);
//            nativeAdViewHolder.template.setNativeAd(this.unifiedNativeAd);
        } else if (getItemViewType(i) == 2) {
//            ((BannerAdViewHolder) viewHolder).mAdView.loadAd(new Builder().build());
        } else {
            try {
                viewHolder.videoName.setText((CharSequence) this.videosTitle.get(i));
                viewHolder.videoDescription.setText(getTimeString((long) Integer.parseInt((String) this.videosDuration.get(i))));
                if (this.listType.equals("video")) {
                    ((RequestBuilder) Glide.with(this.activity).asBitmap().load(new File((String) this.videosUri.get(i))).centerCrop()).into(viewHolder.imageView);
                } else {
                    Uri audioAlbumImageContentUri = getAudioAlbumImageContentUri(this.activity, (String) this.videosUri.get(i));
                    if (audioAlbumImageContentUri.toString().length() < 2) {
                        ((RequestBuilder) Glide.with(this.activity).load(Integer.valueOf(this.activity.getResources().getIdentifier("logo", "drawable", this.activity.getPackageName()))).centerInside()).into(viewHolder.imageView);
                    } else {
                        ((RequestBuilder) Glide.with(this.activity).asBitmap().load(audioAlbumImageContentUri).centerCrop()).into(viewHolder.imageView);
                    }
                }
                viewHolder.relativeLayout.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        Intent intent = new Intent(AudioVideoListAdapter.this.activity, ViewVideo.class);
                        intent.putStringArrayListExtra("VideoUri", AudioVideoListAdapter.this.videosUri);
                        intent.putStringArrayListExtra("VideoTitle", AudioVideoListAdapter.this.videosTitle);
                        intent.putExtra("VideoPosition", i);
                        String str = "ListType";
                        if (AudioVideoListAdapter.this.listType.equals("audio")) {
                            intent.putExtra(str, AudioVideoListAdapter.this.listType);
                        } else {
                            intent.putExtra(str, AudioVideoListAdapter.this.listType);
                        }
                        String str2 = "AdsLoaded";
                        if (AudioVideoListAdapter.this.styles != null) {
                            intent.putExtra(str2, "Yes");
                        } else {
                            intent.putExtra(str2, "No");
                        }
                        AudioVideoListAdapter.this.activity.startActivity(intent);
                    }
                });
                viewHolder.save.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        AudioVideoListAdapter audioVideoListAdapter = AudioVideoListAdapter.this;
                        audioVideoListAdapter.saveWhatsAppVideo((String) audioVideoListAdapter.videosTitle.get(i), (String) AudioVideoListAdapter.this.videosUri.get(i));
                    }
                });
                viewHolder.moreOptions.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        PopupMenu popupMenu = new PopupMenu(AudioVideoListAdapter.this.activity, viewHolder.moreOptions);
                        popupMenu.inflate(R.menu.video_list_item_menu);
                        popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Uri uri;
                                int itemId = menuItem.getItemId();
                                if (itemId == R.id.delete) {
                                    try {
                                        AudioVideoListAdapter.this.deleteVideo((String) AudioVideoListAdapter.this.videosUri.get(i));
                                        AudioVideoListAdapter.this.recreateActivity();
                                        AudioVideoListAdapter.this.notifyDataSetChanged();
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else if (itemId != R.id.share) {
                                    Log.e("default", "its default ..");
                                } else {
                                    File file = new File((String) AudioVideoListAdapter.this.videosUri.get(i));
                                    if (VERSION.SDK_INT >= 24) {
                                        uri = FileProvider.getUriForFile(AudioVideoListAdapter.this.activity, AudioVideoListAdapter.this.activity.getPackageName()+".provider", file);
                                    } else {
                                        uri = Uri.fromFile(file);
                                    }
                                    IntentBuilder.from(AudioVideoListAdapter.this.activity).setStream(uri).setType("video/mp4").setChooserTitle((CharSequence) "Share video...").startChooser();
                                }
                                return true;
                            }
                        });
                        popupMenu.show();
                    }
                });
            } catch (Exception unused) {
            }
        }
    }

    public int getItemCount() {
        return this.videosTitle.size();
    }

    public int getItemViewType(int i) {
        if (this.styles == null) {
            return 0;
        }
        if (this.videosTitle.size() <= 5) {
            if (i == this.videosTitle.size() - 1) {
                return 1;
            }
            return 0;
        } else if (i == 4) {
            return 1;
        } else {
            if (i == 13 || i == 22 || i == 31 || i == 40 || i == 49) {
                return 2;
            }
            return 0;
        }
    }

    private String getTimeString(long j) {
        StringBuffer stringBuffer = new StringBuffer();
        int i = (int) (j / 3600000);
        long j2 = j % 3600000;
        int i2 = (int) (j2 / 60000);
        int i3 = (int) ((j2 % 60000) / 1000);
        String str = ":";
        String str2 = "%02d";
        if (i != 0) {
            stringBuffer.append(String.format(str2, new Object[]{Integer.valueOf(i)}));
            stringBuffer.append(str);
            stringBuffer.append(String.format(str2, new Object[]{Integer.valueOf(i2)}));
            stringBuffer.append(str);
            stringBuffer.append(String.format(str2, new Object[]{Integer.valueOf(i3)}));
        } else {
            stringBuffer.append(String.format(str2, new Object[]{Integer.valueOf(i2)}));
            stringBuffer.append(str);
            stringBuffer.append(String.format(str2, new Object[]{Integer.valueOf(i3)}));
        }
        return stringBuffer.toString();
    }

    public void setLayout(boolean z) {
        this.linearLayout = z;
    }

    public void deleteVideo(String str) {
        File file = new File(str);
        if (file.exists()) {
            String str2 = "-->";
            if (file.delete()) {
                StringBuilder sb = new StringBuilder();
                sb.append("file Deleted :");
                sb.append(str);
                Log.e(str2, sb.toString());
                callBroadCast();
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("file not Deleted :");
                sb2.append(str);
                Log.e(str2, sb2.toString());
            }
        }
        callScanItent(this.activity, str);
    }

    public void callBroadCast() {
        if (VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(this.activity, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new OnScanCompletedListener() {
                public void onScanCompleted(String str, Uri uri) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Scanned ");
                    sb.append(str);
                    sb.append(":");
                    String sb2 = sb.toString();
                    String str2 = "ExternalStorage";
                    Log.e(str2, sb2);
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("-> uri=");
                    sb3.append(uri);
                    Log.e(str2, sb3.toString());
                }
            });
        }
    }

    public void callScanItent(Context context, String str) {
        MediaScannerConnection.scanFile(context, new String[]{str}, null, null);
    }

    public void recreateActivity() {
        String str = "Video Deleted Successfully";
        if (!this.folderName.contains("Status of WhatsApp Play Folder")) {
            this.activity.finish();
            Intent intent = new Intent(this.activity, AudioVideoList.class);
            intent.putExtra("FolderName", this.folderName);
            intent.putExtra("ListType", this.listType);
            Toast.makeText(this.activity, str, 1).show();
            this.activity.startActivity(intent);
            return;
        }
        this.activity.finish();
        this.activity.startActivity(new Intent(this.activity, WhatsappVideoList.class));
        Toast.makeText(this.activity, str, 1).show();
    }

    public void setFolderName(String str) {
        this.folderName = str;
    }

    public void setListType(String str) {
        this.listType = str;
    }

    public Uri getAudioAlbumImageContentUri(Context context, String str) {
        Uri uri = Media.EXTERNAL_CONTENT_URI;
        String str2 = "album_id";
        String[] strArr = {str2};
        StringBuilder sb = new StringBuilder();
        sb.append("is_music=1 AND _data = '");
        sb.append(str);
        sb.append("'");
        Cursor query = context.getApplicationContext().getContentResolver().query(uri, strArr, sb.toString(), null, null);
        if (query == null || query.getCount() <= 0) {
            return Uri.EMPTY;
        }
        query.moveToFirst();
        Uri withAppendedId = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), Long.valueOf(query.getLong(query.getColumnIndexOrThrow(str2))).longValue());
        query.close();
        return withAppendedId;
    }

    public void saveWhatsAppVideo(String str, String str2) {
        File file = new File(Environment.getExternalStorageDirectory(), "Play/WhatsApp Status Download");
        if (!file.exists()) {
            file.mkdirs();
        }
        File file2 = new File(str2);
        StringBuilder sb = new StringBuilder();
        sb.append(Environment.getExternalStorageDirectory().toString());
        sb.append("/Play/WhatsApp Status Download/");
        sb.append(str);
        File file3 = new File(sb.toString());
        if (file2.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file2);
                FileOutputStream fileOutputStream = new FileOutputStream(file3);
                byte[] bArr = new byte[1024];
                while (true) {
                    int read = fileInputStream.read(bArr);
                    if (read > 0) {
                        fileOutputStream.write(bArr, 0, read);
                    } else {
                        fileInputStream.close();
                        fileOutputStream.close();
                        Toast.makeText(this.activity, "Status Saved Successfully, Swipe Left and Refresh List", 1).show();
                        return;
                    }
                }
            } catch (Exception e) {
                Activity activity2 = this.activity;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(e.getMessage());
                Toast.makeText(activity2, sb2.toString(), 1).show();
            }
        } else {
            Toast.makeText(this.activity, "Failed to save, please try again", 1).show();
        }
    }
}
