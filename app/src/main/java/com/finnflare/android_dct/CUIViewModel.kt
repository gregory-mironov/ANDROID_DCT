package com.finnflare.android_dct

import android.app.Application
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import com.finnflare.android_dct.ui.items.barcode.ItemScanFragment
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.finnflare.android_dct.ui.items.rfid.RFIDItemScanFragment
import com.finnflare.android_dct.ui.items.search.ItemSearchFragment
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.core.KoinComponent
import org.koin.core.inject

@ObsoleteCoroutinesApi
class CUIViewModel(application: Application): AndroidViewModel(application), KoinComponent {

    private val networkViewModel by  inject<CNetworkViewModel>()
    private val databaseViewModel by inject<CDatabaseViewModel>()

    //Fragments
    val fragmentsList = listOf(
        ItemScanFragment(when (Build.MANUFACTURER) {
            "Alien" -> Build.MODEL == "ALR-H450"
            else -> false
        }),
        RFIDItemScanFragment(when (Build.MANUFACTURER) {
            "Alien" -> Build.MODEL == "ALR-H450"
            else -> false
        }),
        PlanItemsListFragment(),
        FactItemsListFragment(),
        ItemSearchFragment()
    )
    var selectedFragment = 2

    val locationList = mutableListOf<Location>()
    val documentList = mutableListOf<Document>()

    fun getLocationsList() {
        locationList.clear()
        for (i in 1..25)
            locationList.add(
                Location("Location $i", 'A' + (i - 1) % 26 + " documents", "")
            )
    }

    fun getDocumentsList(locationId: String) {
        documentList.clear()
        for (i in 1..25)
            documentList.add(
                Document("Document $i", "User\'s comment $i"))
    }
}

data class Location(
    val title: String,
    val subtitle: String,
    val id: String
)

data class Document(val title: String, val subtitle: String)