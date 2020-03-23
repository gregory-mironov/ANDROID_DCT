package com.finnflare.android_dct.repository.networkservice

import org.koin.core.KoinComponent

class CNetworkService: KoinComponent {
    val apiService = IFinnFlareApiService.create()

    suspend fun getUsersFromServer() { }

    suspend fun validateUser() { }

    suspend fun getLocationList() { }

    suspend fun getStorageList() { }

    suspend fun getLeftoversFromServer() { }

    suspend fun uploadLeftoversToServer() { }
}