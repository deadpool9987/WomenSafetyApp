package com.finalYearproject.womensafetyapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class WebViewActivity extends AppCompatActivity {

    String TAG = WebViewActivity.class.getSimpleName();
    private Context mContext;
    private Activity activity;
    private TextView title;
    private Button btn_watchVideo;
    private String youtubelink;
    private String instructionLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        mContext = this;
        activity = this;
        WebView webView = (WebView) findViewById(R.id.webview);
        title = findViewById(R.id.title_text);
        btn_watchVideo = (Button) findViewById(R.id.btn_watch_video);
        title.setText("Detail View");
        Bundle extras = getIntent().getExtras();
        youtubelink = extras.getString("Video");
        instructionLink = extras.getString("Instruction");
        webView.loadUrl(instructionLink);

        btn_watchVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                watchYoutubeVideo(youtubelink);
            }
        });
    }

    public void watchYoutubeVideo(String url) {
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + url));
        try {
            startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            startActivity(webIntent);
        }
    }
}