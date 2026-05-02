package com.hasantalhakazmacan.diary_calisma

import android.content.Intent // Intent importu eklendi
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ViewsPage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_views_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bileşenleri tanımlıyoruz
        val btnEdit = findViewById<ImageButton>(R.id.btnEdit)
        val tvTitle = findViewById<TextView>(R.id.tvTitle)
        val tvContent = findViewById<TextView>(R.id.tvContent)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val btnFavorite = findViewById<ImageButton>(R.id.btnFavorite)
        val btnBack = findViewById<ImageButton>(R.id.btnBack) // Geri butonu tanımlandı

        // Intent verilerini alıyoruz
        val incomingTitle = intent.getStringExtra("EXTRA_TITLE")
        val incomingContent = intent.getStringExtra("EXTRA_CONTENT")
        val incomingDate = intent.getStringExtra("EXTRA_DATE")
        val incomingFavoriteStatus = intent.getBooleanExtra("EXTRA_IS_FAVORITE", false)

        // Verileri yansıtıyoruz
        if (incomingTitle != null) tvTitle.text = incomingTitle
        if (incomingContent != null) tvContent.text = incomingContent
        if (incomingDate != null) tvDate.text = incomingDate

        if (incomingFavoriteStatus) {
            btnFavorite.setImageResource(R.drawable.heart_filled_icon)
        } else {
            btnFavorite.setImageResource(R.drawable.heart_icon)
        }

        // --- GERİ BUTONU YÖNLENDİRMESİ ---
        btnBack.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish() // Bu sayfayı kapatır
        }

        btnEdit.setOnClickListener {
            val currentTitle = tvTitle.text.toString()
            val currentContent = tvContent.text.toString()
            val currentIDate = tvDate.text.toString()

            // Mevcut favori durumunu kontrol et (Tag veya değişken üzerinden)
            // Eğer bir değişkende tutmuyorsan, o anki ikonun ne olduğuna bakabiliriz
            val intent = Intent(this, DiaryPage::class.java)

            intent.putExtra("EXTRA_TITLE", currentTitle)
            intent.putExtra("EXTRA_CONTENT", currentContent)
            intent.putExtra("EXTRA_DATE", currentIDate)

            // Gelen favori durumunu (incomingFavoriteStatus) geri gönderiyoruz
            intent.putExtra("EXTRA_IS_FAVORITE", incomingFavoriteStatus)

            startActivity(intent)
            finish()
        }
    }
}