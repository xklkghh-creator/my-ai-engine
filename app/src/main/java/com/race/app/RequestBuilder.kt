package com.race.app

object RequestBuilder {
    fun build(
        systemPrompt: String,
        userMessage: String,
        temperature: Float
    ): String {
        val safeSystem = systemPrompt.replace("\"", "\\\"").replace("\n", "\\n")
        val safeUser = userMessage.replace("\"", "\\\"").replace("\n", "\\n")

        return """
        {
          "model": "meta-llama/llama-3-8b-instruct",
          "messages": [
            {"role": "system", "content": "$safeSystem"},
            {"role": "user", "content": "$safeUser"}
          ],
          "temperature": $temperature
        }
        """.trimIndent()
    }
}
