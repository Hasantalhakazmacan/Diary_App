package com.hasantalhakazmacan.diary_calisma

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbHelper = DatabaseHelper(this)
        val prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)


        val btnKayit = findViewById<Button>(R.id.btnKayitOl)
        val btnGirisYap = findViewById<Button>(R.id.appCompatButton)
        val etUsername = findViewById<EditText>(R.id.editText)
        val etPassword = findViewById<EditText>(R.id.editText2)
        val cbRememberMe = findViewById<CheckBox>(R.id.checkBox)


        btnKayit.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }


        btnGirisYap.setOnClickListener {
            val girilenKullaniciAdi = etUsername.text.toString().trim()
            val girilenSifre = etPassword.text.toString().trim()
            val rememberMe = cbRememberMe.isChecked

            if (girilenKullaniciAdi.isEmpty() || girilenSifre.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val db = dbHelper.readableDatabase
            val query = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE " +
                    "${DatabaseHelper.COLUMN_USERNAME} = ? AND " +
                    "${DatabaseHelper.COLUMN_PASSWORD} = ?"
            val selectionArgs = arrayOf(girilenKullaniciAdi, girilenSifre)
            val cursor = db.rawQuery(query, selectionArgs)

            if (cursor.count > 0) {
                // "Beni Hatırla" tercihini kaydet
                prefs.edit()
                    .putBoolean("logged_in", rememberMe)
                    .putString("user_name", girilenKullaniciAdi)
                    .apply()

                Toast.makeText(this, "Giriş Başarılı! Hoşgeldiniz.", Toast.LENGTH_SHORT).show()

                startActivity(Intent(this@LoginActivity, MainPage::class.java))
                finish()
            } else {
                Toast.makeText(this, "Kullanıcı adı veya şifre hatalı!", Toast.LENGTH_SHORT).show()
            }

            cursor.close()
            db.close()
        }
    }
}