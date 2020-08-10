package com.finnflare.android_dct.ui.drawer_navigation

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.finnflare.android_dct.CUIViewModel
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.SettingsActivity
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ObsoleteCoroutinesApi
class DrawerNavigationLocationListener(private val mContext: Context):
    NavigationView.OnNavigationItemSelectedListener, KoinComponent {

    private val network by inject<CNetworkViewModel>()
    private val database by inject<CDatabaseViewModel>()

    private val uiViewModel by inject<CUIViewModel>()

    private var context: Context? = null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.d_nav_change_docs_date -> {

                DatePickerDialog(
                    mContext,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        uiViewModel.year = year
                        uiViewModel.month = monthOfYear
                        uiViewModel.day = dayOfMonth
                    }, uiViewModel.year, uiViewModel.month, uiViewModel.day)
                .show()
            }
            R.id.d_nav_send_to_server -> {
                network.sendActualDocsState()
            }
            R.id.d_nav_reset_rfid_results -> {
                CoroutineScope(database.dbDispatcher).launch {
                    database.deleteAllRfidResults()
                }
            }
            R.id.d_nav_reset_barcode_results -> {
                CoroutineScope(database.dbDispatcher).launch {
                    database.deleteAllBarcodeResults()
                }
            }
            R.id.d_nav_setting -> {
                val intent = Intent(context, SettingsActivity::class.java)
                (context as FragmentActivity).startActivity(intent)
            }
            R.id.d_nav_info -> {
                val fm = (context as FragmentActivity).supportFragmentManager
                val dialogFragment = DialogFragmentInfo()
                dialogFragment.show(fm, "dialog_fragment_info")
            }
        }

        return true
    }
}