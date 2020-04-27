package com.finnflare.android_dct.di

import com.finnflare.dct_database.CDatabaseViewModel
import com.finnflare.dct_network.CNetworkViewModel
import com.finnflare.scanner.CScannerViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

@ObsoleteCoroutinesApi
val viewModelsModule = module(override = true) {
    single { CScannerViewModel(androidApplication()) }
    single { CNetworkViewModel(androidApplication()) }
    single { CDatabaseViewModel(androidApplication()) }
}