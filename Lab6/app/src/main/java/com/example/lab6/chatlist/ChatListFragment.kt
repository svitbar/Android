package com.example.lab6.chatlist

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.lab6.R
import com.example.lab6.chatroom.ChatRoomFragment
import com.example.lab6.databinding.FragmentChatListBinding
import com.example.lab6.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatListFragment : Fragment() {
    private lateinit var binding: FragmentChatListBinding
    private lateinit var chatListRecyclerViewAdapter: ChatListRecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)

        (activity as? AppCompatActivity)?.supportActionBar?.apply {
            title = "Chat"
            setDisplayHomeAsUpEnabled(false)
        }

        chatListRecyclerViewAdapter = ChatListRecyclerViewAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = chatListRecyclerViewAdapter

        readUsers()

        return binding.root
    }

    private fun readUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                snapshot.children.forEach { dataSnapshot ->
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let {
                        if (user.uid == FirebaseAuth.getInstance().uid) {
                            user.username = "Saved Messages"
                        }
                        userList.add(it)
                    }
                }

                chatListRecyclerViewAdapter.setOnItemClickListener { username ->
                    val selectedUser = userList.find { it.username == username }
                    if (selectedUser != null) {
                        val fragmentManager = requireActivity().supportFragmentManager

                        val args = Bundle().apply {
                            putString("userId", selectedUser.uid)
                            putString("username", selectedUser.username)
                            putString("email", selectedUser.email)
                        }

                        val chatRoomFragment = ChatRoomFragment.newInstance()
                        chatRoomFragment.arguments = args

                        fragmentManager
                            .beginTransaction()
                            .replace(R.id.chat_fragment_container, chatRoomFragment)
                            .addToBackStack(null)
                            .commit()
                    }
                }


                updateRecyclerView(userList)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatListFragment", "Failed to read value.", error.toException())
            }
        })
    }

    private fun updateRecyclerView(userList: List<User>) {
        val userNames = userList.map { it.username }
        chatListRecyclerViewAdapter.updateNames(userNames)
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChatListFragment()
    }
}