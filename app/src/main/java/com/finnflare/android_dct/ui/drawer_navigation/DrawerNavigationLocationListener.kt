package com.finnflare.android_dct.ui.drawer_navigation

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import androidx.fragment.app.FragmentActivity
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.SettingsActivity
import com.google.android.material.navigation.NavigationView

object DrawerNavigationLocationListener: NavigationView.OnNavigationItemSelectedListener {

    var context: Context? = null

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.d_nav_send_to_server -> {
                Log.w("Drawer-navigation", "Save results not implemented")
            }
            R.id.d_nav_save_results -> {
                Log.w("Drawer-navigation", "Save results not implemented")
            }
            R.id.d_nav_upload_result_from_file -> {
                Log.w("Drawer-navigation", "Upload result from file not implemented")
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