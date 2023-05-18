package com.example.justmeet.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.justmeet.Fragments.InfoDialogMatch
import com.example.justmeet.Models.User
import com.example.justmeet.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

var job = Job()

class AdapterMatches(val llista: ArrayList<User>) :
    RecyclerView.Adapter<AdapterMatches.viewholder>(), CoroutineScope {

    class viewholder(val view: View) : RecyclerView.ViewHolder(view) {
        var avatarUser = view.findViewById<ImageView>(R.id.ivAvatarMatch)
        var userName = view.findViewById<TextView>(R.id.tvUserNameMatch)
        var isPremium = view.findViewById<ImageView>(R.id.ivCrownPremium)
        var isConnected = view.findViewById<ImageView>(R.id.ivStateConn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layout = LayoutInflater.from(parent.context)
        return viewholder(layout.inflate(R.layout.cardview_matches, parent, false))
    }


    override fun onBindViewHolder(holder: viewholder, position: Int) {

        if(llista[position].isConnected == true){
            holder.isConnected.setImageResource(R.drawable.circle_isconnected)
        } else {
            holder.isConnected.setImageResource(R.drawable.circle_notconnected)
        }

        if (llista[position].photo == "0") {
            Glide.with(holder.avatarUser.context)
                .load("https://cdn.create.vista.com/api/media/small/471012004/stock-vector-arab-gold-plated-metalic-icon")
                .into(holder.avatarUser)
            //  holder.avatarUser.setImageResource(R.drawable.logousernamegold)
        } else {
            Glide.with(holder.avatarUser.context).load(llista[position].photo!!)
                .into(holder.avatarUser)
            //holder.avatarUser.setImageResource(llista[position].photo!!)
        }
        holder.userName.setText(llista[position].name)
        if (llista[position].premium!!) {
            holder.isPremium.isVisible = true
            holder.isPremium.setImageResource(R.drawable.crownplane)
        }

        holder.view.setOnClickListener {
            val fragmentManager =
                (holder.itemView.context as AppCompatActivity).supportFragmentManager
            val infoDialog = InfoDialogMatch(llista[position])

            infoDialog.show(fragmentManager, "InfoDialog")
        }
    }

    override fun getItemCount(): Int {
        return llista.size
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}