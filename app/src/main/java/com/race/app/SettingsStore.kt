package com.race.app

import android.content.Context

class SettingsStore(ctx: Context) {
    private val prefs = ctx.getSharedPreferences("race", 0)

    var systemPrompt: String
        get() = prefs.getString("prompt", DEFAULT_PROMPT) ?: DEFAULT_PROMPT
        set(v) = prefs.edit().putString("prompt", v).apply()

    var lang: String
        get() = prefs.getString("lang", "auto") ?: "auto"
        set(v) = prefs.edit().putString("lang", v).apply()

    var temperature: Float
        get() = prefs.getFloat("temp", 0.4f)
        set(v) = prefs.edit().putFloat("temp", v).apply()
        
    var apiToken: String
        get() = prefs.getString("token", "") ?: ""
        set(v) = prefs.edit().putString("token", v).apply()

    companion object {
        const val DEFAULT_PROMPT =
            "Ты senior-разработчик. Пиши полный рабочий код " +
            "на языке из запроса (Python, C++ и др.). " +
            "Без заглушек и '...'. Добавляй пример запуска."
    }
}
