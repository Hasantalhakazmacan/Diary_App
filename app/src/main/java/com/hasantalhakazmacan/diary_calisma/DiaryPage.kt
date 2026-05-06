package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DiaryPage : AppCompatActivity() {

    private var isFavorite = false
    private var noteId: Int = -1   // -1 = yeni not, >0 = düzenleme
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_diary_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        val btnBack = findViewById<ImageButton>(R.id.btnBack)
        val btnSave = findViewById<ImageButton>(R.id.btnSave)
        val btnFavorite = findViewById<ImageButton>(R.id.btnFavorite)
        val btnDelete = findViewById<ImageButton>(R.id.btnDelete)
        val tvDate = findViewById<TextView>(R.id.tvDate)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etContent = findViewById<EditText>(R.id.etContent)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("tr", "TR"))
        tvDate.text = dateFormat.format(Date())
        noteId = intent.getIntExtra("note_id", -1)
        if (noteId != -1) {
            val note = dbHelper.getNoteById(noteId)
            if (note != null) {
                etTitle.setText(note.title)
                etContent.setText(note.content)
                tvDate.text = note.date
                isFavorite = note.isFavorite
            }
        } else {
            intent.getStringExtra("EXTRA_TITLE")?.let { etTitle.setText(it) }
            intent.getStringExtra("EXTRA_CONTENT")?.let { etContent.setText(it) }
            intent.getStringExtra("EXTRA_DATE")?.let { tvDate.text = it }
            isFavorite = intent.getBooleanExtra("EXTRA_IS_FAVORITE", false)
        }

        updateFavoriteIcon(btnFavorite)

        btnBack.setOnClickListener {
            finish()
        }

        btnSave.setOnClickListener {
            val titleText = etTitle.text.toString().trim()
            val contentText = etContent.text.toString().trim()
            val dateText = tvDate.text.toString()

            if (titleText.isEmpty() && contentText.isEmpty()) {
                Toast.makeText(this, "Boş not kaydedilemez", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val savedId: Int = if (noteId == -1) {
                // Yeni not ekle
                val newId = dbHelper.insertNote(titleText, contentText, dateText)
                if (newId == -1L) {
                    Toast.makeText(this, "Kaydetme başarısız", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                dbHelper.setFavorite(newId.toInt(), isFavorite)
                newId.toInt()
            } else {
                // Mevcut notu güncelle
                dbHelper.updateNote(noteId, titleText, contentText, dateText)
                dbHelper.setFavorite(noteId, isFavorite)
                noteId
            }

            Toast.makeText(this, "Kaydedildi", Toast.LENGTH_SHORT).show()


            val viewIntent = Intent(this, ViewsPage::class.java)
            viewIntent.putExtra("note_id", savedId)
            viewIntent.putExtra("EXTRA_TITLE", titleText)
            viewIntent.putExtra("EXTRA_CONTENT", contentText)
            viewIntent.putExtra("EXTRA_DATE", dateText)
            viewIntent.putExtra("EXTRA_IS_FAVORITE", isFavorite)
            startActivity(viewIntent)
            finish()
        }


        btnDelete.setOnClickListener {
            if (noteId != -1) {
                dbHelper.deleteNote(noteId)
                Toast.makeText(this, "Silindi", Toast.LENGTH_SHORT).show()
            }
            finish()
        }


        btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            updateFavoriteIcon(btnFavorite)
        }
    }

    private fun updateFavoriteIcon(btn: ImageButton) {
        if (isFavorite) {
            btn.setImageResource(R.drawable.heart_filled_icon)
        } else {
            btn.setImageResource(R.drawable.heart_icon)
        }
    }
}