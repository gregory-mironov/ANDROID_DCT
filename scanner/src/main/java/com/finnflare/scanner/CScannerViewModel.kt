package com.finnflare.scanner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import org.koin.core.KoinComponent

class CScannerViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    val scanResult = MutableLiveData<Triple<String, String, String>>()
    val scanError = MutableLiveData<String>()
}