package com.swipecrafts.school.ui.dc.dcfeed;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.utils.Utils;

/**
 * Created by Madhusudan Sapkota on 4/6/2018.
 */
public class PDFViewerActivity extends AppCompatActivity {

    public static final String ARG_PDF_URL = "PDF_URL";
    public static final String ARG_PDF_TITLE= "PDF_TITLE";

    private String pdfUrl = "";
    private String pdfTitle = "Document viewer";

    private Toolbar toolbar;
    private SwipeRefreshLayout swipeRefreshLayout;

    private WebView webView;
    private LinearLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        toolbar = (Toolbar) findViewById(R.id.webViewToolbar);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.webViewSwipeToRefreshLayout);

        // Setup refresh listener which triggers new data loading
        swipeRefreshLayout.setOnRefreshListener( () ->{
            if (Utils.isOnline(this)){
                errorLayout.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);
                webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
            }else {
                webView.setVisibility(View.GONE);
                errorLayout.setVisibility(View.VISIBLE);
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                R.color.colorPrimaryDark,
                R.color.colorPrimary,
                android.R.color.holo_red_light);

        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                pdfUrl = extras.getString(ARG_PDF_URL);
                pdfTitle= extras.getString(ARG_PDF_TITLE);
            }
        }

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(pdfTitle);
        }

        errorLayout = (LinearLayout) findViewById(R.id.webViewErrorLyt);
        TextView errorMessage = errorLayout.findViewById(R.id.error_message);
        errorMessage.setText(getString(R.string.no_internet_message));

        webView = (WebView) findViewById(R.id.fileViewerWebView);

        if (Utils.isOnline(this)){
            errorLayout.setVisibility(View.GONE);
            webView.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setEnabled(false);

            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new PDFViewer(this));
            webView.getSettings().setBuiltInZoomControls(false);
            webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdfUrl);
        }else {
            webView.setVisibility(View.GONE);
            errorLayout.setVisibility(View.VISIBLE);
            swipeRefreshLayout.setRefreshing(false);
        }
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

    public class PDFViewer extends WebViewClient {
        final PDFViewerActivity pdfFragment;

        private PDFViewer(PDFViewerActivity viewPDF) {
            this.pdfFragment = viewPDF;
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
            pdfFragment.swipeRefreshLayout.setRefreshing(false);
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return false;
        }
    }

}
