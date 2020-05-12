package com.finnflare.android_dct.ui.items

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.finnflare.android_dct.R
import com.finnflare.android_dct.di.FragmentInstance.factItemsListFragment
import com.finnflare.android_dct.di.FragmentInstance.itemScanFragment
import com.finnflare.android_dct.di.FragmentInstance.itemSearchFragment
import com.finnflare.android_dct.di.FragmentInstance.planItemsListFragment
import com.finnflare.android_dct.di.FragmentInstance.rfidItemScanFragment
import com.finnflare.android_dct.ui.drawer_navigation.DrawerNavigationListener
import com.finnflare.android_dct.ui.items.fact.ContentFactItemsListFragment
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.DummyPlanItemsListFragmentContent
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class ItemsActivity : AppCompatActivity(),
    PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener,
    FactItemsListFragment.OnListFactItemsListFragmentInteractionListener {

    val IS_FIRST_START_KEY = "isFirstStart"
    var isFirstStart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        configureToolbar()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.a_items_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener)

        if (savedInstanceState != null)
            isFirstStart = savedInstanceState.getBoolean(IS_FIRST_START_KEY)

        if (isFirstStart) {
            supportFragmentManager.beginTransaction().replace(
                R.id.a_items_placeholder,
                itemScanFragment
            ).commit()
        }
    }

    override fun onStart() {
        super.onStart()
        setDrawerNavigationListener()
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
        object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val selectedFragment: Fragment? = when (item.itemId) {
                    R.id.barcode_nav -> itemScanFragment
                    R.id.rfid_nav -> rfidItemScanFragment
                    R.id.plan_nav -> planItemsListFragment
                    R.id.fact_nav -> factItemsListFragment
                    R.id.search_nav -> itemSearchFragment
                    else -> throw Exception("bottomNavigationView got incorrect itemId")
                }
                if (selectedFragment != null) {
                    supportFragmentManager.beginTransaction().replace(
                        R.id.a_items_placeholder,
                        selectedFragment
                    ).commit()
                }
                return true
            }
        }

    override fun onListPlanItemsListFragmentInteraction(item: DummyPlanItemsListFragmentContent.PlanDummyItem?) {
//        TODO("Change onListPlan...Interaction")
        if (item != null) {
            Toast.makeText(getApplicationContext(), item.title + " clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onListFactItemsListFragmentInteraction(item: ContentFactItemsListFragment.FactDummyItem?) {
//        TODO("Change onListFact...Interaction")
        if (item != null) {
            Toast.makeText(getApplicationContext(), item.title + " clicked", Toast.LENGTH_SHORT).show()
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
}
