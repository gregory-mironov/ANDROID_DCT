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
class CScannerViewModel(application: Application) : AndroidViewModel(application), KoinComponent {
    val scannerDispatcher: CoroutineDispatcher = newSingleThreadContext("DBCoroutine")
    private val database by inject<CDatabaseViewModel>()
    private val network by inject<CNetworkViewModel>()

    val scanResult = MutableLiveData<Triple<String, String, String>>()
    val scanError = MutableLiveData<String>()

    var rfidRSSI = 0.0

    lateinit var scanner: IScanner

    var docId = ""

    private val stateList = mutableListOf<State>()

    private val searchList = mutableListOf<Item>()

    val planItemsList = MutableLiveData<MutableList<Item>>(mutableListOf())
    val factItemsList = MutableLiveData<MutableList<Item>>(mutableListOf())

    val correctBarcodeItemsCount = MutableLiveData(0)
    val wrongBarcodeItemsCount = MutableLiveData(0)

    val correctRfidItemsCount = MutableLiveData(0)
    val wrongRfidItemsCount = MutableLiveData(0)

    val planItemsCount = MutableLiveData(0)

    init {
        CoroutineScope(Dispatchers.Default).launch {
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

            var plan = 0
            var corr_f_b = 0
            var wrong_f_b = 0
            var corr_f_r = 0
            var wrong_f_r = 0

            for (lo in database.getLeftovers(docId)) {
                val item = Item(
                    lo.guid, lo.description, lo.model.toString(), lo.color.toString(),
                    lo.size.toString(), lo.qtyoutBarcode, lo.qtyoutRfid, lo.qtyin
                )

                if (item.planCount != 0) {
                    planItemsList.value?.add(item)
                    plan += item.planCount
                }

                if (item.rfidCount == 0 && item.barcodeCount == 0)
                    continue

                factItemsList.value?.add(item)

                if (item.planCount < item.barcodeCount)
                    wrong_f_b += item.barcodeCount - item.planCount
                else
                    corr_f_b += item.barcodeCount

                if (item.planCount < item.rfidCount)
                    wrong_f_r += item.rfidCount - item.planCount
                else
                    corr_f_r += item.rfidCount
            }
            wrongBarcodeItemsCount.postValue(wrong_f_b)
            correctBarcodeItemsCount.postValue(corr_f_b)

            wrongRfidItemsCount.postValue(wrong_f_r)
            correctRfidItemsCount.postValue(corr_f_r)

            planItemsCount.postValue(plan)

        }.join()
        CoroutineScope(Dispatchers.Main).launch {
            planItemsList.value = planItemsList.value
            factItemsList.value = factItemsList.value
        }
    }

    suspend fun refreshItemsList() {
        CoroutineScope(database.dbDispatcher).launch {
            network.getLeftoversList(docId)
            getItemsList()
        }.join()
    }

    fun cleanRfidLeftovers() {
        CoroutineScope(database.dbDispatcher).launch {
            database.deleteRfidResults(docId)
        }

        correctRfidItemsCount.value = 0
        wrongRfidItemsCount.value = 0

        // Т.к. внутри ссылки на одни и те же объекты,
        // достаточно обработать лишь один
        factItemsList.value?.apply {
            this.forEach { it.rfidCount = 0 }
            this.removeAll { it.barcodeCount == 0 && it.rfidCount == 0 }
        }
        factItemsList.value = factItemsList.value
        planItemsList.value = planItemsList.value
    }

    fun cleanBarcodeLeftovers() {
        CoroutineScope(database.dbDispatcher).launch {
            database.deleteBarcodeResults(docId)
        }

        correctBarcodeItemsCount.value = 0
        wrongBarcodeItemsCount.value = 0

        // Т.к. внутри ссылки на одни и те же объекты,
        // достаточно обработать лишь один
        factItemsList.value?.apply {
            this.forEach { it.barcodeCount = 0 }
            this.removeAll { it.barcodeCount == 0 && it.rfidCount == 0 }
        }

        factItemsList.value = factItemsList.value
        planItemsList.value = planItemsList.value
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

    fun increaseItemCount(gtin: String, sn: String, rfid: String) {
        val guid = database.getMCByGtin(gtin)?.mGuid.toString()

        when (database.scanResultProcessing(docId, gtin, sn, rfid)) {
            -4, -3 -> {
                factItemsList.value!!.add(
                    Item(
                        guid = guid,
                        description = "",
                        model = "",
                        color = "",
                        size = "",
                        rfidCount = 1
                    )
                )

                wrongRfidItemsCount.postValue(wrongRfidItemsCount.value!!.plus(1))
            }
            -2 -> {
                planItemsList.value!!.find { it.guid == guid }!!.let {
                    it.rfidCount = 1

                    factItemsList.value!!.add(it)
                }

                correctRfidItemsCount.postValue(correctRfidItemsCount.value!!.plus(1))
            }
            1 -> {
                // planitemsList не нужно инкрементировать, т.к.
                // внутри ссылки на одни и те же объекты
                factItemsList.value!!.find { it.guid == guid }!!.let {
                    it.barcodeCount++

                    if (it.planCount < it.barcodeCount)
                        wrongBarcodeItemsCount.postValue(wrongBarcodeItemsCount.value!!.plus(1))
                    else
                        correctBarcodeItemsCount.postValue(correctBarcodeItemsCount.value!!.plus(1))
                }
            }
            2 -> {
                planItemsList.value!!.find { it.guid == guid }!!.let {
                    it.barcodeCount = 1

                    factItemsList.value!!.add(it)
                }

                correctBarcodeItemsCount.postValue(correctBarcodeItemsCount.value!!.plus(1))
            }
            3, 4 -> {
                factItemsList.value!!.add(
                    Item(
                        guid = guid,
                        description = "",
                        model = "",
                        color = "",
                        size = "",
                        barcodeCount = 1
                    )
                )

                wrongBarcodeItemsCount.postValue(wrongBarcodeItemsCount.value!!.plus(1))
            }
        }
    }

    fun getItemData(gtin: String, sn: String = "", rfid: String = ""):
            Pair<String, Triple<String, String, String>> {

        val mc = database.getMCByGtin(gtin) ?: return findItemDataFromRemote()

        val items = planItemsList.value!!.filter { it.guid == mc.mGuid }

        if (items.isEmpty())
            return findItemDataFromRemote()

        items[0].let { item ->
            stateList.filter { it.state_id == mc.mState }

            return Pair(
                item.description, Triple(
                    item.color,
                    item.size,
                    if (stateList.isNotEmpty()) stateList[0].state_name else ""
                )
            )
        }
    }

    private fun findItemDataFromRemote(): Pair<String, Triple<String, String, String>> {
        return Pair("", Triple("", "", ""))
    }

    fun updateSearchList(mask: String) {
        searchList.clear()
        searchList.addAll(
            planItemsList.value!!.filter { e ->
                e.description
                    .toLowerCase(Locale.getDefault())
                    .contains(mask.toLowerCase(Locale.getDefault()))
            }
        )
        searchList.addAll(
            factItemsList.value!!.filter { e ->
                e.description
                    .toLowerCase(Locale.getDefault())
                    .contains(mask.toLowerCase(Locale.getDefault()))
            }
        )
    }

    fun cmpScanResWithSearchList(gtin: String, sn: String, rfid: String): Boolean {
        if (rfid.isEmpty())
            return false

        val mc = database.getMCByGtin(gtin) ?: return false

        return searchList.any { e -> mc.mGuid == e.guid }
    }
}

data class MarkingCode(
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
) {
    override fun equals(other: Any?): Boolean {
        if (other !is Item)
            return false

        return description == other.description || guid == other.guid
    }

    override fun hashCode(): Int {
        var result = guid.hashCode()
        result = 31 * result + description.hashCode()
        result = 31 * result + model.hashCode()
        result = 31 * result + color.hashCode()
        result = 31 * result + size.hashCode()
        result = 31 * result + barcodeCount
        result = 31 * result + rfidCount
        result = 31 * result + planCount
        return result
    }
}

data class State(
    val state_id: String,
    val state_name: String
)