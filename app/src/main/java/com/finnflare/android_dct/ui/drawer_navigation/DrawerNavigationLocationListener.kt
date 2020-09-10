package com.finnflare.android_dct.ui.drawer_navigation

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Environment
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.finnflare.android_dct.CUIViewModel
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
class DrawerNavigationLocationListener(private val mContext: Context):
    NavigationView.OnNavigationItemSelectedListener, KoinComponent {

    private val network by inject<CNetworkViewModel>()
    private val database by inject<CDatabaseViewModel>()

    private val uiViewModel by inject<CUIViewModel>()

    private var context = mContext

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.d_nav_change_docs_date ->
                DatePickerDialog(
                    mContext,
                    DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                        uiViewModel.year = year
                        uiViewModel.month = monthOfYear
                        uiViewModel.day = dayOfMonth
                    }, uiViewModel.year, uiViewModel.month, uiViewModel.day)
                .show()
            R.id.d_nav_send_to_server -> network.sendActualDocsState(mContext)
            R.id.d_nav_save_results ->
                CoroutineScope(Dispatchers.IO).launch {
                    database.saveToFile(context)
                }
            R.id.d_nav_upload_result_from_file ->
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
            R.id.d_nav_reset_results ->
                CoroutineScope(database.dbDispatcher).launch {
                    database.deleteAllResults()
                }
            R.id.d_nav_setting ->
                (context as FragmentActivity).startActivity(
                    Intent(context, SettingsActivity::class.java)
                )
            R.id.d_nav_info ->
                DialogFragmentInfo().show(
                    (context as FragmentActivity).supportFragmentManager,
                    "dialog_fragment_info"
                )
        }

        return true
    }
}