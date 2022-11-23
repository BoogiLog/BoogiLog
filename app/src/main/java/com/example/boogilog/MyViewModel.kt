package com.example.boogilog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    val key : MutableLiveData<String> = MutableLiveData<String>()

    init{
        key.value = ""
    }

    fun setKey(k : String) {
        key.value = k
    }
}