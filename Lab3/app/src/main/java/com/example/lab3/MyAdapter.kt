package com.example.lab3

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val list: ArrayList<String>): RecyclerView.Adapter<MyAdapter.MyHolder>() {
    class MyHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val text: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyHolder(inflater.inflate(R.layout.item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.text.text = list[position]
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateAdapter(listItems: ArrayList<String>) {
        list.clear()
        list.addAll(listItems)
    }
}