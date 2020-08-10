package com.finnflare.dct_network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object CNetworkService {
    private const val baseURL = "https://api.finnflare.com:48014/tsd/hs/im/api/"

    val Api: IFinnFlareApiService

    init {
        val client = OkHttpClient.Builder()

        if (BuildConfig.DEBUG) {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            client.addInterceptor(logging)
        }

        Api = Retrofit.Builder()
            .baseUrl(baseURL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(client.build())
            .build().create(IFinnFlareApiService::class.java)
    }
}