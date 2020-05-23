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
import java.util.*

class CScannerViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    val scanResult = MutableLiveData<Triple<String, String, String>>()
    val scanError = MutableLiveData<String>()

    var rfidRSSI = 0.0

    lateinit var scanner: IScanner

    private val markingCodeList = mutableListOf<MarkingCode>()

    private val stateList = mutableListOf<State>()

    private val searchList = mutableListOf<Item>()

    private val planItemsList = mutableListOf<Item>()
    private val factItemsList = mutableListOf<Item>()

    init { CoroutineScope(Dispatchers.Default).launch {
        when (Build.MANUFACTURER) {
            "Alien" -> {
                if (Build.MODEL == "ALR-H450")
                    scanner = CAlienScanner(application)
            }
            else -> scanner = CCameraScanner(application)
        }
    }
    }

    fun updateItemsLists() {
        
        planItemsList.clear()

        factItemsList.clear()

        for (i in 1..25) {
            planItemsList.add(Item(" ", "Plan item $i", "Some color",
                "Some size", 0, 0, 10)
            )
            planItemsList.add(Item(" ", "Found plan item $i", "Some color",
                "Some size", 10, 0, 10)
            )

            factItemsList.add(Item(" ", "Correct fact item $i", "Some color",
                "Some size", 0, 9, 10)
            )
            factItemsList.add(Item(
                " ", "Wrong fact item $i", "Some color",
                "Some size", 11, 0, 10)
            )
        }
    }

    fun getPlanListNotFound() = planItemsList.filter {
        it.description.contains("Plan item")
    }

    fun getPlanListFound() = planItemsList.filter {
        it.description.contains("Found plan item")
    }

    fun getCorrectFactList() = factItemsList.filter {
        it.description.contains("Correct fact item")
    }

    fun getWrongFactList() = factItemsList.filter {
        it.description.contains("Wrong fact item")
    }

    fun increaseItemCount(gtin: String, sn: String, rfid: String): Int {

        //add DB action and remote check

        val mc = markingCodeList.find { it.gtin == gtin } ?: return -1

        val item = factItemsList.find { it.guid == mc.guid } ?: return -1

        if (rfid.isNotEmpty()) {
            item.rfidCount++
            return if (item.rfidCount > item.planCount) 1 else 0
        }

        item.barcodeCount++
        return if (item.barcodeCount > item.planCount) 1 else 0
    }

    fun getItemData(gtin: String, sn: String = "", rfid: String = ""):
            Pair<String, Triple<String, String, String>> {

        val mc = markingCodeList.filter { it.gtin == gtin }

        if (mc.isEmpty())
            return findItemDataFromRemote()

        val items = planItemsList.filter { it.guid == mc[0].guid}

        if (items.isEmpty())
            return findItemDataFromRemote()

        items[0].let { item ->
            stateList.filter { it.state_id == mc[0].state }

            return Pair(item.description, Triple(
                item.color,
                item.size,
                if (stateList.isNotEmpty()) stateList[0].state_name else "" )
            )
        }
    }

    private fun findItemDataFromRemote(): Pair<String, Triple<String, String, String>> {
        return Pair("", Triple("", "", ""))
    }

    fun updateSearchList(mask: String) {
        searchList.clear()
        searchList.addAll(
            planItemsList.filter { e -> e.description
                .toLowerCase(Locale.getDefault())
                .contains(mask.toLowerCase(Locale.getDefault()))}
        )
        searchList.addAll(
            factItemsList.filter { e -> e.description
                .toLowerCase(Locale.getDefault())
                .contains(mask.toLowerCase(Locale.getDefault()))}
        )
    }

    fun cmpScanResWithSearchList(gtin: String, sn: String, rfid: String): Boolean {
        if (rfid.isEmpty())
            return false

        val mc = markingCodeList.filter { it.gtin == gtin }

        if (mc.isEmpty())
            return false

        return searchList.any { e -> mc.any { it.guid == e.guid } }
    }
}

data class MarkingCode (
    val guid: String,
    val gtin: String,
    val sn: String,
    val rfid: String,
    val state: String
)

data class Item(
    val guid: String,
    val description: String,
    val color: String,
    val size: String,
    var barcodeCount: Int = 0,
    var rfidCount: Int = 0,
    val planCount: Int = 0
)

data class State(
    val state_id: String,
    val state_name: String
)