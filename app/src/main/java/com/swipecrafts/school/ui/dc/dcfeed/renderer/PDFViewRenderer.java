package com.swipecrafts.school.ui.dc.dcfeed.renderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swipecrafts.school.R;
import com.swipecrafts.school.ui.dc.dcfeed.ContentFeed;
import com.swipecrafts.school.utils.renderer.BaseViewRenderer;
import com.swipecrafts.school.utils.renderer.ClickListener;

import java.text.DateFormat;

/**
 * Created by Madhusudan Sapkota on 3/22/2018.
 */

public class PDFViewRenderer extends BaseViewRenderer<ContentFeed, PDFViewRenderer.ViewHolder>{

    private Context mContext;

    public PDFViewRenderer(Context context, int mType, ClickListener<ContentFeed> listener) {
        super(mType, listener);
        this.mContext = context;
    }
    @Override
    public void bindView(@NonNull ContentFeed model, @NonNull ViewHolder holder) {
        holder.model = model;
        holder.bindView(model);

        if (getClickListener() != null){
            // handel click events
            holder.fullScreenBtn.setOnClickListener( it ->{
                getClickListener().onItemClicked(holder.model);
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder createViewHolder(@Nullable ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_pdf_feed_item, parent, false);
        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ContentFeed model;

        private final TextView contentTitleTV;
        private final TextView contentDescTV;
        private final TextView contentDateTV;

//        private final PDFView contentPdfView;
        private WebView contentPdfView;
        private final ProgressBar progressBar;

        private ImageButton fullScreenBtn;

        private View view;

        public ViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;

            contentTitleTV = (TextView) view.findViewById(R.id.contentTitleTV);
            contentDescTV = (TextView) view.findViewById(R.id.contentDescTV);
            contentDateTV = (TextView) view.findViewById(R.id.contentTimeTV);

            contentPdfView = (WebView) view.findViewById(R.id.pdfWebView);
            progressBar = (ProgressBar) view.findViewById(R.id.contentPDFProgress);

            fullScreenBtn = (ImageButton) view.findViewById(R.id.fullScreenBtn);
        }


        public void bindView(ContentFeed model) {
            contentPdfView.getSettings().setJavaScriptEnabled(true);
            contentPdfView.setWebViewClient(new PDFViewer(this));
            contentPdfView.getSettings().setBuiltInZoomControls(false);
            Log.e("pdf", model.getDcContentLinkKey());
            contentPdfView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + model.getDcContentLinkKey());

            contentTitleTV.setText(model.getDcContentTitle());
            String desc = model.getDcContentDesc();
            if (TextUtils.isEmpty(desc)) desc = model.getDcContentName();
            contentDescTV.setText(desc);

//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//            String time = sdf.format(model.getDcContentPostedOn());
//
            String time = DateFormat.getDateInstance(DateFormat.LONG).format(model.getDcContentPostedOn());
            contentDateTV.setText(time);
        }

        public void hideProgressBar(){
            progressBar.setVisibility(View.GONE);
            contentPdfView.setVisibility(View.VISIBLE);
        }
    }

    public class PDFViewer extends WebViewClient {
        final ViewHolder viewHolder;

        public class PDFRunnable implements Runnable {
            final PDFViewer pdfViewer;

            PDFRunnable(PDFViewer pdfViewer) {
                this.pdfViewer = pdfViewer;
            }

            public void run() {
                this.pdfViewer.viewHolder.hideProgressBar();
            }
        }

        private PDFViewer(ViewHolder viewPDF) {
            this.viewHolder = viewPDF;
        }

        public void onPageFinished(WebView webView, String str) {
            super.onPageFinished(webView, str);
        }

        public void onPageStarted(WebView webView, String str, Bitmap bitmap) {
            super.onPageStarted(webView, str, bitmap);
            this.viewHolder.hideProgressBar();
            new Handler().postDelayed(new PDFViewer.PDFRunnable(this), 4000);
        }

        public boolean shouldOverrideUrlLoading(WebView webView, String str) {
            return true;
        }
    }
}
