package com.example.placio;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ResumeReview extends AppCompatActivity {
    WebView webView;
    Button confrim,cancel;
    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resume_review);
        webView=findViewById(R.id.web);
        cancel=findViewById(R.id.cancel);
        confrim=findViewById(R.id.confrim);
        String pdfUrl=getIntent().getExtras().getString("url");


        showPdfFile(pdfUrl);
        confrim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(MainHome.class);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(MainHome.class);
            }
        });
    }
    private void openNewActivity( final Class<? extends Activity> ActivityToOpen)
    {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    private void showPdfFile(final String pdfUrl) {
        webView.invalidate();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url="+pdfUrl);
        webView.setWebViewClient(new WebViewClient() {
            boolean checkOnPageStartedCalled = false;

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                checkOnPageStartedCalled = true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                if (checkOnPageStartedCalled) {
                    webView.loadUrl(removePdfTopIcon);
                } else {
                    showPdfFile(pdfUrl);
                }
            }
        });
    }
}