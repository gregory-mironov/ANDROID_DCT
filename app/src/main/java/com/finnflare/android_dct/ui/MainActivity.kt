package com.finnflare.android_dct.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.finnflare.android_dct.ui.login.LoginActivity
import kotlinx.coroutines.ObsoleteCoroutinesApi

class MainActivity : AppCompatActivity() {

    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
