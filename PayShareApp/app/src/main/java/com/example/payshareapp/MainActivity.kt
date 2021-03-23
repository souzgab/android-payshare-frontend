package com.example.payshareapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.payshare.payshareapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(1000)
        setTheme(R.style.Theme_AppCompat)

        setContentView(R.layout.activity_splash)
    }
}