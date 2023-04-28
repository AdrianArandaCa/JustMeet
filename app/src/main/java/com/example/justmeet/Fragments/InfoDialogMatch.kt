package com.example.justmeet.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.example.justmeet.Models.User
import com.example.justmeet.R

class InfoDialogMatch(private var user : User) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_infomatch, null)

        val photoUser = dialogView.findViewById<ImageView>(R.id.ivDetalleMatch)
        photoUser.setImageResource(user.photo!!)

        val nameAgeUser = dialogView.findViewById<TextView>(R.id.tvNameEdadMatch)
        nameAgeUser.text = "${user.name!!},${user.birthday}"

        val descriptionUser = dialogView.findViewById<TextView>(R.id.tvDescriptionUserMatch)
        descriptionUser.text = "${user.description!!}"


        val buttonOpenChat = dialogView.findViewById<AppCompatButton>(R.id.btnOpenChat)
        buttonOpenChat.setOnClickListener {

            println("ABRIENDO CHAT")
        }
        val buttonBack = dialogView.findViewById<ImageButton>(R.id.btnBackDetailMatch)
        buttonBack.setOnClickListener {
            dialog?.dismiss()
        }

        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialog)
        return AlertDialog.Builder(requireActivity())
            .setView(dialogView)
            .create()
    }

}