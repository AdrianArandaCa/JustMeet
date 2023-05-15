package com.example.justmeet.Fragments

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.justmeet.Activitys.ActivityChat
import com.example.justmeet.Models.User
import com.example.justmeet.R

class InfoDialogMatch(private var user: User) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_infomatch, null)

        val photoUser = dialogView.findViewById<ImageView>(R.id.ivDetalleMatch)
        Glide.with(requireContext()).load(user.photo!!).into(photoUser)
        //photoUser.setImageResource(user.photo!!)

        val nameAgeUser = dialogView.findViewById<TextView>(R.id.tvNameEdadMatch)
        nameAgeUser.text = "${user.name!!},${user.birthday}"

        val descriptionUser = dialogView.findViewById<TextView>(R.id.tvDescriptionUserMatch)
        descriptionUser.text = "${user.description!!}"


        val buttonOpenChat = dialogView.findViewById<AppCompatButton>(R.id.btnOpenChat)
        buttonOpenChat.setOnClickListener {

            val intento = Intent(requireContext(), ActivityChat::class.java)
            intento.putExtra("User", user as java.io.Serializable)
            startActivity(intento)
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