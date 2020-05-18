package com.finnflare.scanner

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.finnflare.scanner.alien.CAlienScanner
import com.finnflare.scanner.camera.CCameraScanner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent

class CScannerViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    val scanResult = MutableLiveData<Triple<String, String, String>>()
    val scanError = MutableLiveData<String>()

    lateinit var scanner: IScanner

    init {
        CoroutineScope(Dispatchers.Default).launch {
            when (Build.MANUFACTURER) {
                "Alien" -> {
                    if (Build.MODEL == "ALR-H450")
                        scanner = CAlienScanner(application)
                }
                else -> scanner = CCameraScanner(application)
            }

            scanner.init()
        }
    }
}