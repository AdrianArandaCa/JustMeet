package com.example.justmeet.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.justmeet.Models.Answer
import com.example.justmeet.R

class AdapterAnswer(var list: java.util.ArrayList<Answer>) :
    RecyclerView.Adapter<AdapterAnswer.viewholder>() {

    class viewholder(val view: View) : RecyclerView.ViewHolder(view) {
        val answer = view.findViewById<TextView>(R.id.txtAnswer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val layout = LayoutInflater.from(parent.context)
        when (viewType) {
            0 -> return viewholder(layout.inflate(R.layout.cardviewanswer, parent, false))
            1 -> return viewholder(layout.inflate(R.layout.cardviewanswerselected, parent, false))
        }
        return viewholder(layout.inflate(R.layout.cardviewanswer, parent, false))
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.answer.setText("${list[position].answer1}")
        holder.view.setOnClickListener {
            for (i in 0..list.size - 1) {
                if (i == position) {
                    if (list[i].selected == 0) {
                        list[i].selected = 1
                    } else {
                        list[i].selected = 0
                    }
                } else {
                    list[i].selected = 0
                }
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = list.size

    override fun getItemViewType(position: Int) = list[position].selected.toInt()

    fun updateDades(newList: ArrayList<Answer>) {
        list = newList
        notifyDataSetChanged()
    }
}