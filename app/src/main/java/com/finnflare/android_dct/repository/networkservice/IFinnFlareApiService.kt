package com.finnflare.android_dct.repository.networkservice

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

interface IFinnFlareApiService {

    companion object {
        fun create(): IFinnFlareApiService {
            return Retrofit.Builder()
                .client(
                    OkHttpClient()
                        .newBuilder()
                        .build()
                )
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(MoshiConverterFactory.create())
                .baseUrl("https://api.finnflare.com:48011")
                .build()
                .create(IFinnFlareApiService::class.java)


        }
    }
}