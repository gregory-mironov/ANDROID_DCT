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