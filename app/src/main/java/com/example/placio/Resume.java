
package com.example.placio;

import androidx.annotation.NonNull;
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
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Resume extends AppCompatActivity {
    WebView webView;
    Button cancel,edit;
    FirebaseFirestore firestore;
    ProgressBar progbar;

    private String removePdfTopIcon = "javascript:(function() {" + "document.querySelector('[role=\"toolbar\"]').remove();})()";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        firestore = FirebaseFirestore.getInstance();

        Intent intent = getIntent();



        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_resume);
        webView = findViewById(R.id.web);
        cancel = findViewById(R.id.cancel);
        edit=findViewById(R.id.edit);
        progbar = (ProgressBar) findViewById(R.id.progbar);
        progbar.setVisibility(View.VISIBLE);
        webView.setVisibility(View.INVISIBLE);
        String pdfUrl = getIntent().getExtras().getString("url");
        showPdfFile(pdfUrl);



        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(EditResume.class);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openNewActivity(CopleteProfile.class);
            }
        });
    }

    private void openNewActivity(final Class<? extends Activity> ActivityToOpen) {
        startActivity(new Intent(getBaseContext(), ActivityToOpen));
    }

    private void showPdfFile(final String pdfUrl) {
        webView.invalidate();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setSupportZoom(true);
        webView.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + pdfUrl);
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
                    progbar.setVisibility(View.INVISIBLE);
                    webView.setVisibility(View.VISIBLE);

                } else {
                    showPdfFile(pdfUrl);
                }
            }
        });
    }
}