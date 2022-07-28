package com.mp4andmp3.superextremeplayer;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.gms.ads.AdRequest.Builder;
import com.google.android.gms.ads.AdView;

public class ExtraFragment extends Fragment {
    LinearLayout aboutUsLayout;
    boolean darkMode;
    LinearLayout downloadLayout;
    LinearLayout moreAppsLayout;
    LinearLayout rateUsLayout;
    LinearLayout settingLayout;
    
    public Switch sw1;
    LinearLayout whatsAppLayout;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_extra, viewGroup, false);
        ((AdView) inflate.findViewById(R.id.adView)).loadAd(new Builder().build());
        this.sw1 = (Switch) inflate.findViewById(R.id.switch1);
        this.whatsAppLayout = (LinearLayout) inflate.findViewById(R.id.whatsAppLayout);
        this.downloadLayout = (LinearLayout) inflate.findViewById(R.id.downloadLayout);
        this.settingLayout = (LinearLayout) inflate.findViewById(R.id.settingLayout);
        this.moreAppsLayout = (LinearLayout) inflate.findViewById(R.id.moreAppsLayout);
        this.rateUsLayout = (LinearLayout) inflate.findViewById(R.id.rateUsLayout);
        this.aboutUsLayout = (LinearLayout) inflate.findViewById(R.id.aboutUsLayout);
        boolean z = getActivity().getSharedPreferences("Log", 0).getBoolean("AppDarkMode", false);
        this.darkMode = z;
        if (z) {
            this.sw1.setChecked(true);
        } else {
            this.sw1.setChecked(false);
        }
        this.sw1.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                Editor edit = ExtraFragment.this.getActivity().getSharedPreferences("Log", 0).edit();
                String str = "AppDarkMode";
                if (ExtraFragment.this.sw1.isChecked()) {
                    AppCompatDelegate.setDefaultNightMode(2);
                    edit.putBoolean(str, true);
                    edit.commit();
                    ExtraFragment.this.getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout, new ExtraFragment()).commit();
                    return;
                }
                AppCompatDelegate.setDefaultNightMode(1);

                edit.putBoolean(str, false);
                ExtraFragment.this.getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, new ExtraFragment()).commit();
                edit.commit();
            }
        });
        this.whatsAppLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExtraFragment.this.startActivity(new Intent(ExtraFragment.this.getActivity(), WhatsappVideoList.class));
            }
        });
        this.downloadLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExtraFragment.this.startActivity(new Intent(ExtraFragment.this.getActivity(), DownloadVideos.class));
            }
        });
        this.settingLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                Toast.makeText(ExtraFragment.this.getActivity(), "Coming Soon", Toast.LENGTH_LONG).show();
            }
        });
        this.rateUsLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExtraFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=com.mp4andmp3.superextremeplayer")));
            }
        });
        this.moreAppsLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExtraFragment.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=Creation+Center")));
            }
        });
        this.aboutUsLayout.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                ExtraFragment.this.startActivity(new Intent(ExtraFragment.this.getActivity(), AboutUs.class));
            }
        });
        return inflate;
    }
}
