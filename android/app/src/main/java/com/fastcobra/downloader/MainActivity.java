package com.fastcobra.downloader;

import android.app.Activity;
import android.app.DownloadManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class MainActivity extends Activity {

    private WebView webView;

    private static final String WEBSITE =
        "https://thecobradeveloper.github.io/The-fast-Cobra-/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        webView = new WebView(this);
        setContentView(webView);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setSupportZoom(false);
        settings.setLoadsImagesAutomatically(true);

        CookieManager.getInstance().setAcceptCookie(true);
        CookieManager.getInstance().setAcceptThirdPartyCookies(webView, true);

        webView.setWebViewClient(new WebViewClient());
        webView.setWebChromeClient(new WebChromeClient());

        webView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(
                String url,
                String userAgent,
                String contentDisposition,
                String mimeType,
                long contentLength
            ) {
                try {
                    DownloadManager.Request request =
                        new DownloadManager.Request(Uri.parse(url));

                    request.setTitle(URLUtil.guessFileName(
                        url, contentDisposition, mimeType
                    ));

                    request.setDescription("THE FAST COBRA Download");
                    request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                    );

                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        URLUtil.guessFileName(url, contentDisposition, mimeType)
                    );

                    String cookie = CookieManager.getInstance().getCookie(url);
                    if (cookie != null) {
                        request.addRequestHeader("Cookie", cookie);
                    }

                    request.addRequestHeader("User-Agent", userAgent);

                    DownloadManager manager =
                        (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                    manager.enqueue(request);

                    Toast.makeText(
                        MainActivity.this,
                        "Download شروع ہو گیا ہے",
                        Toast.LENGTH_LONG
                    ).show();

                } catch (Exception e) {
                    Toast.makeText(
                        MainActivity.this,
                        "Download شروع نہیں ہو سکا",
                        Toast.LENGTH_LONG
                    ).show();
                }
            }
        });

        webView.loadUrl(WEBSITE);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
