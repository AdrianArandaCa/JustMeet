package com.example.justmeet.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.justmeet.Activitys.ActivityLocation
import com.example.justmeet.Activitys.ActivityResumeIsMatch
import com.example.justmeet.Activitys.ActivityResumeNotMatch
import com.example.justmeet.Activitys.GameActivity
import com.example.justmeet.Models.*
import com.example.justmeet.Socket.MessageListener
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.FragmentPlayBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.concurrent.thread
import kotlin.random.Random

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

private lateinit var binding: FragmentPlayBinding

class PlayFragment : Fragment(), MessageListener {
var isSucces : Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        val serverUrl = "ws://172.16.24.123:45456/ws/${userLog!!.idUser}"
        binding.btnBuscarPartida.setOnClickListener {
            thread {
                kotlin.run {
                    WebSocketManager.init(serverUrl, this)
                    WebSocketManager.connect()
                    val randomDelay = Random.nextInt(400, 500)
                    Thread.sleep(randomDelay.toLong())
                    WebSocketManager.sendMessage("STARTGAME")
                }
            }
            activity?.runOnUiThread {
                binding.btnBuscarPartida.setText("Buscando partida...")
                binding.progressBarPlay.visibility = View.VISIBLE
            }
        }

       binding.btnIntentLocation.setOnClickListener {

           val intento = Intent(requireContext(), ActivityLocation::class.java)
           startActivity(intento)
       }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onConnectSuccess() {
        println("on connectSucces")

    }

    override fun onConnectFailed() {
        println("on Connect Failed")
    }

    override fun onClose() {
        println("WEB SOCKET CERRADO PUTO")
    }

    override fun onMessage(text: String?) {
        val gson = Gson()

        if (text != null) {
            if (text.startsWith("CLOSE")) {
                if(text == "CLOSEMATCH") {
                    println("CLOSEMATCH")
                } else if(text == "CLOSEAGE") {
                    println("CLOSEAGE")
                } else if(text == "CLOSEGENRE"){
                    println("CLOSEGENRE")
                } else if(text == "CLOSEGAMETYPE"){
                    println("CLOSEGAMETYPE")
                }
                hideProgressBar()
                thread {
                    kotlin.run {
                            WebSocketManager.sendMessage("CLOSE")
                    }
                }
            }
            else if (text.startsWith("GAMERESULT")) {
                var textSubstring = text.substring(10)
                val gameType = object : TypeToken<Game>() {}.type
                gameFinishFromSocket = gson.fromJson(textSubstring, gameType)
                println("GAME RESULT :" + gameFinishFromSocket.idGame)
                if (gameFinishFromSocket.match == true) {
                    val intento = Intent(requireContext(), ActivityResumeIsMatch::class.java)
                    startActivity(intento)
                } else {
                    //WebSocketManager.sendMessage("close")
                    //WebSocketManager.close()
                    val intento = Intent(requireContext(), ActivityResumeNotMatch::class.java)
                    startActivity(intento)
                }
            } else if (text.startsWith("Game", false)) {
                var textSubstring = text.substring(4)
                val listType = object : TypeToken<Game>() {}.type
                gameFromSocket = gson.fromJson(textSubstring, listType)
                println("ID GAME INICIO :" + gameFromSocket.idGame)
                addText(" Receive message: $text \n ")
            } else {
                val listType = object : TypeToken<ArrayList<Question>>() {}.type
                listQuestion = gson.fromJson(text, listType)
                activity?.runOnUiThread {
                    binding.progressBarPlay.visibility = View.GONE
                    val intent = Intent(requireContext(), GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun hideProgressBar() {
        activity?.runOnUiThread {
            binding.progressBarPlay.visibility = View.GONE
            binding.btnBuscarPartida.setText("Buscar partida")
        }
    }

    private fun addText(text: String?) {
        activity?.runOnUiThread {

        }
    }

    override fun onDestroy() {
        //WebSocketManager.close()
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        WebSocketManager.sendMessage("CLOSE")
    }
    override fun onStop() {
        super.onStop()
        WebSocketManager.sendMessage("CLOSE")
    }


}