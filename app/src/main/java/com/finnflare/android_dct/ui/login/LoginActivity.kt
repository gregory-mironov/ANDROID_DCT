package com.finnflare.android_dct.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import com.finnflare.android_dct.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        configureToolbar()

        findViewById<Button>(R.id.a_login_button).setOnClickListener {
            network.checkAuth(a_login_login_field.text.toString(), a_login_password_field.text.toString())
        }

        configureObservers()
    }

    private fun configureToolbar() {
        val toolbar: Toolbar = findViewById(R.id.a_login_toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = getString(R.string.a_login_title)
    }

    private fun configureObservers() {
        network.authSuccessful.observe(this, Observer {
            database.checkUser(a_login_login_field.text.toString(), a_login_password_field.text.toString())
        })

        database.authSuccessful.observe(this, Observer {
            if (it)
                startActivity(Intent(this@LoginActivity, LocationActivity::class.java))
        })
    }
}
