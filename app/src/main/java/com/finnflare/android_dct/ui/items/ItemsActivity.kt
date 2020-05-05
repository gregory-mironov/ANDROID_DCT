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
import com.finnflare.android_dct.ui.drawer_navigation.DrawerNavigationListener
import com.finnflare.android_dct.ui.items.barcode.ItemScanFragment
import com.finnflare.android_dct.ui.items.fact.ContentFactItemsListFragment
import com.finnflare.android_dct.ui.items.fact.FactItemsListFragment
import com.finnflare.android_dct.ui.items.plan.DummyPlanItemsListFragmentContent
import com.finnflare.android_dct.ui.items.plan.PlanItemsListFragment
import com.finnflare.android_dct.ui.items.rfid.RFIDItemScanFragment
import com.finnflare.android_dct.ui.items.search.ItemSearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class ItemsActivity : AppCompatActivity(),
    PlanItemsListFragment.OnListPlanItemsListFragmentInteractionListener,
    FactItemsListFragment.OnListFactItemsListFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_items)

        configureToolbar()

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.a_items_bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavigationListener)

        val drawerNavigationView = findViewById<NavigationView>(R.id.drawer_navigation_view)
        DrawerNavigationListener.context = this
        drawerNavigationView.setNavigationItemSelectedListener(DrawerNavigationListener)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.a_items_placeholder,
                ItemScanFragment()
            ).commit()
        }
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
                    R.id.barcode_nav -> ItemScanFragment()
                    R.id.rfid_nav -> RFIDItemScanFragment()
                    R.id.plan_nav -> PlanItemsListFragment()
                    R.id.fact_nav ->  FactItemsListFragment()
                    R.id.search_nav -> ItemSearchFragment()
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
}
