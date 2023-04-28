package com.example.justmeet.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justmeet.Models.User
import com.example.justmeet.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

var job = Job()
class AdapterMatches(val llista : ArrayList<User>): RecyclerView.Adapter<AdapterMatches.viewholder>(),CoroutineScope {

    class viewholder(val view: View) : RecyclerView.ViewHolder(view) {
        var avatarUser = view.findViewById<ImageView>(R.id.ivAvatarMatch)
        var userName =  view.findViewById<TextView>(R.id.tvUserNameMatch)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layout = LayoutInflater.from(parent.context)
        return viewholder(layout.inflate(R.layout.cardview_matches, parent, false))
    }



    override fun onBindViewHolder(holder: viewholder, position: Int) {

        if(llista[position].photo == 0) {
            holder.avatarUser.setImageResource(R.drawable.logousernamegold)
        } else {
            holder.avatarUser.setImageResource(llista[position].photo!!)
        }
        holder.userName.setText(llista[position].name)

        holder.view.setOnClickListener {

        }
    }
    override fun getItemCount(): Int {
        return llista.size
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job
}