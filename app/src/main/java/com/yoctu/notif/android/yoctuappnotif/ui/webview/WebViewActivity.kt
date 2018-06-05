package com.yoctu.notif.android.yoctuappnotif.ui.webview

import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.yoctu.notif.android.yoctuappnotif.R
import kotlinx.android.synthetic.main.activity_web_view.*

/**
 * Display a web view
 * Created by gael on 05.06.18.
 */

class WebViewActivity: AppCompatActivity() {

    companion object {

        val KEY_URL = "key_url"

        fun newIntent(context: Context, url: String): Intent{
            var intent = Intent(context,WebViewActivity::class.java)
            intent.putExtra(KEY_URL,url)
            return intent
        }
    }
    private var url: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_web_view)
        manageIntent(intent)

        url?.let { u ->
            loadURL()
        }
    }

    /**
     * Get url from Intent Extra
     */
    private fun manageIntent(intent: Intent) {
        intent?.let { i ->
            if (i.hasExtra(KEY_URL))
                url = i.getStringExtra(KEY_URL)
        }
    }

    /**
     * redirect user to web view with good URL
     */
    private fun loadURL() {
        yoctu_web_view?.let { wv ->
            wv.loadUrl(url)
            // Enable Javascript
            val webSettings = wv.settings
            webSettings.javaScriptEnabled = true
            // Force links and redirects to open in the WebView instead of in a browser
            wv.webViewClient = WebViewClient()
            wv.webViewClient = object : WebViewClient() {

                override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
                    handler.proceed()
                }
            }
        }
    }
}