package com.race.app

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val etApiKey = findViewById<EditText>(R.id.etApiKey)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val settings = SettingsStore(this)

        // Если ключ уже сохранен, сразу идем в чат
        if (settings.apiToken.isNotEmpty()) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        btnLogin.setOnClickListener {
            val key = etApiKey.text.toString().trim()
            if (key.isEmpty()) {
                Toast.makeText(this, "Введите API Key", Toast.LENGTH_SHORT).show()
            } else {
                settings.apiToken = key
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
