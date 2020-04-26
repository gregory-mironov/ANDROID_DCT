package com.finnflare.dct_network

import com.finnflare.dct_network.classes.actual_docs.CActualDocsRequest
import com.finnflare.dct_network.classes.actual_docs.CActualDocsResponse
import com.finnflare.dct_network.classes.auth.CAuthRequest
import com.finnflare.dct_network.classes.auth.CAuthResponse
import com.finnflare.dct_network.classes.docs.CDocsRequest
import com.finnflare.dct_network.classes.docs.CDocsResponse
import com.finnflare.dct_network.classes.shops.CShopsRequest
import com.finnflare.dct_network.classes.shops.CShopsResponse
import com.finnflare.dct_network.classes.stocks.CStocksRequest
import com.finnflare.dct_network.classes.stocks.CStocksResponse
import com.finnflare.dct_network.classes.stores.CStoresRequest
import com.finnflare.dct_network.classes.stores.CStoresResponse
import com.finnflare.dct_network.classes.users.CUsersRequest
import com.finnflare.dct_network.classes.users.CUsersResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

internal interface IFinnFlareApiService {
    @POST(".")
    suspend fun getUsers(@Body request: CUsersRequest): Response<CUsersResponse>

    @POST(".")
    suspend fun auth(@Body request: CAuthRequest): Response<CAuthResponse>

    @POST(".")
    suspend fun getShopsList(@Body request: CShopsRequest): Response<CShopsResponse>

    @POST(".")
    suspend fun getStoresList(@Body request: CStoresRequest): Response<CStoresResponse>

    @POST(".")
    suspend fun getDocsList(@Body request: CDocsRequest): Response<CDocsResponse>

    @POST(".")
    suspend fun getStocksList(@Body request: CStocksRequest): Response<CStocksResponse>

    @POST(".")
    suspend fun uploadActualDocs(@Body request: CActualDocsRequest): Response<CActualDocsResponse>
}