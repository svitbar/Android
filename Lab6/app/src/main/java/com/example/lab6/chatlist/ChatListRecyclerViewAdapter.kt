package com.example.lab6.chatlist

import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.lab6.R
import com.example.lab6.databinding.ItemNameBinding
import com.google.firebase.auth.FirebaseAuth

class ChatListRecyclerViewAdapter(private val names: MutableList<String> = mutableListOf()) :
    RecyclerView.Adapter<ChatListRecyclerViewAdapter.ViewHolder>() {

    private var onItemClick: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClick = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_name, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val name = names[position]
        holder.binding.nameTextView.text = name
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(name)
        }
    }

    override fun getItemCount(): Int {
        return names.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ItemNameBinding = ItemNameBinding.bind(itemView)
    }

    fun updateNames(newNames: List<String>) {
        names.clear()
        names.addAll(newNames)
        notifyDataSetChanged()    }
}

