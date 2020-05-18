package com.finnflare.android_dct

import android.app.Application
import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import com.finnflare.android_dct.ui.items.barcode.ItemScanFragment
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.finnflare.android_dct.ui.items.rfid.RFIDItemScanFragment
import com.finnflare.android_dct.ui.items.search.ItemSearchFragment
import org.koin.core.KoinComponent

class CUIViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    var selectedFragment: Fragment? = null

    val itemScanFragment = ItemScanFragment(when (Build.MANUFACTURER) {
        "Alien" -> Build.MODEL == "ALR-H450"
        else -> false
    })

    val rfidItemScanFragment = RFIDItemScanFragment(when (Build.MANUFACTURER) {
        "Alien" -> Build.MODEL == "ALR-H450"
        else -> false
    })
    val planItemsListFragment = PlanItemsListFragment()
    val factItemsListFragment = FactItemsListFragment()
    val itemSearchFragment = ItemSearchFragment()
}