package com.example.lab2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DataModel: ViewModel() {
    val message: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    val isCancel: MutableLiveData<Boolean> by lazy {
        MutableLiveData<Boolean>()
    }
}