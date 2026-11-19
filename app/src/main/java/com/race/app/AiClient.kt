package com.race.app

import java.net.HttpURLConnection
import java.net.URL

object AiClient {
    // Склейка URL из кусков, чтобы не резался буфер
    private const val SCHEME = "https://"
    private const val DOMAIN = "api-inference.huggingface.co"
    private const val PATH = "/models/"
    
    fun buildUrl(model: String): String {
        return SCHEME + DOMAIN + PATH + model
    }

    fun sendRequest(token: String, model: String, jsonBody: String): String {
        val url = URL(buildUrl(model))
        val conn = url.openConnection() as HttpURLConnection
        return try {
            conn.requestMethod = "POST"
            conn.setRequestProperty("Authorization", "Bearer " + token)
            conn.setRequestProperty("Content-Type", "application/json")
            conn.doOutput = true
            conn.outputStream.use { it.write(jsonBody.toByteArray()) }
            val code = conn.responseCode
            if (code in 200..299) {
                conn.inputStream.bufferedReader().readText()
            } else {
                "Error: HTTP " + code
            }
        } catch (e: Exception) {
            "Error: " + e.message
        } finally {
            conn.disconnect()
        }
    }
}
