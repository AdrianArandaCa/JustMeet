package com.example.justmeet.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.justmeet.Models.Avatar
import com.example.justmeet.R

class AdapterAvatar(val llista : ArrayList<Avatar>) : RecyclerView.Adapter<AdapterAvatar.viewholder>() {

    class viewholder(val view : View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layout = LayoutInflater.from(parent.context)
        return viewholder(layout.inflate(R.layout.cardview_avatares, parent, false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.img.setImageResource(llista[position].resourcePhoto)

    }

    override fun getItemCount(): Int {
        return llista.size
    }
}