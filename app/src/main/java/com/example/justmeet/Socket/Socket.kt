package com.example.justmeet.Socket

import com.example.justmeet.Models.Question
import com.example.justmeet.Models.User
import com.example.justmeet.Models.listQuestion
import com.example.justmeet.Models.listQuestionAux
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.*
import java.util.concurrent.CompletableFuture


class Socket(user: User) : WebSocketListener() {
    lateinit var webSocket: WebSocket
    //val client = OkHttpClient()
    val gson = Gson()
    val listType = object : TypeToken<ArrayList<Question>>() {}.type
    var urlServer = "ws://172.16.24.123:45456/ws/"


    //val request = Request.Builder().url(urlServer + user.idUser).build()
   // webSocket = client.newWebSocket(request, object : WebSocketListener()

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        runBlocking {
            var corrutina = launch {
                listQuestion = gson.fromJson(text, listType)

            }
            corrutina.join()

        }


        for (question in listQuestion) {
            println(question.question1)
        }


    }
}



