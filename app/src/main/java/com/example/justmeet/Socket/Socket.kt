package com.example.justmeet.Socket

import com.example.justmeet.Models.Question
import com.example.justmeet.Models.User
import com.example.justmeet.Models.listQuestion
import com.example.justmeet.Models.listQuestionAux
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*
import okhttp3.*
import java.util.concurrent.CompletableFuture
import kotlin.coroutines.CoroutineContext


class Socket(user: User) : WebSocketListener(), CoroutineScope {
    lateinit var webSocket: WebSocket
    var job = Job()
    //val client = OkHttpClient()
    val gson = Gson()
    val listType = object : TypeToken<ArrayList<Question>>() {}.type
    var urlServer = "ws://172.16.24.123:45456/ws/"

    //val request = Request.Builder().url(urlServer + user.idUser).build()
   // webSocket = client.newWebSocket(request, object : WebSocketListener()

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
                listQuestion = gson.fromJson(text, listType)

        for (question in listQuestion) {
            println(question.question1)
        }


    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}



