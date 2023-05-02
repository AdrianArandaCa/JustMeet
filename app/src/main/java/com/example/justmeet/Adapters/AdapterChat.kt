package com.example.justmeet.Adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.justmeet.Models.Answer
import com.example.justmeet.Models.Chat
import com.example.justmeet.R
import org.w3c.dom.Text

class AdapterChat(var list: java.util.ArrayList<Chat>) :
    RecyclerView.Adapter<AdapterChat.viewholder>() {

    class viewholder(val view: View) : RecyclerView.ViewHolder(view) {
        val message = view.findViewById<TextView>(R.id.tvMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layout = LayoutInflater.from(parent.context)
        when (viewType) {
            0 -> return viewholder(layout.inflate(R.layout.cardview_chatmatch, parent, false))
            1 -> return viewholder(layout.inflate(R.layout.cardview_chatlocal, parent, false))
        }
        return viewholder(layout.inflate(R.layout.cardviewanswer, parent, false))
    }

    //Enllaça cadascun dels elements individuals (cardview) amb els elements de la list
    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.message.setText("${list[position].message}")

    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].isUserLogged.toString().toInt()

    fun updateDades(newList: ArrayList<Chat>) {
        list = newList
        notifyDataSetChanged()
    }
}