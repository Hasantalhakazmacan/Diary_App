package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2000 milisaniye (2 saniye) bekleme süresi
        Handler(Looper.getMainLooper()).postDelayed({

            // RegisterActivity'ye geçiş yapacak olan Intent
            val intent = Intent(this@MainActivity, RegisterActivity::class.java)
            startActivity(intent)

            // MainActivity'yi kapatıyoruz ki geri tuşuna basınca logo ekranı tekrar gelmesin
            finish()

        }, 2000)
    }
}