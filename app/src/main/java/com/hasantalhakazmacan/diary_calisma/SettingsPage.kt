package com.hasantalhakazmacan.diary_calisma

import android.content.Intent // Intent kütüphanesini ekledik
import android.os.Bundle
import android.widget.ImageButton // ImageButton kütüphanesini ekledik
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SettingsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // XML dosyasındaki geri butonunu id'si ile buluyoruz
        val btnBack = findViewById<ImageButton>(R.id.btnBack)

        // Butona tıklandığında yapılacak işlem
        btnBack.setOnClickListener {
            // SettingsPage'den MainPage'e geçiş için Intent oluşturuyoruz
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)

            // Ayarlar sayfasını kapatarak hafızadan temizliyoruz
            finish()
        }
    }
}