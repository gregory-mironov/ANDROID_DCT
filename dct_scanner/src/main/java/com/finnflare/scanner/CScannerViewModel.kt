package com.finnflare.scanner

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import com.finnflare.scanner.alien.CAlienScanner
import com.finnflare.scanner.camera.CCameraScanner
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.util.*

@ObsoleteCoroutinesApi
class CScannerViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    val scannerDispatcher: CoroutineDispatcher = newSingleThreadContext("DBCoroutine")
    private val database by inject<CDatabaseViewModel>()
    private val network by inject<CNetworkViewModel>()

    val scanResult = MutableLiveData<Triple<String, String, String>>()
    val scanError = MutableLiveData<String>()

    var rfidRSSI = 0.0

    lateinit var scanner: IScanner

    var docId = ""

    private val markingCodeList = mutableListOf<MarkingCode>()

    private val stateList = mutableListOf<State>()

    private val searchList = mutableListOf<Item>()

    val planItemsList = MutableLiveData<MutableList<Item>>(mutableListOf())
    val factItemsList = MutableLiveData<MutableList<Item>>(mutableListOf())

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

    suspend fun getItemsList() {
        CoroutineScope(database.dbDispatcher).launch {
            planItemsList.value?.clear()
            factItemsList.value?.clear()

            database.getLeftovers(docId).forEach {
                val item = Item(it.guid, it.description, it.model, it.color,
                    it.size, it.qtyoutBarcode, it.qtyoutRfid, it.qtyin)

                factItemsList.value?.add(item)
                if (it.qtyin != 0)
                    planItemsList.value?.add(item)
            }
            planItemsList.postValue(planItemsList.value)
            factItemsList.postValue(factItemsList.value)

//            planItemsList.value?.let {
//                for (i in 1..25) {
//                    it.add(
//                        Item(
//                            " ", "Plan item $i", "","Some color",
//                            "Some size", 0, 0, 10
//                        )
//                    )
//                    it.add(
//                        Item(
//                            " ", "Found plan item $i", "", "Some color",
//                            "Some size", 10, 0, 10
//                        )
//                    )
//                }
//            }
//            planItemsList.postValue(planItemsList.value)
//
//            factItemsList.value?.let {
//                it.clear()
//                for (i in 1..25) {
//                    it.add(
//                        Item(
//                            " ", "Correct fact item $i", "", "Some color",
//                            "Some size", 0, 9, 10
//                        )
//                    )
//                    it.add(
//                        Item(
//                            " ", "Wrong fact item $i", "", "Some color",
//                            "Some size", 11, 0, 10
//                        )
//                    )
//                }
//            }
//            factItemsList.postValue(factItemsList.value)
        }.join()
    }

    suspend fun refreshItemsList() {
        network.getStocksList(docId)
        getItemsList()
    }

    fun getPlanListNotFound() = planItemsList.value!!.filter {
        it.planCount > 0 && it.barcodeCount == 0 && it.rfidCount == 0
    }

    fun getPlanListFound() = planItemsList.value!!.filter {
        it.planCount > 0 && (it.barcodeCount > 0 || it.rfidCount > 0)
    }

    fun getCorrectFactList() = factItemsList.value!!.filter {
        (it.barcodeCount <= it.planCount && it.barcodeCount > 0) ||
                (it.rfidCount <= it.planCount && it.rfidCount > 0)
    }

    fun getWrongFactList() = factItemsList.value!!.filter {
        it.barcodeCount > it.planCount || it.rfidCount > it.planCount
    }

    fun increaseItemCount(gtin: String, sn: String, rfid: String): Int {

        //add DB action and remote check

        val mc = markingCodeList.find { it.gtin == gtin } ?: return -1

        val item = factItemsList.value!!.find { it.guid == mc.guid } ?: return -1

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

        val items = planItemsList.value!!.filter { it.guid == mc[0].guid}

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
            planItemsList.value!!.filter { e -> e.description
                .toLowerCase(Locale.getDefault())
                .contains(mask.toLowerCase(Locale.getDefault()))}
        )
        searchList.addAll(
            factItemsList.value!!.filter { e -> e.description
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
    val model: String,
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