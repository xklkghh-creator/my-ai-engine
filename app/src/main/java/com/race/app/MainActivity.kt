package com.race.app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ScrollView

class MainActivity : Activity() {
    private lateinit var settings: SettingsStore
    private lateinit var chatText: TextView
    private lateinit var scroll: ScrollView
    private lateinit var input: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settings = SettingsStore(this)

        chatText = findViewById(R.id.chatText)
        scroll = findViewById(R.id.chatScroll)
        input = findViewById(R.id.inputText)
        val btn = findViewById<Button>(R.id.btnSend)

        btn.setOnClickListener {
            val prompt = input.text.toString().trim()
            if (prompt.isEmpty()) return@setOnClickListener
            input.setText("")
            chatText.text = "Генерация..."
            
            Thread {
                val json = RequestBuilder.build(
                    settings.systemPrompt, prompt, settings.temperature
                )
                // TODO: Токен пока захардкожен для теста, потом вынесем в настройки
                val result = AiClient.sendRequest("ТВОЙ_ТОКЕН", "meta-llama/Meta-Llama-3-70B-Instruct", json)
                runOnUiThread {
                    chatText.text = result
                    scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
                }
            }.start()
        }
    }
}
