package com.example.lab6

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.lab6.chatlist.ChatListFragment
import com.example.lab6.chatroom.ChatRoomFragment
import com.example.lab6.databinding.ActivityChatBinding
import com.example.lab6.models.User
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        isUserLoggedIn()

        if (intent != null && intent.action == "OPEN_CHAT_ROOM_FRAGMENT") {
            val userId = intent.getStringExtra("USER_ID")
            readUsers(userId!!)
        } else {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.chat_fragment_container, ChatListFragment.newInstance())
                .commit()
        }
    }

    private fun readUsers(userId: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userList = mutableListOf<User>()
                snapshot.children.forEach { dataSnapshot ->
                    val user = dataSnapshot.getValue(User::class.java)
                    user?.let {
                        userList.add(it)
                    }
                }

                userList.forEach { _ ->
                    val selectedUser = userList.find { it.uid == userId }
                    if (selectedUser != null) {
                        Log.d("FindUserByClick", "Find user with id ${selectedUser.uid} and name ${selectedUser.username}")
                        val fragmentManager = supportFragmentManager

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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w("ChatListFragment", "Failed to read value.", error.toException())
            }
        })
    }

    private fun isUserLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid

        if (uid == null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {/*
            R.id.menu_new_mess -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.chat_fragment_container, UsersListFragment.newInstance())
                    .addToBackStack(null)
                    .commit()
            }*/

            android.R.id.home -> {
                supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.chat_fragment_container, ChatListFragment.newInstance())
                    .commit()
                return true
            }

            R.id.menu_sign_out -> {
                Firebase.auth.signOut()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back);
        return super.onCreateOptionsMenu(menu)
    }
}
