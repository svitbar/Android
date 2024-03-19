package com.example.lab3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.fragment.app.activityViewModels
import com.example.lab3.db.DBManager

class FragmentSpinner : Fragment() {
    private val dataModel: DataModel by activityViewModels()
    private lateinit var dbManager: DBManager

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dbManager = DBManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_spinner, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val languages = arrayListOf("Java", "Kotlin", "JavaScript", "Python")

        val spinner = view.findViewById<Spinner>(R.id.spinner)
        val btnOk = view.findViewById<Button>(R.id.btnOk)

        val adapter = ArrayAdapter(requireContext(), R.layout.custom_spinner_item, languages) // android.R.layout.simple_spinner_item
        adapter.setDropDownViewResource(R.layout.custom_spinner_item) // android.R.layout.simple_spinner_dropdown_item
        spinner.adapter = adapter

        btnOk.setOnClickListener {
            val selected = spinner.selectedItem
            dataModel.message.value = selected.toString()
            dbManager.insertToDb(selected.toString())
        }

        dataModel.isCancel.observe(viewLifecycleOwner) {
            spinner.setSelection(0)
        }
    }

    override fun onResume() {
        super.onResume()
        dbManager.openDbForWrite()
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }

    companion object {
        @JvmStatic
        fun newInstance() = FragmentSpinner()
    }
}