package com.finnflare.android_dct.ui.location

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.finnflare.android_dct.CUIViewModel
import com.finnflare.android_dct.Document
import com.finnflare.android_dct.Location
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.drawer_navigation.DrawerNavigationListener
import com.finnflare.android_dct.ui.items.ItemsActivity
import com.finnflare.android_dct.ui.location.document.DocumentChooseFragment
import com.finnflare.android_dct.ui.location.location.LocationChooseFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class LocationActivity : AppCompatActivity(),
    LocationChooseFragment.OnListLocationChooseFragmentInteractionListener,
    DocumentChooseFragment.OnListDocumentChooseFragmentInteractionListener{

    private val uiViewModel by inject<CUIViewModel>()

    val IS_FIRST_START_KEY = "isFirstStart"
    var isFirstStart: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location)

        if (savedInstanceState != null)
            isFirstStart = savedInstanceState.getBoolean(IS_FIRST_START_KEY)

        configureToolbar()

        if (isFirstStart)
            supportFragmentManager.beginTransaction().apply {
                this.replace(R.id.a_location_placeholder, LocationChooseFragment())
                this.commit()
            }

        GlobalScope.launch {
            uiViewModel.getLocationsList()
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
        val toolbar: Toolbar = findViewById(R.id.a_location_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            title = getString(R.string.a_location_title)
            supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_menu_white)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu_location, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> findViewById<DrawerLayout>(R.id.a_location_drawer_layout).
            openDrawer(GravityCompat.START)
            R.id.a_location_menu_search -> {
                findViewById<EditText>(R.id.locationSearchEditText)?.let {
                    it.visibility = if (it.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
                findViewById<EditText>(R.id.documentSearchEditText)?.let {
                    it.visibility = if (it.visibility == View.VISIBLE) View.GONE else View.VISIBLE
                }
            }
        }
        return true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        isFirstStart = false
        outState.putBoolean(IS_FIRST_START_KEY, isFirstStart)
    }

    override fun onListLocationChooseFragmentInteraction(item: Location) {
        GlobalScope.launch {
            uiViewModel.getDocumentsList(item.id)
        }
        supportFragmentManager.beginTransaction().apply {
            this.replace(R.id.a_location_placeholder, DocumentChooseFragment())
            this.addToBackStack("Some string")
            this.commit()
        }
    }

    override fun onListDocumentChooseFragmentInteraction(item: Document) {
        val intent = Intent(this, ItemsActivity::class.java)
        startActivity(intent)
    }
}
