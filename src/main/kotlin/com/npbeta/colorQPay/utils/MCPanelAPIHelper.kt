package com.npbeta.colorQPay.utils


import com.npbeta.colorQPay.Main
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody


class MCPanelAPIHelper {

    private val client = OkHttpClient()

    fun sendCommand(apiURL: String, apiKey: String, cmd: String): Boolean {
        Main.logger.debug("向面板发送命令: $cmd")
        val json: MediaType = "application/json; charset=utf-8".toMediaType()
        val requestBody="{ \"command:\": \"$cmd\"}".toRequestBody(json)
        val request= Request.Builder().url(apiURL)
            .addHeader("charset", "utf-8")
            .addHeader("Accept", "application/json")
            .addHeader("Authorization", "Bearer $apiKey")
            .post(requestBody).build()
        val responseCode = client.newCall(request).execute().code
        Main.logger.debug("HTTP 响应 $responseCode")
        return responseCode == 204
    }
}