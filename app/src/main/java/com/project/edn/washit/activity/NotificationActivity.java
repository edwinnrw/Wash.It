package com.project.edn.washit.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.project.edn.washit.Config;
import com.project.edn.washit.MyBrowser;
import com.project.edn.washit.R;

public class NotificationActivity extends AppCompatActivity  {
    private  Toolbar toolbar;
    private WebView webView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Notification");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        webView=(WebView)findViewById(R.id.webView);
        progressBar=(ProgressBar)findViewById(R.id.progress_bar123);

        webView.setWebViewClient(new MyBrowser(progressBar));
        webView.getSettings().setJavaScriptEnabled(true);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.webView.loadUrl("http://washittest.azurewebsites.net/API/notification.php?token=" + sharedPreferences.getString(Config.TOKEN_SHARED_PREF, ""));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
