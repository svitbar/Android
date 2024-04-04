package com.example.lab4

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataModel: ViewModel() {
    val uri: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}