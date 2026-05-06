package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainPage : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var adapter: NoteAdapter
    private lateinit var searchBar: LinearLayout
    private lateinit var etSearch: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main_page)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)

        // XML'deki butonları id'leri ile buluyoruz
        val btnSettings = findViewById<ImageButton>(R.id.btnSettings)
        val btnSearch = findViewById<ImageButton>(R.id.btnSearch)
        val fabAdd = findViewById<FloatingActionButton>(R.id.fabAdd)
        val rvNotes = findViewById<RecyclerView>(R.id.rvNotes)
        searchBar = findViewById(R.id.searchBar)
        etSearch = findViewById(R.id.etSearch)

        // RecyclerView ayarları
        rvNotes.layoutManager = LinearLayoutManager(this)
        adapter = NoteAdapter(emptyList()) { note ->
            // Karta tıklanınca DiaryPage düzenleme modunda açılır
            val intent = Intent(this, DiaryPage::class.java)
            intent.putExtra("note_id", note.id)
            startActivity(intent)
        }
        rvNotes.adapter = adapter

        // Ayarlar butonu
        btnSettings.setOnClickListener {
            startActivity(Intent(this, SettingsPage::class.java))
        }

        // Yeni günlük (FAB)
        fabAdd.setOnClickListener {
            startActivity(Intent(this, DiaryPage::class.java))
        }

        // Arama butonu — arama çubuğunu aç/kapat
        btnSearch.setOnClickListener {
            if (searchBar.visibility == View.VISIBLE) {
                searchBar.visibility = View.GONE
                etSearch.setText("")
                refreshList()
            } else {
                searchBar.visibility = View.VISIBLE
                etSearch.requestFocus()
            }
        }

        // Arama yazısı değiştikçe filtrele
        etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val q = s?.toString()?.trim().orEmpty()
                if (q.isEmpty()) refreshList()
                else adapter.update(dbHelper.searchNotes(q))
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    override fun onResume() {
        super.onResume()
        // Sayfaya her dönüldüğünde listeyi yenile
        refreshList()
    }

    private fun refreshList() {
        adapter.update(dbHelper.getAllNotes())
    }
}