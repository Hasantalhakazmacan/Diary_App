package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // XML'deki butonları id'leri ile buluyoruz
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)

        // Ayarlar (btnSettings) butonuna tıklandığında çalışacak kod
        btnSettings.setOnClickListener {
            // SettingsPage sınıfına yönlendirir
            val intent = Intent(this, SettingsPage::class.java)
            startActivity(intent)
        }

        // Yeni Ekle (fabAdd) butonuna tıklandığında çalışacak kod
        fabAdd.setOnClickListener {
            // DiaryPage sınıfına yönlendirir
            val intent = Intent(this, DiaryPage::class.java)
            startActivity(intent)
        }
    }
}