package com.mp4andmp3.superextremeplayer;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.mp4andmp3.superextremeplayer.R;
import com.google.android.ads.nativetemplates.NativeTemplateStyle;
import com.google.android.ads.nativetemplates.TemplateView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader.Builder;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.formats.NativeAdOptions;
import com.google.android.gms.ads.formats.UnifiedNativeAd;
import com.google.android.gms.ads.formats.UnifiedNativeAd.OnUnifiedNativeAdLoadedListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class AboutUs extends AppCompatActivity {
    TemplateView template;

   
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_about_us);
        setTitle("About Us");
//        MobileAds.initialize((Context) this, (OnInitializationCompleteListener) new OnInitializationCompleteListener() {
//            public void onInitializationComplete(InitializationStatus initializationStatus) {
//            }
//        });
//        nativeAd();
        this.template = (TemplateView) findViewById(R.id.my_template);
    }

    private void nativeAd() {
        MobileAds.initialize((Context) this, "ca-app-pub-1909992210462949/5960469852");
        new Builder((Context) this, "ca-app-pub-1909992210462949/6614064211").forUnifiedNativeAd(new OnUnifiedNativeAdLoadedListener() {
            public void onUnifiedNativeAdLoaded(UnifiedNativeAd unifiedNativeAd) {
                AboutUs.this.template.setVisibility(View.VISIBLE);
                NativeTemplateStyle build = new NativeTemplateStyle.Builder().build();
                AboutUs.this.template.setVisibility(View.VISIBLE);
                AboutUs.this.template.setStyles(build);
//                AboutUs.this.template.setNativeAd(unifiedNativeAd);
            }
        }).withAdListener(new AdListener() {
            public void onAdFailedToLoad(int i) {
            }
        }).withNativeAdOptions(new NativeAdOptions.Builder().build()).build().loadAd(new AdRequest.Builder().build());
    }
}
