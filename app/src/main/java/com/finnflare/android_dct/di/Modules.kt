package com.finnflare.android_dct.di

import com.finnflare.scanner.CScannerViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val viewModelsModule = module(override = true) {
    single { CScannerViewModel(androidApplication()) }
}