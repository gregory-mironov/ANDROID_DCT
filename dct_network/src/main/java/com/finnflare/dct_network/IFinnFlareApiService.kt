package com.finnflare.dct_network

import com.finnflare.dct_network.dataclasses.actual_docs.CActualDocsRequest
import com.finnflare.dct_network.dataclasses.actual_docs.CActualDocsResponse
import com.finnflare.dct_network.dataclasses.auth.CAuthRequest
import com.finnflare.dct_network.dataclasses.auth.CAuthResponse
import com.finnflare.dct_network.dataclasses.docs.CDocsRequest
import com.finnflare.dct_network.dataclasses.docs.CDocsResponse
import com.finnflare.dct_network.dataclasses.shops.CShopsRequest
import com.finnflare.dct_network.dataclasses.shops.CShopsResponse
import com.finnflare.dct_network.dataclasses.stocks.CStocksRequest
import com.finnflare.dct_network.dataclasses.stocks.CStocksResponse
import com.finnflare.dct_network.dataclasses.stores.CStoresRequest
import com.finnflare.dct_network.dataclasses.stores.CStoresResponse
import com.finnflare.dct_network.dataclasses.users.CUsersRequest
import com.finnflare.dct_network.dataclasses.users.CUsersResponse
import retrofit2.http.Body
import retrofit2.http.POST

internal interface IFinnFlareApiService {
    @POST(".")
    suspend fun getUsers(@Body request: CUsersRequest): CUsersResponse

    @POST(".")
    suspend fun auth(@Body request: CAuthRequest): CAuthResponse

    @POST(".")
    suspend fun getShopsList(@Body request: CShopsRequest): CShopsResponse

    @POST(".")
    suspend fun getStoresList(@Body request: CStoresRequest): CStoresResponse

    @POST(".")
    suspend fun getDocsList(@Body request: CDocsRequest): CDocsResponse

    @POST(".")
    suspend fun getStocksList(@Body request: CStocksRequest): CStocksResponse

    @POST(".")
    suspend fun uploadActualDocs(@Body request: CActualDocsRequest): CActualDocsResponse
}