package com.example.boogilog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel() {
    var countLiveData: MutableLiveData<Int> = MutableLiveData<Int>()

    init{
        countLiveData.value = 0
    }

    fun increaseCount(){
        countLiveData.value = (countLiveData.value ?: 0) + 1
    }
}