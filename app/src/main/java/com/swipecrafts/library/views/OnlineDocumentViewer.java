package com.swipecrafts.library.views;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.Utils;

/**
 * Created by Madhusudan Sapkota on 4/6/2018.
 */
public class OnlineDocumentViewer extends AppCompatActivity {

    public static final String ARG_FILE_URL = "FILE_URL";
    public static final String ARG_FILE_NAME = "FILE_NAME";

    private String fileUrl = "";
    private String fileTitle = "Document viewer";

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private WebView webView;
    private LinearLayout errorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = (Toolbar) findViewById(R.id.webViewToolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.webViewSwipeToRefreshLayout);
        errorLayout = (LinearLayout) findViewById(R.id.webViewErrorLyt);


        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                fileUrl = extras.getString(ARG_FILE_URL);
                fileTitle = extras.getString(ARG_FILE_NAME);
            }
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(fileTitle);
        }

        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        Log.e("WebView", "FileUrl "+ fileUrl);

        webView = (WebView) findViewById(R.id.fileViewerWebView);

        if (Utils.isOnline(this)) {
            errorLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(false);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new OnlineDocumentViewer.DocumentViewer(this));
            webView.getSettings().setBuiltInZoomControls(false);
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + fileUrl);
        } else {
            webView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }


        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Utils.isOnline(this)) {
                errorLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + fileUrl);
            } else {
                webView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    public class DocumentViewer extends WebViewClient {
        final OnlineDocumentViewer viewerActivity;

        private DocumentViewer(OnlineDocumentViewer viewerActivity) {
            this.viewerActivity = viewerActivity;
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            viewerActivity.swipeRefreshLayout.setRefreshing(false);
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String url) {
            webView.loadUrl(url);
            return true;
        }
    }
}
