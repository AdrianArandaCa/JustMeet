package com.example.justmeet.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justmeet.Models.Avatar
import com.example.justmeet.R

class AdapterAvatar(val llista : ArrayList<Avatar>) : RecyclerView.Adapter<AdapterAvatar.viewholder>() {

    class viewholder(val view : View) : RecyclerView.ViewHolder(view) {
        val img = view.findViewById<ImageView>(R.id.ivAvatar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layout = LayoutInflater.from(parent.context)
        if(viewType==1){
            return viewholder(layout.inflate(R.layout.cardviews_avataresselected, parent, false))
        } else {
            return viewholder(layout.inflate(R.layout.cardview_avatares, parent, false))
        }

    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        Glide.with(holder.img.context).load(llista[position].resourcePhoto).into(holder.img)
        //holder.img.setImageResource(llista[position].resourcePhoto)
        holder.view.setOnClickListener {
            for (i in 0..llista.size - 1) {
                if (i == position) {
                    if (llista[i].selected == 0) {
                        llista[i].selected = 1
                    } else {
                        llista[i].selected = 0
                    }
                } else {
                    llista[i].selected = 0
                }
            }

            notifyDataSetChanged()
        }

    }

    override fun getItemCount(): Int {
        return llista.size
    }

    override fun getItemViewType(position: Int) = llista[position].selected
}