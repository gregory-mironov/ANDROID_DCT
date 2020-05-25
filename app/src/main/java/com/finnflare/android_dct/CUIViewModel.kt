package com.finnflare.android_dct

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.finnflare.android_dct.ui.items.barcode.ItemScanFragment
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.finnflare.android_dct.ui.items.rfid.RFIDItemScanFragment
import com.finnflare.android_dct.ui.items.search.ItemSearchFragment
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject
import java.text.SimpleDateFormat
import java.util.*

@ObsoleteCoroutinesApi
class CUIViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    private val dateISOFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale("ru"))

    private val networkViewModel by  inject<CNetworkViewModel>()
    private val databaseViewModel by inject<CDatabaseViewModel>()

    val rfidEnabled = when (Build.MANUFACTURER) {
        "Alien" -> Build.MODEL == "ALR-H450"
        else -> false
    }

    //Fragments
    val fragmentsList = listOf(
        ItemScanFragment.newInstance(rfidEnabled),
        RFIDItemScanFragment.newInstance(rfidEnabled),
        PlanItemsListFragment(),
        FactItemsListFragment(),
        ItemSearchFragment.newInstance(rfidEnabled)
    )
    var selectedFragment = 2

    val locationList = MutableLiveData<MutableList<Location>>(mutableListOf())
    val documentList = MutableLiveData<MutableList<Document>>(mutableListOf())

    fun getLocationsList() {
        CoroutineScope(databaseViewModel.dbDispatcher).launch {
            networkViewModel.getLocationsList()

            locationList.value?.let { list ->
                list.clear()
                databaseViewModel.getLocationsList().forEach {
                   list.add(Location(it.mDescription, "", it.mId))
                }
            }

            locationList.postValue(locationList.value)
        }
    }

    fun getDocumentsList(locationId: String) {
        CoroutineScope(databaseViewModel.dbDispatcher).launch {
            networkViewModel.getDocsList(dateISOFormatter.format(Date()), locationId)

            documentList.value?.let { list ->
                list.clear()
                databaseViewModel.getDocsList().forEach {
                    list.add(
                        Document(
                            it.mNumber,
                            it.mBasis,
                            it.mAuditor,
                            it.mQty,
                            it.mQtyFact,
                            it.mId
                        )
                    )
                }
            }

            documentList.postValue(documentList.value)
        }
    }
}

data class Location(
    val title: String,
    val subtitle: String,
    val id: String
)

data class Document(
    val title: String,
    val basis: String,
    val auditor: String,
    val qtyin: Int,
    var qtyout: Int,
    val id: String
)