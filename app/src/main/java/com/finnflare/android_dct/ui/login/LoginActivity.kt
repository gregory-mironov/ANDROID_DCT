package com.finnflare.android_dct.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.finnflare.android_dct.CUIViewModel
import com.finnflare.android_dct.R
import com.finnflare.android_dct.ui.SettingsActivity
import com.finnflare.android_dct.ui.location.LocationActivity
import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.android.inject


@ObsoleteCoroutinesApi
class LoginActivity : AppCompatActivity() {
    private val network by inject<CNetworkViewModel>()
    private val database by inject<CDatabaseViewModel>()
    private val viewModel by inject<CUIViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        configureToolbar()

        findViewById<Button>(R.id.a_login_button).setOnClickListener {
            network.checkAuth(
                this,
                a_login_login_field.text.toString(),
                a_login_password_field.text.toString()
            )
        }

        configureObservers()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.login_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.login_settings -> this.startActivity(
                Intent(this, SettingsActivity::class.java)
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById(R.id.a_login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.a_login_title)
    }

    private fun configureObservers() {
        network.authSuccessful.observe(this, Observer {
            if (!it)
                database.checkUser(
                    a_login_login_field.text.toString(),
                    a_login_password_field.text.toString()
                )
            else {
                startActivity(Intent(this@LoginActivity, LocationActivity::class.java))
                finish()
            }
        })

        database.authSuccessful.observe(this, Observer {
            if (!it)
                return@Observer

            startActivity(Intent(this@LoginActivity, LocationActivity::class.java))
            finish()
        })
    }
}
