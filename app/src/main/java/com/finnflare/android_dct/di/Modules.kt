package com.finnflare.android_dct.di

import com.finnflare.android_dct.presenter.CNetworkPresenter
import org.koin.dsl.module

val presentersModule = module(override = true) {
    single { CNetworkPresenter() }
}