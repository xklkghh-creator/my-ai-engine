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
import io.noties.markwon.Markwon
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.markwon.syntax.SyntaxHighlightPlugin
import io.noties.markwon.syntax.Prism4jTheme

class MainActivity : Activity() {
    private lateinit var settings: SettingsStore
    private lateinit var chatText: TextView
    private lateinit var scroll: ScrollView
    private lateinit var input: EditText
    private lateinit var btnCopy: Button
    private lateinit var markwon: Markwon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        val theme = Prism4jThemeDefault.create()
        markwon = Markwon.builder(this)
            .usePlugin(SyntaxHighlightPlugin.create(Prism4jThemeDefault.create(), theme))
            .build()

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
                    val result = AiClient.sendRequest(settings.apiToken, "meta-llama/Meta-Llama-3-70B-Instruct", json)
                    runOnUiThread {
                        markwon.setMarkdown(chatText, result)
                        btnCopy.visibility = Button.VISIBLE
                        scroll.post { scroll.fullScroll(ScrollView.FOCUS_DOWN) }
                    }
                }.start()
            }
        }
    }
}
