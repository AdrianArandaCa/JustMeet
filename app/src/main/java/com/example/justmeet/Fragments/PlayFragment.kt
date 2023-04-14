package com.example.justmeet.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justmeet.Activitys.GameActivity
import com.example.justmeet.Activitys.RegisterActivity
import com.example.justmeet.Models.User
import com.example.justmeet.R
import com.example.justmeet.databinding.FragmentPlayBinding
import okhttp3.*
import com.google.gson.Gson

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private lateinit var binding : FragmentPlayBinding
private lateinit var webSocket: WebSocket
class PlayFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(inflater,container,false)
        binding.btnBuscarPartida.setOnClickListener {
            val intento = Intent(context, GameActivity::class.java)
            startActivity(intento)
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

//    override fun onDestroyView() {
//        super.onDestroyView()
//        webSocket.close(1000, null)
//    }
    }


