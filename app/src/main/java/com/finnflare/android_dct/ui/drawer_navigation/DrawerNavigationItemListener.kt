package com.finnflare.android_dct.ui.drawer_navigation

import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.SettingsActivity
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import com.google.android.material.navigation.NavigationView
import com.obsez.android.lib.filechooser.ChooserDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.inject

@ObsoleteCoroutinesApi
class DrawerNavigationItemListener(
    private val context: Context,
    private val docId: String):
    NavigationView.OnNavigationItemSelectedListener, KoinComponent {

    private val network by inject<CNetworkViewModel>()
    private val database by inject<CDatabaseViewModel>()

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.d_nav_send_to_server -> {
                network.sendActualDocState(docId)
            }
            R.id.d_nav_save_results -> {
                CoroutineScope(Dispatchers.IO).launch {
                    database.saveToFile(context, docId)
                }
            }
            R.id.d_nav_upload_result_from_file -> {
                ChooserDialog(context)
                    .withFilter(false, false, "json")
                    .displayPath(true)
                    .withStartFile(
                        context.getExternalFilesDir(
                            Environment.DIRECTORY_DOWNLOADS
                        )?.absolutePath
                    )
                    .enableOptions(true)
                    .withChosenListener { _, pathFile ->
                        CoroutineScope(Dispatchers.IO).launch {
                            database.uploadFromFile(pathFile)
                        }
                    }
                    .cancelOnTouchOutside(true)
                    .build()
                    .show()
            }
            R.id.d_nav_reset_rfid_results -> {
                CoroutineScope(database.dbDispatcher).launch {
                    database.deleteRfidResults(docId)
                }
            }
            R.id.d_nav_reset_barcode_results -> {
                CoroutineScope(database.dbDispatcher).launch {
                    database.deleteBarcodeResults(docId)
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