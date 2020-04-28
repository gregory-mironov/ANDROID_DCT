package com.finnflare.android_dct

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.finnflare.android_dct.di.viewModelsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class CApplicationDelegate : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin{
            androidLogger()
            androidContext(this@CApplicationDelegate)
            modules(viewModelsModule)
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}