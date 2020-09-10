package com.finnflare.dct_network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

internal object CNetworkService {
    var url = ""
        private set

    internal var Api: IFinnFlareApiService? = null

    fun init(new_url: String?): Boolean {
        if (new_url != null && new_url.isNotEmpty())
            url = new_url

        if (url.isEmpty())
            return false

        Api = Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder().apply {
                if (BuildConfig.DEBUG) {
                    val logging = HttpLoggingInterceptor()
                    logging.level = HttpLoggingInterceptor.Level.BODY
                    this.addInterceptor(logging)
                }
            }.build())
            .build().create(IFinnFlareApiService::class.java)

        return true
    }
}