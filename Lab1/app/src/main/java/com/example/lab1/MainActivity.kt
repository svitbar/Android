package com.example.lab1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val languages = arrayListOf("Java", "Kotlin", "JavaScript", "Python")

        val spinner = findViewById<Spinner>(R.id.spinner)
        val btnOk = findViewById<Button>(R.id.btnOk)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val text = findViewById<TextView>(R.id.text)

        val adapter = ArrayAdapter(this, R.layout.custom_spinner_item, languages) // android.R.layout.simple_spinner_item
        adapter.setDropDownViewResource(R.layout.custom_spinner_item) // android.R.layout.simple_spinner_dropdown_item
        spinner.adapter = adapter

        btnOk.setOnClickListener {
            val selected = spinner.selectedItem
            text.text = selected.toString()
        }

        btnCancel.setOnClickListener {
            text.text = ""
        }
    }
}