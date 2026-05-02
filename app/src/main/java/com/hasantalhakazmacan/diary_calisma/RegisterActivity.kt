package com.hasantalhakazmacan.diary_calisma

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hasantalhakazmacan.diary_calisma.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper // Veritabanı yardımcımızı tanımlıyoruz

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DatabaseHelper nesnesini başlatıyoruz
        dbHelper = DatabaseHelper(this)

        // Kayıt Ol Butonu
        binding.appCompatButton.setOnClickListener {
            performRegister()
        }

        // Giriş Yap Butonu - LoginActivity'ye Yönlendirme
        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister() {
        val username = binding.editText.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editText2.text.toString().trim()

        // Alanların boş olup olmadığını kontrol ediyoruz
        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
        } else {
            // Veritabanını "Yazılabilir" modda açıyoruz
            val db = dbHelper.writableDatabase

            // Eklenecek verileri ContentValues ile paketliyoruz
            val values = ContentValues().apply {
                put(DatabaseHelper.COLUMN_USERNAME, username)
                put(DatabaseHelper.COLUMN_EMAIL, email)
                put(DatabaseHelper.COLUMN_PASSWORD, password)
            }

            // Verileri 'users' tablosuna ekliyoruz.
            val result = db.insert(DatabaseHelper.TABLE_USERS, null, values)

            if (result != -1L) {
                // Kayıt başarılı oldu
                Toast.makeText(this, "Kayıt işlemi başarılı!", Toast.LENGTH_SHORT).show()

                // Başarılı kayıttan sonra otomatik olarak Login ekranına yönlendir
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish() // Kullanıcı geri tuşuna basınca tekrar kayıt ekranına dönmesin
            } else {
                // Kayıt sırasında bir hata oluştu
                Toast.makeText(this, "Kayıt başarısız oldu. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show()
            }

            // DİKKAT: Geliştirme aşamasında Database Inspector'ı izleyebilmek için
            // close() işlemini geçici olarak kapattık (yorum satırı yaptık).
            // db.close()
        }
    }
}