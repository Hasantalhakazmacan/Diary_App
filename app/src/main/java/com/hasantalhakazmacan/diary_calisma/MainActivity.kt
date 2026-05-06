package com.hasantalhakazmacan.diary_calisma

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
            val loggedIn = prefs.getBoolean("logged_in", false)

            val nextActivity = if (loggedIn) MainPage::class.java else RegisterActivity::class.java

            startActivity(Intent(this, nextActivity))
            finish()
        }, 2000)
    }
}