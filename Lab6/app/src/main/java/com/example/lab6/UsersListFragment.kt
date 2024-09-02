package com.example.lab6

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab6.databinding.FragmentUsersListBinding

class UsersListFragment : Fragment() {
    private lateinit var binding: FragmentUsersListBinding
    // private lateinit var recyclerViewAdapter: RecyclerViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentUsersListBinding.inflate(inflater, container, false)

/*        recyclerViewAdapter = RecyclerViewAdapter()

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = recyclerViewAdapter

        readUsers()*/

        return binding.root
    }

/*    private fun readUsers() {
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

                recyclerViewAdapter.setOnItemClickListener { username ->
                    Toast.makeText(requireContext(), "Clicked on user with ID: $username", Toast.LENGTH_SHORT).show()

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
                Log.w("UsersListFragment", "Failed to read value.", error.toException())
            }
        })
    }

    private fun updateRecyclerView(userList: List<User>) {
        val userNames = userList.map { it.username }
        recyclerViewAdapter.updateNames(userNames)
    }*/

    companion object {
        @JvmStatic
        fun newInstance() = UsersListFragment()
    }
}