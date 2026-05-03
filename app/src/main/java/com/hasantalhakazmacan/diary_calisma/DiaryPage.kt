package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryPage : AppCompatActivity() {

    // Sınıf düzeyinde değişken, böylece tıklama olaylarında durumu korur
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diary_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bileşenleri tanımlıyoruz
        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        val btnFavorite = findViewById<ImageButton>(R.id.btnFavorite)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)

        // 1. ADIM: Varsayılan Tarihi Ayarla (Yeni kayıt için)
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr", "TR"))
        tvDate.text = dateFormat.format(Date())

        // 2. ADIM: ViewsPage'den (Düzenleme için) gelen verileri kontrol et
        val editTitle = intent.getStringExtra("EXTRA_TITLE")
        val editContent = intent.getStringExtra("EXTRA_CONTENT")
        val editDate = intent.getStringExtra("EXTRA_DATE")
        // Favori durumunu al, veri yoksa false kabul et
        val editFavorite = intent.getBooleanExtra("EXTRA_IS_FAVORITE", false)

        // Eğer veriler boş değilse (Düzenleme Modu), bileşenlere yerleştir
        if (editTitle != null) {
            etTitle.setText(editTitle)
        }
        if (editContent != null) {
            etContent.setText(editContent)
        }
        if (editDate != null) {
            tvDate.text = editDate // Eski tarihi koru
        }

        // Favori durumunu ve ikonunu eşitle
        isFavorite = editFavorite
        updateFavoriteIcon(btnFavorite)

        // GERİ BUTONU: Ana sayfaya döner
        btnBack.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
            finish()
        }

        // KAYDET BUTONU: Verileri paketleyip ViewsPage'e gönderir
        btnSave.setOnClickListener {
            val titleText = etTitle.text.toString()
            val contentText = etContent.text.toString()
            val dateText = tvDate.text.toString()

            val intent = Intent(this, ViewsPage::class.java)

            intent.putExtra("EXTRA_TITLE", titleText)
            intent.putExtra("EXTRA_CONTENT", contentText)
            intent.putExtra("EXTRA_IS_FAVORITE", isFavorite)
            intent.putExtra("EXTRA_DATE", dateText)

            startActivity(intent)
        }

        // FAVORİ BUTONU: Durumu değiştirir ve görseli günceller
        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon(btnFavorite)
        }
    }

    // İkon güncellemeyi merkezi bir fonksiyon yaptım, kod tekrarını önler
    private fun updateFavoriteIcon(btn: ImageButton) {
        if (isFavorite) {
            btn.setImageResource(R.drawable.heart_filled_icon)
        } else {
            btn.setImageResource(R.drawable.heart_icon)
        }
    }
}