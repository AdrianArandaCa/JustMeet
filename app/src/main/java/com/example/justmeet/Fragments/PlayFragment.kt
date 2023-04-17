package com.example.justmeet.Fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justmeet.Activitys.BottomNavigationActivity
import com.example.justmeet.Activitys.GameActivity
import com.example.justmeet.Models.*
import com.example.justmeet.Socket.Socket
import com.example.justmeet.databinding.FragmentPlayBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private lateinit var binding: FragmentPlayBinding
private lateinit var webSocket: WebSocket
var job = Job()

class PlayFragment : Fragment(), CoroutineScope {
    // TODO: Rename and change types of parameters

    val gson = Gson()
    val listType = object : TypeToken<ArrayList<Question>>() {}.type
    var urlServer = "ws://172.16.24.123:45456/ws/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        binding.btnBuscarPartida.setOnClickListener {
            var socket = Socket(userLog)
            val client = OkHttpClient()
            val request = Request.Builder().url(socket.urlServer + userLog.idUser).build()
            client.newWebSocket(request, socket)
            Handler().postDelayed({
                if (!listQuestion.isEmpty()) {
                    val intento = Intent(context, GameActivity::class.java)
                    startActivity(intento)
                }
            },3000)

        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val client = OkHttpClient()
//
//        val request = Request.Builder().url("ws://172.16.24.123:45456:6666/ws/2").build()
//
//        webSocket = client.newWebSocket(request, object : WebSocketListener() {
//
//            override fun onOpen(webSocket: WebSocket, response: Response) {
//                super.onOpen(webSocket, response)
//
//            }
//
//            override fun onMessage(webSocket: WebSocket, text: String) {
//                super.onMessage(webSocket, text)
//                val gson = Gson()
//                try {
//                    val data = gson.fromJson(text, User::class.java)
//
//
//                    activity?.runOnUiThread {
//                        binding.txtPruebaSocket.setText("Valor 1: ${data.name}\nValor 2: ${data.password}")
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//                super.onClosing(webSocket, code, reason)
//
//            }
//
//            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
//                super.onFailure(webSocket, t, response)
//
//            }
//        })
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job


//    override fun onDestroyView() {
//        super.onDestroyView()
//        webSocket.close(1000, null)
//    }
}


