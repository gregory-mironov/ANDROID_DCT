package com.finnflare.android_dct.di

import com.finnflare.android_dct.repository.database.request_results.CAppDatabase
import com.finnflare.scanner.CScannerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presentersModule = module(override = true) {
    single { CAppDatabase.getInstance(androidContext())}
}
val viewModelsModule = module(override = true) {
    single { CScannerViewModel(androidApplication()) }
}