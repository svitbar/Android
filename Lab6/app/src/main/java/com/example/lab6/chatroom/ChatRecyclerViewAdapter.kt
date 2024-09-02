package com.example.lab6.chatroom

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.lab6.R
import com.example.lab6.databinding.DateChatViewBinding
import com.example.lab6.databinding.ReceiveMessageBinding
import com.example.lab6.databinding.SendMessageBinding
import com.example.lab6.models.Message
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatRecyclerViewAdapter(private var messages: MutableList<Message> = mutableListOf()) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_SEND = 1
    private val ITEM_RECEIVE = 2
    private val ITEM_DATE = 3

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_SEND -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.send_message, parent, false)
                SendMessageViewHolder(view)
            }
            ITEM_RECEIVE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.receive_message, parent, false)
                ReceiveMessageViewHolder(view)
            }
            ITEM_DATE -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.date_chat_view, parent, false)
                DateHeaderViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 || isDifferentDate(messages[position].date, messages[position - 1].date)) {
            ITEM_DATE
        } else {
            if (FirebaseAuth.getInstance().uid == messages[position].id) {
                ITEM_SEND
            } else {
                ITEM_RECEIVE
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messages[position]
        when (holder) {
            is SendMessageViewHolder -> {
                holder.binding.sendText.text = message.message
                holder.binding.messageTime.text = formatTime(message.date)
            }
            is ReceiveMessageViewHolder -> {
                holder.binding.receiveText.text = message.message
                holder.binding.messageTime.text = formatTime(message.date)
            }
            is DateHeaderViewHolder -> {
                holder.binding.dateText.text = formatDate(message.date)
            }
        }
    }

/*    private fun makeUI(messageTextView: TextView, timeTextView: TextView?, message: String, date: String) {
        val maxLength = 240
        val messageLength = message.length
        val timeLength = formatTime(date).length

        val maxMessageLength = maxLength - timeLength

        messageTextView.maxWidth = maxMessageLength

        val params = messageTextView.layoutParams as LinearLayout.LayoutParams
        params.width = ViewGroup.LayoutParams.WRAP_CONTENT // Reset width to wrap_content
        params.weight = 0f // Reset weight to 0
        messageTextView.layoutParams = params

        // If the message length exceeds the available space, adjust the layout of the message and time TextViews
        if (messageLength > maxMessageLength) {
            // Set the width of the message TextView to match_parent to allow it to wrap and display on multiple lines
            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            params.weight = 1f // Set weight to 1 to allow the message TextView to expand
            messageTextView.layoutParams = params

            // Hide the time TextView if available
            timeTextView?.visibility = View.GONE
        } else {
            // Show the time TextView if available
            timeTextView?.visibility = View.VISIBLE
        }
    }*/

    override fun getItemCount(): Int {
        return messages.size
    }

    private fun formatTime(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateTimeString)

        date?.let {
            val outputFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

            return outputFormat.format(date)
        }

        return ""
    }

    private fun formatDate(dateTimeString: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date: Date? = inputFormat.parse(dateTimeString)

        date?.let {
            val outputFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

            return outputFormat.format(date)
        }

        return ""
    }

    private fun isDifferentDate(date1: String, date2: String): Boolean {
        val formattedDate1 = formatDate(date1)
        val formattedDate2 = formatDate(date2)

        return formattedDate1 != formattedDate2
    }

    fun addMessage(message: Message) {
        messages.add(message)
        notifyItemInserted(messages.size - 1)
    }

    fun updateChat(mess: List<Message>) {
        messages.clear()
        messages.addAll(mess)
        notifyDataSetChanged()
    }

    inner class SendMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: SendMessageBinding = SendMessageBinding.bind(itemView)
    }

    inner class ReceiveMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: ReceiveMessageBinding = ReceiveMessageBinding.bind(itemView)
    }

    inner class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var binding: DateChatViewBinding = DateChatViewBinding.bind(itemView)
    }

    init {
        this.messages = messages
    }
}