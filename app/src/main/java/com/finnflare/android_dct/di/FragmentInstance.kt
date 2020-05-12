package com.finnflare.android_dct.di

import com.finnflare.android_dct.ui.items.barcode.ItemScanFragment
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.finnflare.android_dct.ui.items.rfid.RFIDItemScanFragment
import com.finnflare.android_dct.ui.items.search.ItemSearchFragment

object FragmentInstance {
    val itemScanFragment = ItemScanFragment()
    val rfidItemScanFragment = RFIDItemScanFragment()
    val planItemsListFragment = PlanItemsListFragment()
    val factItemsListFragment = FactItemsListFragment()
    val itemSearchFragment = ItemSearchFragment()
}