package com.example.justmeet.Fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.justmeet.Activitys.*
import com.example.justmeet.Models.*
import com.example.justmeet.R
import com.example.justmeet.Socket.MessageListener
import com.example.justmeet.Socket.WebSocketManager
import com.example.justmeet.databinding.FragmentPlayBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlin.concurrent.thread
import kotlin.random.Random

private lateinit var binding: FragmentPlayBinding

class PlayFragment : Fragment(), MessageListener {
    var isFinding: Boolean = false
    private lateinit var focusAnimation: AnimatorSet
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentPlayBinding.inflate(inflater, container, false)
        val serverUrl = "ws://172.16.24.24:45456/ws/${userLog!!.idUser}"
        binding.btnPlay.setOnClickListener {

            if (!isFinding) {
                // Find game
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
                    binding.tvStateGame.isVisible = true
                    binding.tvStateGame.setText(getString(R.string.finding_game))
                    isFinding = true
                    animatePlayButton()
                }
            } else {
                // Cancel a game
                WebSocketManager.sendMessage("CLOSE")
                binding.tvStateGame.setText(getString(R.string.main_click))
                isFinding = false
                // Reset button animation
                if (focusAnimation.isRunning) {
                    focusAnimation.cancel()
                    focusAnimation.end()
                    binding.btnPlay.alpha = 1.0f
                    binding.btnPlay.scaleX = 1.0f
                    binding.btnPlay.scaleY = 1.0f
                    binding.btnPlay.rotation = 0.0f
                    return@setOnClickListener
                }
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
            // Return message from Web Socket to close connection
            if (text.startsWith("CLOSE")) {
                if (text == "CLOSEMATCH") {
                    println("CLOSEMATCH")
                } else if (text == "CLOSEAGE") {
                    println("CLOSEAGE")
                } else if (text == "CLOSEGENRE") {
                    println("CLOSEGENRE")
                } else if (text == "CLOSEGAMETYPE") {
                    println("CLOSEGAMETYPE")
                } else if (text == "CLOSEDISTANCE") {
                    println("CLOSEDISTANCE")
                }
                // Close web socket connection
                thread {
                    kotlin.run {
                        WebSocketManager.sendMessage("CLOSE")
                    }
                }
                // Dialog to show a message when user dont accomplish the requirements
                activity?.runOnUiThread {
                    val builder = AlertDialog.Builder(context)
                    builder.setTitle(getString(R.string.warning))
                    builder.setMessage(getString(R.string.no_coincidence))
                    builder.setPositiveButton(getString(R.string.keep_looking)) { dialog, which ->
                        binding.tvStateGame.setText(getString(R.string.main_click))
                        // Reset animation
                        if (focusAnimation.isRunning) {
                            focusAnimation.cancel()
                            focusAnimation.end()
                            binding.btnPlay.alpha = 1.0f
                            binding.btnPlay.scaleX = 1.0f
                            binding.btnPlay.scaleY = 1.0f
                            binding.btnPlay.rotation = 0.0f
                        }
                        isFinding = false

                    }
                    val dialog = builder.create()
                    dialog.show()
                }
                // When Game is finish, web socket send a GameResult and us check if true or not
            } else if (text.startsWith("GAMERESULT")) {
                var textSubstring = text.substring(10)
                val gameType = object : TypeToken<Game>() {}.type
                gameFinishFromSocket = gson.fromJson(textSubstring, gameType)
                if (gameFinishFromSocket.match == true) {
                    val intento = Intent(requireContext(), ActivityResumeIsMatch::class.java)
                    startActivity(intento)
                } else {
                    val intento = Intent(requireContext(), ActivityResumeNotMatch::class.java)
                    startActivity(intento)
                }
                // Socket send a number of game when game started
            } else if (text.startsWith("Game", false)) {
                var textSubstring = text.substring(4)
                val listType = object : TypeToken<Game>() {}.type
                gameFromSocket = gson.fromJson(textSubstring, listType)
                addText(" Receive message: $text \n ")
                // When user leave a game, web socket send a message
            } else if (text.startsWith("USERGAMELEAVE", false)) {
                activity?.runOnUiThread {
                    userGameLeave = true
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.user_leave_game),
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(requireContext(), BottomNavigationActivity::class.java)
                    startActivity(intent)
                }

            } else {
                // Web socket send a list of questions with answers and init the game
                val listType = object : TypeToken<ArrayList<Question>>() {}.type
                listQuestion = gson.fromJson(text, listType)
                isSucces = true
                activity?.runOnUiThread {
                    val intent = Intent(requireContext(), GameActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun addText(text: String?) {
        activity?.runOnUiThread {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onStop() {
        super.onStop()
        if(!isSucces) {
            WebSocketManager.sendMessage("CLOSE")
        }
    }

    // Button animation when find a game
    fun animatePlayButton() {
        val scaleDownX = ObjectAnimator.ofFloat(binding.btnPlay, "scaleX", 2.0f)
        val scaleDownY = ObjectAnimator.ofFloat(binding.btnPlay, "scaleY", 2.0f)
        scaleDownX.duration = 2500
        scaleDownY.duration = 2500

        val scaleUpX = ObjectAnimator.ofFloat(binding.btnPlay, "scaleX", 10f)
        val scaleUpY = ObjectAnimator.ofFloat(binding.btnPlay, "scaleY", 10f)
        scaleUpX.duration = 1000
        scaleUpY.duration = 1000
        focusAnimation = AnimatorSet()
        focusAnimation.play(scaleDownX).with(scaleDownY)
        focusAnimation.play(scaleUpX).with(scaleUpY).after(scaleDownX)

        focusAnimation.interpolator = AccelerateDecelerateInterpolator()
        scaleDownX.repeatCount = ValueAnimator.INFINITE
        scaleDownY.repeatCount = ValueAnimator.INFINITE
        scaleUpX.repeatCount = ValueAnimator.INFINITE
        scaleUpY.repeatCount = ValueAnimator.INFINITE

        focusAnimation.start()
    }
}