package com.finnflare.dct_network

import com.finnflare.dct_network.classes.actual_docs.CActualDocsRequest
import com.finnflare.dct_network.classes.actual_docs.CActualDocsResponse
import com.finnflare.dct_network.classes.auth.CAuthRequest
import com.finnflare.dct_network.classes.auth.CAuthResponse
import com.finnflare.dct_network.classes.docs.CDocsRequest
import com.finnflare.dct_network.classes.docs.CDocsResponse
import com.finnflare.dct_network.classes.goods.CGoodsRequest
import com.finnflare.dct_network.classes.goods.CGoodsResponse
import com.finnflare.dct_network.classes.leftovers.CLeftoversRequest
import com.finnflare.dct_network.classes.leftovers.CLeftoversResponse
import com.finnflare.dct_network.classes.marking_codes.CMarkingCodeRequest
import com.finnflare.dct_network.classes.marking_codes.CMarkingCodeResponse
import com.finnflare.dct_network.classes.shops.CShopsRequest
import com.finnflare.dct_network.classes.shops.CShopsResponse
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
    suspend fun getGoodsList(@Body request: CGoodsRequest): Response<CGoodsResponse>

    @POST(".")
    suspend fun getMarkingCodesList(@Body request: CMarkingCodeRequest): Response<CMarkingCodeResponse>

    @POST(".")
    suspend fun getLeftoversList(@Body request: CLeftoversRequest): Response<CLeftoversResponse>

    @POST(".")
    suspend fun uploadActualDocs(@Body request: CActualDocsRequest): Response<CActualDocsResponse>
}