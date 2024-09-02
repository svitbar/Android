package com.example.lab6.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.lab6.R
import com.example.lab6.databinding.FragmentLaunchBinding

class LaunchFragment : Fragment() {
    private lateinit var binding: FragmentLaunchBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchBinding.inflate(inflater, container, false)
        val fragmentManager = requireActivity().supportFragmentManager

        binding.signIn.setOnClickListener {
            fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SignInFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        binding.signUp.setOnClickListener {
            fragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, SignUpFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = LaunchFragment()
    }
}