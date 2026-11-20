package com.race.app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.ScrollView
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast

class MainActivity : Activity() {
    private lateinit var settings: SettingsStore
    private lateinit var chatText: TextView
    private lateinit var scroll: ScrollView
    private lateinit var input: EditText
    private lateinit var btnCopy: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        settings = SettingsStore(this)
        chatText = findViewById(R.id.chatText)
        scroll = findViewById(R.id.chatScroll)
        input = findViewById(R.id.inputText)
        btnCopy = findViewById(R.id.btnCopy)
        val btn = findViewById<Button>(R.id.btnSend)

        btnCopy.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = android.content.ClipData.newPlainText("Race", chatText.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this, "Скопировано", Toast.LENGTH_SHORT).show()
        }

        btn.setOnClickListener {
            val prompt = input.text.toString().trim()
            if (prompt.isNotEmpty()) {
                input.setText("")
                chatText.text = "Генерация..."
                btnCopy.visibility = Button.GONE
                
                Thread {
                    val json = RequestBuilder.build(
                        settings.systemPrompt, prompt, settings.temperature
                    )
                    val result = AiClient.sendRequest(settings.apiToken, "meta-llama/llama-3-8b-instruct", json)
                    runOnUiThread {
                        chatText.text = result
                        btnCopy.visibility = Button.VISIBLE
                        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
                    }
                }.start()
            }
        }
    }
}
