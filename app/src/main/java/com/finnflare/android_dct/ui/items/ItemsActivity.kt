package com.finnflare.android_dct.ui.items

import android.os.Bundle
import android.view.KeyEvent
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.finnflare.android_dct.CUIViewModel
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.drawer_navigation.DrawerNavigationListener
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.finnflare.scanner.CScannerViewModel
import com.finnflare.scanner.Item
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

@ObsoleteCoroutinesApi
class ItemsActivity : AppCompatActivity(),
    PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener,
    FactItemsListFragment.OnListFactItemsListFragmentInteractionListener {

    private val scannerViewModel by inject<CScannerViewModel>()
    private val uiViewModel by inject<CUIViewModel>()

    val IS_FIRST_START_KEY = "isFirstStart"
    var isFirstStart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        configureToolbar()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.a_items_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener)
        bottomNavigationView.selectedItemId = R.id.plan_nav

        if (savedInstanceState != null)
            isFirstStart = savedInstanceState.getBoolean(IS_FIRST_START_KEY)

        if (isFirstStart) {
            uiViewModel.selectedFragment = 2
            supportFragmentManager.beginTransaction().replace(
                R.id.a_items_placeholder,
                uiViewModel.fragmentsList[uiViewModel.selectedFragment]
            ).commit()
        }

        GlobalScope.launch {
            scannerViewModel.getItemsLists()
        }
    }

    override fun onStart() {
        super.onStart()
        setDrawerNavigationListener()
    }

    override fun onStop() {
        super.onStop()
        scannerViewModel.scanner.deinit()
    }

    private fun setDrawerNavigationListener() {
        val drawerNavigationView = findViewById<NavigationView>(R.id.drawer_navigation_view)
        DrawerNavigationListener.context = this
        drawerNavigationView.setNavigationItemSelectedListener(DrawerNavigationListener)
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById(R.id.a_items_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.a_items_title)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private val bottomNavigationListener: BottomNavigationView.OnNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            uiViewModel.selectedFragment = when (item.itemId) {
                R.id.barcode_nav -> 0
                R.id.rfid_nav -> 1
                R.id.plan_nav -> 2
                R.id.fact_nav -> 3
                R.id.search_nav -> 4
                else -> throw Exception("bottomNavigationView got incorrect itemId")
            }

            supportFragmentManager.beginTransaction().replace(
                R.id.a_items_placeholder,
                uiViewModel.fragmentsList[uiViewModel.selectedFragment]
            ).commit()

            true
        }

    override fun onListPlanItemsListFragmentInteraction(item: Item?) {
        if (item != null) {
            Toast.makeText(applicationContext, item.description + " clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onListFactItemsListFragmentInteraction(item: Item?) {
        if (item != null) {
            Toast.makeText(applicationContext, item.description + " clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findViewById<DrawerLayout>(R.id.a_items_drawer_layout).
            openDrawer(GravityCompat.START)
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        isFirstStart = false
        outState.putBoolean(IS_FIRST_START_KEY, isFirstStart)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (uiViewModel.selectedFragment == 0)
            scannerViewModel.scanner.startBarcodeScan(keyCode, event!!)

        if (uiViewModel.selectedFragment == 1)
            scannerViewModel.scanner.startRFIDScan(keyCode, event!!)

        return super.onKeyDown(keyCode, event)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        if (uiViewModel.selectedFragment == 0)
            scannerViewModel.scanner.stopBarcodeScan(keyCode, event!!)

        if (uiViewModel.selectedFragment == 1)
            scannerViewModel.scanner.stopRFIDScan(keyCode, event!!)

        return super.onKeyUp(keyCode, event)
    }
}
