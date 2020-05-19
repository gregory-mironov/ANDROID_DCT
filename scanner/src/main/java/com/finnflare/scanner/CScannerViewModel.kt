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

    var planItemsList = mutableListOf<Item>()
    var planItemsListFound = mutableListOf<Item>()

    var factItemsListCorrect = mutableListOf<Item>()
    var factItemsListWrong = mutableListOf<Item>()

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

    fun getItemsLists() {
        planItemsList.clear()
        planItemsListFound.clear()

        factItemsListCorrect.clear()
        factItemsListWrong.clear()

        for (i in 1..25) {
            planItemsList.add(Item("Plan item $i", "Some content"))
            planItemsListFound.add(Item("Found plan item $i", "Some content"))

            factItemsListCorrect.add(Item("Correct fact item $i", "Some content"))
            factItemsListWrong.add(Item("Wrong fact item $i", "Some content"))
        }
    }
}

data class Item(val description: String, val content: String)