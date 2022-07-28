package com.mp4andmp3.superextremeplayer;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mp4andmp3.superextremeplayer.R;

public class WebViewActivity extends AppCompatActivity {
    ProgressDialog progressDialog;
    WebView webView;

    private class MyWebViewClient extends WebViewClient {
        private MyWebViewClient() {
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            WebViewActivity.this.progressDialog.setMessage("Loading Please wait..... ");
            WebViewActivity.this.progressDialog.show();
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            WebViewActivity.this.progressDialog.dismiss();
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_web_view_video_download);
        this.webView = (WebView) findViewById(R.id.webView);
        this.progressDialog = new ProgressDialog(this);
        if (isConnectingToInternet()) {
            this.webView.setWebViewClient(new MyWebViewClient());
            this.webView.loadUrl("https://www.technolligencee.com/");
            this.webView.getSettings().setJavaScriptEnabled(true);
            return;
        }
        Toast.makeText(getApplication(), "Oops!! There is no internet connection. " +
                "Please enable internet connection and try again.", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        if (this.webView.canGoBack()) {
            this.webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isConnectingToInternet() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
