package com.example.clientneocafe.utils

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocketListener
import okio.ByteString
import okhttp3.*

class NotificationsWebSocket (private val listener: NotificationsWebSocketListener) {

    private lateinit var client: OkHttpClient
    private lateinit var webSocket: WebSocket

    interface NotificationsWebSocketListener {
        fun onMessage(message: String)
        fun onClose()
        fun onFailure(error: String)
    }

    private inner class NotificationsEchoWebSocketListener : WebSocketListener() {
        private val NORMAL_CLOSURE_STATUS = 1000

        override fun onOpen(webSocket: okhttp3.WebSocket, response: Response) {
            // Обработка события открытия соединения
        }

        override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
            // Обработка текстового сообщения
            listener.onMessage(text)
        }

        override fun onMessage(webSocket: okhttp3.WebSocket, bytes: ByteString) {
            // Обработка бинарного сообщения
            listener.onMessage(bytes.toString())
        }

        override fun onClosing(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
            // Обработка события закрытия соединения
            webSocket.close(NORMAL_CLOSURE_STATUS, null)
            listener.onClose()
        }

        override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: Response?) {
            // Обработка ошибки
            listener.onFailure(t.message ?: "Unknown error")
        }
    }

    fun startWebSocket() {
        client = OkHttpClient()
        val request = Request.Builder().url("ВАШ_URL_ДЛЯ_WEBSOCKET").build()
        webSocket = client.newWebSocket(request, NotificationsEchoWebSocketListener())
    }

    fun closeWebSocket() {
        webSocket.close(1000, "WebSocket closed")
        client.dispatcher.executorService.shutdown()
    }

    fun sendMessage(message: String) {
        webSocket.send(message)
    }
}