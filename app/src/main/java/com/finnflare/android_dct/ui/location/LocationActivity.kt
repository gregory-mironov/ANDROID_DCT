package com.finnflare.android_dct.ui.location

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentTransaction
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.drawer_navigation.DrawerNavigationListener
import com.finnflare.android_dct.ui.items.ItemsActivity
import com.finnflare.android_dct.ui.location.location.DummyLocationChooseFragmentContent
import com.finnflare.android_dct.ui.location.location.LocationChooseFragment
import com.finnflare.android_dct.ui.location.storage.DummyStorageChooseFragmentContent
import com.finnflare.android_dct.ui.location.storage.StorageChooseFragment
import com.google.android.material.navigation.NavigationView


class LocationActivity : AppCompatActivity(),
    LocationChooseFragment.OnListLocationChooseFragmentInteractionListener,
    StorageChooseFragment.OnListStorageChooseFragmentInteractionListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        configureToolbar()

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.a_location_placeholder, LocationChooseFragment())
        ft.commit()

        val drawerNavigationView = findViewById<NavigationView>(R.id.drawer_navigation_view)
        DrawerNavigationListener.context = this
        drawerNavigationView.setNavigationItemSelectedListener(DrawerNavigationListener)
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById(R.id.a_location_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.a_location_title)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_location, menu)
        return true
    }

    override fun onListLocationChooseFragmentInteraction(item: DummyLocationChooseFragmentContent.LocationDummyItem?) {
//        TODO("Change onListLocation...Interaction")

        if (item != null) {
            Toast.makeText(getApplicationContext(), item.title + " clicked", Toast.LENGTH_SHORT).show()
        }

        val ft: FragmentTransaction = supportFragmentManager.beginTransaction()
        ft.replace(R.id.a_location_placeholder, StorageChooseFragment())
        ft.addToBackStack("Some string")
        ft.commit()
    }

    override fun onListStorageChooseFragmentInteraction(item: DummyStorageChooseFragmentContent.StorageDummyItem?) {
//        TODO("Change onListStorage...Interaction")

        val intent = Intent(this, ItemsActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findViewById<DrawerLayout>(R.id.a_location_drawer_layout).
                    openDrawer(GravityCompat.START)
        }
        return true
    }
}
