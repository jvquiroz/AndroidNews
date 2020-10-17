package com.reign.androidnews.activities

import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.reign.androidnews.R

/**
 * Simple activity to display a web page content inside a WebView
 */
class NewsDetailsActivity : AppCompatActivity() {

    companion object {
        const val NEWS_STORY_URL = "NEWS_STORY_URL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        val newsDetailsView:WebView = findViewById(R.id.webview)
        newsDetailsView.settings.loadWithOverviewMode = true
        newsDetailsView.settings.useWideViewPort = true
        newsDetailsView.settings.builtInZoomControls = true
        newsDetailsView.settings.javaScriptEnabled = true
        newsDetailsView.webViewClient = WebViewClient()


        intent.getStringExtra(NEWS_STORY_URL)?.let {
            newsDetailsView.loadUrl(it)
        }
    }
}