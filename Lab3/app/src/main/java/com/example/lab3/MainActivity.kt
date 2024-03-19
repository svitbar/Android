package com.example.lab3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.lab3.db.DBManager

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentSpinnerContainer, FragmentSpinner.newInstance())
            .commit()

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragmentResultContainer, FragmentResult.newInstance())
            .commit()

        val btn = findViewById<Button>(R.id.btnOpen)
        btn.setOnClickListener {
            val new = Intent(this, ResultActivity::class.java)
            startActivity(new)
        }
    }
}