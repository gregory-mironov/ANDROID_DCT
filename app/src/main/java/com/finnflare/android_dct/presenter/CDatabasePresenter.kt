package com.finnflare.android_dct.presenter

import com.finnflare.android_dct.repository.database.request_results.CAppDatabase
import org.koin.core.KoinComponent
import org.koin.core.inject

class CDatabasePresenter: KoinComponent {
    private val database by inject<CAppDatabase>()
}