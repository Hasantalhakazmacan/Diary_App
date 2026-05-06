package com.hasantalhakazmacan.diary_calisma

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hasantalhakazmacan.diary_calisma.databinding.ActivityRegisterBinding

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)


        binding.appCompatButton.setOnClickListener {
            performRegister()
        }


        binding.btnGoToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun performRegister() {
        val username = binding.editText.text.toString().trim()
        val email = binding.editTextEmail.text.toString().trim()
        val password = binding.editText2.text.toString().trim()


        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Lütfen tüm alanları doldurun", Toast.LENGTH_SHORT).show()
            return
        }


        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Geçerli bir e-mail adresi girin", Toast.LENGTH_SHORT).show()
            return
        }


        if (password.length < 4) {
            Toast.makeText(this, "Şifre en az 4 karakter olmalı", Toast.LENGTH_SHORT).show()
            return
        }


        if (dbHelper.isEmailRegistered(email)) {
            Toast.makeText(this, "Bu e-mail ile zaten bir hesap var", Toast.LENGTH_LONG).show()
            return
        }


        val result = dbHelper.insertUser(username, email, password)

        if (result != -1L) {
            Toast.makeText(this, "Kayıt işlemi başarılı!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Toast.makeText(this, "Kayıt başarısız oldu. Lütfen tekrar deneyin.", Toast.LENGTH_SHORT).show()
        }
    }
}