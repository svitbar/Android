package com.example.lab2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

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
    }
}