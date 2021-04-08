package com.example.payshareapp

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.payshare.payshareapp.R
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        setTheme(R.style.Theme_AppCompat)
        setContentView(R.layout.activity_splash)

        val urlBackend = "https://paysharedev.herokuapp.com/v1/payshare"
    }

        inner class AsyncTaskHandle : AsyncTask<String, String, String>() {
            override fun doInBackground(vararg urlBackend: String?): String {
                var text: String
                val conn = URL(urlBackend[0]).openConnection() as HttpsURLConnection
                try {
                    conn.connect()
                    conn.inputStream.use {it.reader().use {reader -> reader.readText()} }
                        .also { text = it }
                } finally {
                    conn.disconnect()
                }
                return text
            }
        }
}