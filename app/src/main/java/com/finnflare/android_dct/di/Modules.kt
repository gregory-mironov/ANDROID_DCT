package com.finnflare.android_dct.di

import com.finnflare.android_dct.presenter.CDatabasePresenter
import com.finnflare.android_dct.presenter.CNetworkPresenter
import com.finnflare.android_dct.repository.database.request_results.CAppDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val presentersModule = module(override = true) {
    single { CNetworkPresenter() }
    single { CAppDatabase.getInstance(androidContext())}
}