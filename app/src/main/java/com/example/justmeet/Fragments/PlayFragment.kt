package com.example.justmeet.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justmeet.Activitys.GameActivity
import com.example.justmeet.Models.*
import com.example.justmeet.Socket.MessageListener
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.FragmentPlayBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.concurrent.thread

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private lateinit var binding: FragmentPlayBinding

class PlayFragment : Fragment(), MessageListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        binding.btnJugar.visibility = View.GONE
        val serverUrl = "ws://172.16.24.123:45456/ws/${userLog.idUser}"
        WebSocketManager.init(serverUrl, this)
        binding.btnBuscarPartida.setOnClickListener {
            thread {
                kotlin.run {
                    WebSocketManager.connect()
                }
            }

        }
        binding.btnJugar.setOnClickListener {
            if (WebSocketManager.sendMessage(" Client send ")) {
//                val intent = Intent(requireContext(), GameActivity::class.java)
//                startActivity(intent)
                //addText( " Send from the client \n " )
            }
        }
        /*closeConnectionBtn.setOnClickListener {
            WebSocketManager.close()
        }*/
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onConnectSuccess() {
        addText(" Connected successfully \n ")
    }

    override fun onConnectFailed() {
        addText(" Connection failed \n ")
    }

    override fun onClose() {
        addText(" Closed successfully \n ")
    }

    override fun onMessage(text: String?) {
        val gson = Gson()

        if (text != null) {
            if (text.startsWith("Game", false)) {
                var textSubstring = text.substring(4)
                val listType = object : TypeToken<Game>() {}.type
                gameFromSocket = gson.fromJson(textSubstring, listType)
                println("GAME TYPE :" + gameFromSocket.idGame)
                addText(" Receive message: $text \n ")
            } else {
                val listType = object : TypeToken<ArrayList<Question>>() {}.type
                listQuestion = gson.fromJson(text, listType)
                activity?.runOnUiThread {
                   // binding.btnJugar.visibility = View.VISIBLE
                    val intent = Intent(requireContext(), GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun addText(text: String?) {
        activity?.runOnUiThread {
            //contentEt.text.append(text)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        WebSocketManager.close()
    }

}