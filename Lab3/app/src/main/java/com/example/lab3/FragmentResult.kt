package com.example.lab3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer

class FragmentResult : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val btnCancel = view.findViewById<Button>(R.id.btnCancel)
        val text = view.findViewById<TextView>(R.id.text)

        dataModel.message.observe(viewLifecycleOwner, Observer {it ->
            text.text = it
        })

        btnCancel.setOnClickListener {
            text.text = ""
            dataModel.isCancel.value = true
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentResult()
    }
}