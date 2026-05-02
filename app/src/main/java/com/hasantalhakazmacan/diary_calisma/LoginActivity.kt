package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class LoginActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper // Veritabanı yardımcısı

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Veritabanı sınıfımızı başlatıyoruz
        dbHelper = DatabaseHelper(this)

        // XML'deki bileşenleri buluyoruz
        val btnKayit = findViewById<Button>(R.id.btnKayitOl)
        val btnGirisYap = findViewById<Button>(R.id.appCompatButton)
        val etUsername = findViewById<EditText>(R.id.editText)
        val etPassword = findViewById<EditText>(R.id.editText2)

        // Kayıt Ol sayfasına yönlendirme
        btnKayit.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Giriş Yap butonuna tıklandığında çalışacak kodlar
        btnGirisYap.setOnClickListener {
            val girilenKullaniciAdi = etUsername.text.toString().trim()
            val girilenSifre = etPassword.text.toString().trim()

            // 1. Durum: Alanlar boş mu kontrolü
            if (girilenKullaniciAdi.isEmpty() || girilenSifre.isEmpty()) {
                Toast.makeText(this, "Lütfen tüm alanları doldurun!", Toast.LENGTH_SHORT).show()
            } else {
                // Veritabanını "Okunabilir" modda açıyoruz
                val db = dbHelper.readableDatabase

                // Veritabanında bu kullanıcı adı ve şifreye sahip bir kayıt var mı sorgusu
                val query = "SELECT * FROM ${DatabaseHelper.TABLE_USERS} WHERE " +
                        "${DatabaseHelper.COLUMN_USERNAME} = ? AND " +
                        "${DatabaseHelper.COLUMN_PASSWORD} = ?"

                val selectionArgs = arrayOf(girilenKullaniciAdi, girilenSifre)
                val cursor = db.rawQuery(query, selectionArgs)

                // Eğer cursor.count 0'dan büyükse, eşleşen bir kayıt bulunmuş demektir
                if (cursor.count > 0) {
                    Toast.makeText(this, "Giriş Başarılı! Hoşgeldiniz.", Toast.LENGTH_SHORT).show()

                    // Sınıf adını MainPage olarak güncelledik
                    val intent = Intent(this@LoginActivity, MainPage::class.java)
                    startActivity(intent)

                    // Kullanıcı giriş yaptıktan sonra telefonun geri tuşuna basıp tekrar Login'e dönmesin diye:
                    finish()
                } else {
                    // Kayıt bulunamadı veya şifre yanlış
                    Toast.makeText(this, "Kullanıcı adı veya şifre hatalı!", Toast.LENGTH_SHORT).show()
                }

                // İşlem bitince cursor ve veritabanını kapatıyoruz
                cursor.close()
                db.close()
            }
        }
    }
}