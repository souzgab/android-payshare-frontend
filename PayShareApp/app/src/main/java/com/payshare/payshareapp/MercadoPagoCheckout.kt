package com.payshare.payshareapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.http.SslError
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.webkit.SslErrorHandler
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_mercado_pago.*

class MercadoPagoCheckout : AppCompatActivity() {

    companion object {
        const val PAGE_URL = ""
        const val MAX_PROGRESS = 100
        fun newIntent(context: Context, pageUrl: String): Intent {
            val intent = Intent(context, MercadoPagoCheckout::class.java)
            intent.putExtra(PAGE_URL, pageUrl)
            return intent
        }
    }
    private lateinit var pageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mercado_pago)
        // get pageUrl from String
        pageUrl = "https://www.mercadopago.com.br/checkout/v1/redirect?pref_id=224099046-3fefbab8-c1c4-4569-bf40-cb9ff7dedc3f"
        initWebView()
        setWebClient()
        handlePullToRefresh()
        loadUrl(pageUrl)
    }
    private fun handlePullToRefresh() {
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView() {
        webView.settings.javaScriptEnabled = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.domStorageEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override
            fun onReceivedSslError(view: WebView?, handler: SslErrorHandler?, error: SslError?) {
                handler?.proceed()
            }
        }
    }
    private fun setWebClient() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
                progressBar.progress = newProgress
                if (newProgress < MAX_PROGRESS && progressBar.visibility == ProgressBar.GONE) {
                    progressBar.visibility = ProgressBar.VISIBLE
                }
                if (newProgress == MAX_PROGRESS) {
                    progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        // Check if the key event was the Back button and if there's history
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack()
            return true
        }
        // If it wasn't the Back key or there's no web page history, exit the activity)
        return super.onKeyDown(keyCode, event)
    }
    private fun loadUrl(pageUrl: String) {
        webView.loadUrl(pageUrl)
    }
}