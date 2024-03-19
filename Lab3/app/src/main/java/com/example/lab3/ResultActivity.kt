package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.lab3.db.DBManager

class ResultActivity : AppCompatActivity() {
    private lateinit var dbManager: DBManager
    private lateinit var adapter: MyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            Log.d("ResultActivity", "Back button clicked")
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val btnDelete = findViewById<Button>(R.id.btnDelete)
        btnDelete.setOnClickListener {
            dbManager.deleteDbData()
            showDb()
        }

        showDb()
    }

    private fun showDb() {
        dbManager = DBManager(this)
        val dataList = dbManager.readDbData()

        adapter = MyAdapter(dataList)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        if (adapter.itemCount == 0) {
            Toast.makeText(this, "There is no record", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.updateAdapter(dbManager.readDbData())
    }

    override fun onDestroy() {
        super.onDestroy()
        dbManager.closeDb()
    }
}