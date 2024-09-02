package com.example.lab6.chatroom

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab6.R
import com.example.lab6.databinding.FragmentChatRoomBinding
import com.example.lab6.models.Message
import com.example.lab6.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import android.content.Context
import androidx.core.content.ContextCompat.getSystemService
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class ChatRoomFragment : Fragment() {
    private lateinit var binding: FragmentChatRoomBinding
    private lateinit var adapter: ChatRecyclerViewAdapter
    private lateinit var toUser: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatRoomBinding.inflate(inflater, container, false)

        val args = arguments
        val userId = args?.getString("userId")
        val toUsername = args?.getString("username")
        val email = args?.getString("email")
        toUser = User(userId!!, toUsername!!, email!!)

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = toUsername
            setDisplayHomeAsUpEnabled(true)
        }

        adapter = ChatRecyclerViewAdapter()
        binding.recyclerViewChatRoom.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewChatRoom.adapter = adapter

        binding.sendButton.setOnClickListener {
            if (binding.inputLine.text.isEmpty()) {
                Toast.makeText(requireContext(), "Fill the line", Toast.LENGTH_SHORT).show()
            } else {
                val messageText = binding.inputLine.text.toString()
                getCurrentUserUsername { username ->
                    val calendar = Calendar.getInstance()
                    val date = calendar.time
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
                    val formattedDate = dateFormat.format(date)

                    val user = FirebaseAuth.getInstance().currentUser
                    val userId = user?.uid ?: ""
                    val message = Message(userId, username, messageText, formattedDate)
                    sendMessage(message)
                }
                binding.inputLine.text.clear()
            }
        }

        readMessages()

        return binding.root
    }

    private fun sendMessage(message: Message) {
        val toId = toUser.uid
        val fromId = FirebaseAuth.getInstance().uid ?: return

        val reference = FirebaseDatabase.getInstance().getReference("/user-chats/$fromId/$toId").push()
        reference.setValue(message)
            .addOnSuccessListener {
                Log.d("ChatRoomFragment", "Message sent successfully")
            }
            .addOnFailureListener { e ->
                Log.e("ChatRoomFragment", "Error sending message", e)
            }

        if (toId != fromId) {
            val toReference = FirebaseDatabase.getInstance().getReference("/user-chats/$toId/$fromId").push()
            toReference.setValue(message)
        }
    }


    private fun readMessages() {
        val toId = toUser.uid
        val fromId = FirebaseAuth.getInstance().uid ?: return

        val ref = FirebaseDatabase.getInstance().getReference("/user-chats/$fromId/$toId")

        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val messageList = mutableListOf<Message>()
                var prevDate: String? = null

                for (dataSnapshot in snapshot.children) {
                    val message = dataSnapshot.getValue(Message::class.java)
                    message?.let {
                        val currentDate = formatDate(message.date)
                        if (prevDate == null || prevDate != currentDate) {
                            messageList.add(it)
                            prevDate = currentDate
                        }
                        messageList.add(it)
                    }
                }

                adapter.updateChat(messageList)
                binding.recyclerViewChatRoom.scrollToPosition(adapter.itemCount - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatRoomFragment", "Failed to read value.", error.toException())
            }
        })
    }


    private fun getCurrentUserUsername(callback: (String) -> Unit) {
        val currentUid = FirebaseAuth.getInstance().currentUser?.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                snapshot.children.forEach { dataSnapshot ->
                    val user = dataSnapshot.getValue(User::class.java)
                    Log.d("ChatRoomFragment", "User id ${user?.uid}")
                    if (user?.uid == currentUid) {
                        val username = user?.username ?: ""
                        Log.d("ChatRoomFragment", "Username is $username")
                        callback(username)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatRoomFragment", "Failed to read value.", error.toException())
            }
        })
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

    companion object {
        @JvmStatic
        fun newInstance() = ChatRoomFragment()
    }
}